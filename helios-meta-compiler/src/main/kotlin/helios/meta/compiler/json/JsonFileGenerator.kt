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

  //TODO FIXME
  inline val String.encoder: String
    get() = when {
      this == "Boolean"                          -> "BooleanInstances.encoder()"
      this.startsWith("kotlin.collections.List") -> "${Regex("kotlin.collections.List<(.*)>$").matchEntire(
        this
      )!!.groupValues[1]}.encoder()"
      else                                       -> "$this.encoder()"
    }

  private fun jsonProperties(je: JsonElement): String =
    je.pairs.map { (p, r) ->
      """
                    |"$p" to ${r.encoder}.run { $p.toJson() }
                """.trimMargin()
    }.joinToString(",", "JsObject(mapOf(", "))")

  //TODO FIXME
  inline val String.decoder: String
    get() = when {
      this == "Boolean"                          -> "BooleanInstances.decoder()"
      this.startsWith("kotlin.collections.List") -> "ListDecoderInstance(${Regex("kotlin.collections.List<(.*)>$").matchEntire(
        this
      )!!.groupValues[1]}.decoder())"
      else                                       -> "$this.decoder()"
    }

  private fun parse(je: JsonElement): String = je.pairs.joinToString(
    prefix = "\n\t",
    separator = ",\n\t",
    postfix = "\n"
  ) { (p, t) -> "value[\"$p\"].fold({Either.Left(KeyNotFound(\"$p\"))}, { ${t.decoder}.run { decode(it) } })" }

  private fun map(je: JsonElement): String = if (je.pairs.size == 1) "${parse(je)}.map("
  else "Either.applicative<DecodingError>().map(${parse(je)}, "

  private fun createInstance(je: JsonElement): String = """
      |${map(je)} { ${je.pairs.joinToString(
    prefix = if (je.pairs.size > 1) "(" else "",
    separator = ",",
    postfix = if (je.pairs.size > 1) ")" else ""
  ) { (p, _) -> p }} ->
      |  ${je.name}(${je.pairs.map { (p, _) -> "$p = $p" }.joinToString(",")})
      |}).fix()
      |""".trimMargin()

  private fun genToJson(je: JsonElement): String =
    """|
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
