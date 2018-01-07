package helios.meta.compiler.json

import arrow.common.Package
import arrow.common.utils.ClassOrPackageDataWrapper
import arrow.common.utils.typeConstraints
import org.jetbrains.kotlin.serialization.ProtoBuf
import java.io.File

data class JsonElement(
        val `package`: Package,
        val target: JsonAnnotated
) {
    val properties: List<ProtoBuf.Property> = (target.classOrPackageProto as ClassOrPackageDataWrapper.Class).propertyList
    val tparams: List<ProtoBuf.TypeParameter> = target.classOrPackageProto.typeParameters
    val name: String = target.classElement.simpleName.toString()
    val typeArgs: List<String> = tparams.map { target.classOrPackageProto.nameResolver.getString(it.name) }
    val expandedTypeArgs: String = target.classOrPackageProto.typeParameters.joinToString(
            separator = ", ", transform = { target.classOrPackageProto.nameResolver.getString(it.name) })
    val typeConstraints = target.classOrPackageProto.typeConstraints()

}

class JsonFileGenerator(
        private val generatedDir: File,
        jsonAnnotatedList: List<JsonAnnotated>
) {

    private val jsonElements: List<JsonElement> = jsonAnnotatedList.map { JsonElement(it.classOrPackageProto.`package`, it) }

    /**
     * Main entry point for higher kinds extension generation
     */
    fun generate() {
        jsonElements.forEachIndexed { _, je ->
            val elementsToGenerate = listOf(genToJson(je), genEncoderInstance(je))
            val source: String = elementsToGenerate.joinToString(prefix = listOf(
                    "package ${je.`package`}",
                    "import arrow.*",
                    "import helios.core.*",
                    "import helios.typeclasses.*",
                    "import helios.syntax.json.*"
            ).joinToString("\n"), separator = "\n", postfix = "\n")
            val file = File(generatedDir, jsonAnnotationClass.simpleName + ".${je.target.classElement.qualifiedName}.kt")
            file.writeText(source)
        }
    }

    private fun jsonProperties(je: JsonElement): String =
            je.properties.map {
                val pname = je.target.classOrPackageProto.nameResolver.getString(it.name)
                """
                    |"$pname" to this.$pname.toJson()
                """.trimMargin()
            }.joinToString(",", "jsObject(mapOf(", "))")

    private fun genToJson(je: JsonElement): String =
            """|
               |fun ${je.name}.toJson(): Json = ${jsonProperties(je)}
               |""".trimMargin()

    private fun genEncoderInstance(je: JsonElement): String =
            """|
               |interface ${je.name}EncoderInstance: Encoder<${je.name}> {
               |  override fun encode(value: ${je.name}): Json = value.toJson()
               |}
               |
               |object ${je.name}EncoderInstanceImplicits {
               |  fun instance(): ${je.name}EncoderInstance = object : ${je.name}EncoderInstance {}
               |}
               |""".trimMargin()

}
