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
      val elementsToGenerate = listOf(genToJson(je), genEncoderInstance(je))
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

  private fun String.keyEncoder(): String = "$this.keyEncoder()"

  private fun String.encoder(): String =
    when {
      this.startsWith("kotlin.collections.List") -> complexEncoder("ListEncoderInstance")
      this.startsWith("kotlin.collections.Map") ->
        "MapEncoderInstance<${getTypeParameters.joinToString()}>(${getTypeParameters.first().keyEncoder()}, ${getTypeParameters.last().encoder()})"
      this.startsWith("java") -> "${this.split('.').last()}EncoderInstance()"
      this.contains('<') -> complexEncoder("${substringBefore('<')}.Companion.encoder")
      this.contains('?') -> "arrow.core.Option<${substringBefore('?')}>".encoder()
      else -> "$this.encoder()"
    }

  private fun jsonProperties(je: JsonElement): String =
    je.pairs.joinToString(",", "JsObject(mapOf(", "))") { (p, r) ->
      """|
         |"$p" to ${r.encoder()}.run { $p.encode() }
         |""".trimMargin()
    }

  private fun String.complexDecoder(pre: String) = getTypeParameters.joinToString(
    prefix = "$pre(",
    postfix = ")"
  ) { it.decoder() }

  private fun String.keyDecoder(): String = "$this.keyDecoder()"

  private fun String.decoder(): String =
    when {
      this.startsWith("kotlin.collections.List") ->
        complexDecoder("ListDecoderInstance")
      this.startsWith("kotlin.collections.Map") ->
        "MapDecoderInstance<${getTypeParameters.joinToString()}>(${getTypeParameters.first().keyDecoder()}, ${getTypeParameters.last().decoder()})"
      this.startsWith("java") -> "${this.split('.').last()}DecoderInstance()"
      this.contains('<') ->
        complexDecoder("${substringBefore('<')}.Companion.decoder")
      this.contains('?') -> "arrow.core.Option<${substringBefore('?')}>".decoder()
      else -> "$this.decoder()"
    }

  private fun parse(je: JsonElement): String = je.pairs.joinToString(
    prefix = "\n\t",
    separator = ",\n\t",
    postfix = "\n"
  ) { (p, t) -> "value[\"$p\"].fold({Either.Left(KeyNotFound(\"$p\"))}, { ${t.decoder()}.run { decode(it) } })" }

  private fun map(je: JsonElement): String = if (je.pairs.size == 1) "${parse(je)}.map("
  else "Either.applicative<DecodingError>().map(${parse(je)}, "

  private fun addExtraImport(je: JsonElement) =
    if (je.pairs.size != 1) "import arrow.core.extensions.either.applicative.applicative" else ""

  private fun createInstance(je: JsonElement): String = """
      |${map(je)} { ${je.pairs.joinToString(
    prefix = if (je.pairs.size > 1) "(" else "",
    separator = ",",
    postfix = if (je.pairs.size > 1) ")" else ""
  ) { (p, _) -> p }} ->
      |  ${je.name}(${je.pairs.joinToString(",") { (p, _) -> "$p = $p" }})
      |}).fix()
      |""".trimMargin()

  private fun genToJson(je: JsonElement): String =
    """|${addExtraImport(je)}
       |
       |fun ${je.name}.toJson(): Json = ${jsonProperties(je)}
       |
       |fun Json.Companion.to${je.name}(value: Json): Either<DecodingError, ${je.name}> =
       |  ${createInstance(je)}
       |""".trimMargin()

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
