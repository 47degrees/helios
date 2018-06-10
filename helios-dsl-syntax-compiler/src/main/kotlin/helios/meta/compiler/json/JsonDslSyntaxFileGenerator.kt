package helios.meta.compiler.json

import arrow.common.Package
import arrow.common.utils.ClassOrPackageDataWrapper
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.ProtoBuf
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.deserialization.NameResolver
import me.eugeniomarletti.kotlin.metadata.shadow.name.Name
import me.eugeniomarletti.kotlin.metadata.shadow.serialization.deserialization.getName
import java.io.File

data class JsonElement(
        val `package`: Package,
        val target: JsonAnnotated
) {
    val properties: List<ProtoBuf.Property> = (target.classOrPackageProto as ClassOrPackageDataWrapper.Class).propertyList
    val nameResolver: NameResolver
            inline get() = target.classOrPackageProto.nameResolver
}

class JsonDslSyntaxFileGenerator(
        private val generatedDir: File,
        jsonAnnotatedList: List<JsonAnnotated>
) {

    private val packageSyntax: List<Pair<Package, List<Name>>> = jsonAnnotatedList
            .map { JsonElement(it.classOrPackageProto.`package`, it) }
            .groupBy(JsonElement::`package`)
            .mapValues { (_, v) -> v.flatMap { element -> element.properties.map { element.nameResolver.getName(it.name) } }.distinct() }
            .toList()

    /**
     * Main entry point for json dsl syntax generation
     */
    fun generate() = packageSyntax.forEach { (`package`, names) ->
        val content = names.joinToString(prefix = "package $`package`\n\n", separator = "\n") {
            """
            |val helios.optics.JsonPath.$it
            |    inline get() = select("$it")
            |
            |val helios.optics.JsonTraversalPath.$it
            |    inline get() = select("$it")
            |""".trimMargin()
        }
        val file = File(generatedDir, "${jsonAnnotationClass.simpleName}.helios.dsl.syntax.$`package`.kt")
        file.writeText(content)
    }

}
