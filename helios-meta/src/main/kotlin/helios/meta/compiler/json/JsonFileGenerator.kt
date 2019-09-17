package helios.meta.compiler.json

import arrow.common.Package
import arrow.common.utils.ClassOrPackageDataWrapper
import arrow.common.utils.extractFullName
import arrow.common.utils.removeBackticks
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.ProtoBuf
import java.io.File

data class JsonElement(
  val `package`: Package,
  val target: JsonAnnotated
) {
  val properties: List<ProtoBuf.Property> =
    (target.classOrPackageProto as ClassOrPackageDataWrapper.Class).propertyList
  val tparams: List<ProtoBuf.TypeParameter> = target.classOrPackageProto.typeParameters
  val name: String = target.classElement.simpleName.toString()
  val pairs: List<Pair<String, String>> = properties.map {
    val retType = it.returnType.extractFullName(
      target.classOrPackageProto as ClassOrPackageDataWrapper.Class,
      true
    )
    val pname = target.classOrPackageProto.nameResolver.getString(it.name)
    pname to retType.removeBackticks()
  }

}

class JsonFileGenerator(
  private val generatedDir: File,
  jsonAnnotatedList: List<JsonAnnotated>
) {

  private val jsonElements: List<JsonElement> =
    jsonAnnotatedList.map { JsonElement(it.classOrPackageProto.`package`, it) }

  /**
   * Main entry point for json extension generation
   */
  fun generate() {
    jsonElements.forEachIndexed { _, je ->
      val elementsToGenerate = listOf(genFromJson(je), genEncoderInstance(je))
      val source: String = elementsToGenerate.joinToString(
        prefix = listOf(
          "package ${je.`package`}",
          "",
          "import arrow.*",
          "import arrow.core.*",
          "import helios.core.*",
          "import helios.instances.*",
          "import helios.typeclasses.*",
          "import helios.syntax.json.*",
          ""
        ).joinToString("\n"), separator = "\n", postfix = "\n"
      )
      val file = File(
        generatedDir,
        jsonAnnotationClass.simpleName + ".${je.target.classElement.qualifiedName}.kt"
      )
      file.writeText(source)
    }
  }

  private inline val String.isComplex get() = this.contains('<')
  private inline val String.notClosed get() = !(this.contains('<') && this.contains('>'))

  private inline val String.getTypeParameters
    get() = {
      val inside = this.substringAfter('<').substringBeforeLast('>')
      inside.split(',').map { it.trim() }.fold(emptyList()) { acc: List<String>, str: String ->
        val maybeLast = acc.lastOrNull()
        if (maybeLast != null && maybeLast.isComplex && maybeLast.notClosed)
          acc.subList(0, acc.size - 1) + "$maybeLast, $str"
        else acc + str
      }
    }()

  private fun String.complexEncoder(pre: String) = getTypeParameters.joinToString(
    prefix = "$pre(",
    postfix = ")"
  ) { it.encoder() }

  private fun String.keyEncoder(): String = "${this.split('.').last()}KeyEncoder"

  private fun String.encoder(): String =
    when {
      this.endsWith('?') -> "NullableEncoder<${substringBeforeLast('?')}>(${substringBefore('?').encoder()})"
      this.startsWith("kotlin.collections.List") -> complexEncoder("ListEncoder")
      this.startsWith("kotlin.collections.Map") ->
        "MapEncoder<${getTypeParameters.joinToString()}>(${getTypeParameters.first().keyEncoder()}, ${getTypeParameters.last().encoder()})"
      this.startsWith("java") -> "${substringAfterLast('.')}Encoder.instance"
      this.contains('<') -> complexEncoder("${substringBefore('<')}.Companion.encoder")
      else -> "$this.encoder()"
    }

  private fun jsonProperties(je: JsonElement): String =
    je.pairs.joinToString(",\n", "JsObject(mapOf(\n", "\n))") { (p, r) ->
      """
         |  "$p" to ${r.encoder()}.run { $p.encode() }
         """.trimMargin()
    }

  private fun String.complexDecoder(pre: String) = getTypeParameters.joinToString(
    prefix = "$pre(",
    postfix = ")"
  ) { it.decoder() }

  private fun String.keyDecoder(): String = "${this.split('.').last()}KeyDecoder"

  private fun String.decoder(): String =
    when {
      this.endsWith('?') -> "NullableDecoder<${substringBeforeLast('?')}>(${substringBefore('?').decoder()})"
      this.startsWith("kotlin.collections.List") ->
        complexDecoder("ListDecoder")
      this.startsWith("kotlin.collections.Map") ->
        "MapDecoder<${getTypeParameters.joinToString()}>(${getTypeParameters.first().keyDecoder()}, ${getTypeParameters.last().decoder()})"
      this.startsWith("java") -> "${substringAfterLast('.')}Decoder.instance"
      this.contains('<') -> complexDecoder("${substringBefore('<')}.Companion.decoder")
      else -> "$this.decoder()"
    }

  private fun genFromJson(je: JsonElement): String {

    val extraImports = if (je.pairs.size != 1) "import arrow.core.extensions.either.applicative.applicative" else ""

    val params = je.pairs.joinToString(
      prefix = if (je.pairs.size > 1) "(" else "",
      separator = ", ",
      postfix = if (je.pairs.size > 1) ")" else ""
    ) { (p, _) -> p }

    fun parse(parsePrefix: String, parseSeparator: String, parsePostfix: String): String = je.pairs.joinToString(
      prefix = parsePrefix,
      separator = parseSeparator,
      postfix = parsePostfix
    ) { (p, t) ->
      when {
        t.startsWith("arrow.core.Option") -> "value[\"$p\"].fold({ None.right() }, { ${t.decoder()}.run { decode(it) } })"
        t.endsWith('?') -> "value[\"$p\"].fold({ null.right() }, { ${t.decoder()}.run { decode(it) } })"
        else -> "value[\"$p\"].fold({ Either.Left(KeyNotFound(\"$p\")) }, { ${t.decoder()}.run { decode(it) } })"
      }
    }

    val map: String =
      if (je.pairs.size == 1) "${parse(parsePrefix = "", parseSeparator = ",\n", parsePostfix = "")}.map"
      else "Either.applicative<DecodingError>().map(${parse(
        parsePrefix = "\t",
        parseSeparator = ",\n\t\t",
        parsePostfix = "\n\t"
      )})"

    val createInstance = """
      |$map { $params ->
      |    ${je.name}(${je.pairs.joinToString(", ") { (p, _) -> "$p = $p" }})
      |  }.fix()""".trimMargin()

    return """
      |$extraImports
      |
      |fun ${je.name}.toJson(): Json = ${jsonProperties(je)}
      |
      |fun Json.Companion.to${je.name}(value: Json): Either<DecodingError, ${je.name}> =
      |  $createInstance
      |""".trimMargin()
  }

  private fun genEncoderInstance(je: JsonElement): String =
    """|
       |fun ${je.name}.Companion.encoder() = object : Encoder<${je.name}> {
       |  override fun ${je.name}.encode(): Json = this.toJson()
       |}
       |
       |fun ${je.name}.Companion.decoder() = object : Decoder<${je.name}> {
       |  override fun decode(value: Json): Either<DecodingError, ${je.name}> =
       |    Json.to${je.name}(value)
       |}
       |""".trimMargin()

}
