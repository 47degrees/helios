package helios.meta.compiler.json

import arrow.common.Package
import arrow.common.utils.ClassOrPackageDataWrapper
import arrow.common.utils.extractFullName
import arrow.common.utils.removeBackticks
import org.jetbrains.kotlin.serialization.ProtoBuf
import java.io.File

data class JsonElement(
        val `package`: Package,
        val target: JsonAnnotated
) {
    val properties: List<ProtoBuf.Property> = (target.classOrPackageProto as ClassOrPackageDataWrapper.Class).propertyList
    val tparams: List<ProtoBuf.TypeParameter> = target.classOrPackageProto.typeParameters
    val name: String = target.classElement.simpleName.toString()
    val pairs: List<Pair<String, String>> = properties.map {
        val retType = it.returnType.extractFullName(target.classOrPackageProto as ClassOrPackageDataWrapper.Class, true, false)
        val pname = target.classOrPackageProto.nameResolver.getString(it.name)
        pname to retType.removeBackticks()
    }
}

class JsonFileGenerator(
        private val generatedDir: File,
        jsonAnnotatedList: List<JsonAnnotated>
) {

    private val jsonElements: List<JsonElement> = jsonAnnotatedList.map { JsonElement(it.classOrPackageProto.`package`, it) }

    /**
     * Main entry point for json extension generation
     */
    fun generate() {
        jsonElements.forEachIndexed { _, je ->
            val elementsToGenerate = listOf(genToJson(je), genEncoderInstance(je))
            val source: String = elementsToGenerate.joinToString(prefix = listOf(
                    "package ${je.`package`}",
                    "",
                    "import arrow.*",
                    "import arrow.core.*",
                    "import arrow.syntax.applicative.map",
                    "import helios.core.*",
                    "import helios.typeclasses.*",
                    "import helios.syntax.json.*",
                    ""
            ).joinToString("\n"), separator = "\n", postfix = "\n")
            val file = File(generatedDir, jsonAnnotationClass.simpleName + ".${je.target.classElement.qualifiedName}.kt")
            file.writeText(source)
        }
    }

    private fun jsonProperties(je: JsonElement): String =
            je.pairs.map { (p, _) ->
                """
                    |"$p" to this.$p.toJson()
                """.trimMargin()
            }.joinToString(",", "JsObject(mapOf(", "))")

    private fun createInstance(je: JsonElement): String = """|Either.applicative<DecodingError>().map(${je.pairs.map { (p, t) -> "value[\"$p\"].fold({Either.Left(KeyNotFound(\"$p\"))}, { decoder<$t>().decode(it)})" }.joinToString(prefix = "\n\t", separator = ",\n\t", postfix = "\n")}, { ${je.pairs.map { (p, _) -> p }.joinToString(prefix = if (je.pairs.size > 1) "(" else "", separator = ",", postfix = if (je.pairs.size > 1) ")" else "")} ->
            |  ${je.name}(${je.pairs.map { (p, _) -> "$p = $p" }.joinToString(",")})
            |}).ev()
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
               |interface ${je.name}EncoderInstance: Encoder<${je.name}> {
               |  override fun encode(value: ${je.name}): Json = value.toJson()
               |}
               |
               |interface ${je.name}DecoderInstance: Decoder<${je.name}> {
               |  override fun decode(value: Json): Either<DecodingError, ${je.name}> =
               |    Json.to${je.name}(value)
               |}
               |
               |object ${je.name}EncoderInstanceImplicits {
               |  fun instance(): ${je.name}EncoderInstance =
               |    object : ${je.name}EncoderInstance {}
               |}
               |
               |object ${je.name}DecoderInstanceImplicits {
               |  fun instance(): ${je.name}DecoderInstance =
               |    object : ${je.name}DecoderInstance {}
               |}
               |""".trimMargin()

}
