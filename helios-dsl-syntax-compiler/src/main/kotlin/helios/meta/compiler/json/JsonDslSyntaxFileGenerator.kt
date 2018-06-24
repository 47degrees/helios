package helios.meta.compiler.json

import arrow.common.Package
import arrow.common.utils.ClassOrPackageDataWrapper
import arrow.common.utils.fullName
import arrow.common.utils.simpleName
import arrow.optics.Optional
import arrow.optics.Traversal
import me.eugeniomarletti.kotlin.metadata.KotlinClassMetadata
import me.eugeniomarletti.kotlin.metadata.kotlinMetadata
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.ProtoBuf
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.deserialization.NameResolver
import me.eugeniomarletti.kotlin.metadata.shadow.name.FqName
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

private val JsonElement.jsclass: JsonClass? get()=
  (target.classOrPackageProto as? ClassOrPackageDataWrapper.Class)?.run {
    JsonClass(fullName.replace("/","."), simpleName)
  }

private data class JsonClass(val fqName: String, val name: String)
private data class DslContent(val keys: List<Name>,val types: List<JsonClass>)

class JsonDslSyntaxFileGenerator(
        private val generatedDir: File,
        jsonAnnotatedList: List<JsonAnnotated>
) {

    private val Json = "helios.core.Json"
    private val imports = """
      |import helios.optics.select
      |import helios.optics.extract
      |
      |""".trimMargin()

    private val packageSyntax: List<Pair<Package, DslContent>> = jsonAnnotatedList
            .map {  JsonElement(it.classOrPackageProto.`package`, it) }
            .groupBy(JsonElement::`package`)
            .mapValues { (_, v) ->
              DslContent(v.flatMap { element -> element.properties.map { element.nameResolver.getName(it.name) } }.distinct(),
                v.mapNotNull { it.jsclass })
            }.toList()
    /**
     * Main entry point for json dsl syntax generation
     */
    fun generate() = packageSyntax.forEach { (`package`, content) ->
      val (keys, types) = content
        val keyDsl = keys.joinToString(prefix = "package $`package`\n\n$imports", separator = "\n") {
          """
            |inline val $Optional<$Json, $Json>.$it: $Optional<$Json, $Json>
            |    inline get() = select("$it")
            |
            |inline val $Traversal<$Json, $Json>.$it: $Traversal<$Json, $Json>
            |    inline get() = select("$it")
            |""".trimMargin()
        }

        val typeDsl = types.joinToString(separator = "\n") { (fqName, name) ->
          """
          |fun $Optional<$Json, $Json>.to$name(): $Optional<$Json, $fqName>
          |    = extract($fqName.decoder(), $fqName.encoder())
          |
          |fun $Traversal<$Json, $Json>.to$name(): $Traversal<$Json, $fqName>
          |    = extract($fqName.decoder(), $fqName.encoder())
          |""".trimMargin()
        }
        val file = File(generatedDir, "${jsonAnnotationClass.simpleName}.helios.dsl.syntax.$`package`.kt")
        file.writeText("""
          |$keyDsl
          |
          |$typeDsl
        """.trimMargin())
    }

}
