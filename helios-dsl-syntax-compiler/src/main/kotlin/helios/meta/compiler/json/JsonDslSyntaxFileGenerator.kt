package helios.meta.compiler.json

import arrow.common.Package
import arrow.common.utils.ClassOrPackageDataWrapper
import arrow.common.utils.simpleName
import arrow.optics.Traversal
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.ProtoBuf
import me.eugeniomarletti.kotlin.metadata.shadow.metadata.deserialization.NameResolver
import me.eugeniomarletti.kotlin.metadata.shadow.name.Name
import me.eugeniomarletti.kotlin.metadata.shadow.serialization.deserialization.getName
import java.io.File

data class JsonElement(
  val `package`: Package,
  val target: JsonAnnotated
) {
  val properties: List<ProtoBuf.Property> =
    (target.classOrPackageProto as ClassOrPackageDataWrapper.Class).propertyList
  val nameResolver: NameResolver
    inline get() = target.classOrPackageProto.nameResolver
}

private val JsonElement.typeName: String?
  get() =
    (target.classOrPackageProto as? ClassOrPackageDataWrapper.Class)?.simpleName

private data class DslContent(val keys: List<Name>, val typeNames: List<String>)

class JsonDslSyntaxFileGenerator(
  private val generatedDir: File,
  jsonAnnotatedList: List<JsonAnnotated>
) {

  private val Json = "Json"
  private val Optional = "Optional"
  private val Travesal = "Travesal"
  private val imports = """
      |import arrow.optics.Optional
      |import arrow.optics.Traversal
      |import helios.core.Json
      |import helios.optics.select
      |import helios.optics.extract
      |""".trimMargin()

  private val packageSyntax: List<Pair<Package, DslContent>> = jsonAnnotatedList
    .map { JsonElement(it.classOrPackageProto.`package`, it) }
    .groupBy(JsonElement::`package`)
    .mapValues { (_, v) ->
      DslContent(v.flatMap { element -> element.properties.map { element.nameResolver.getName(it.name) } }.distinct(),
        v.mapNotNull { it.typeName })
    }.toList()

  /**
   * Main entry point for json dsl syntax generation
   */
  fun generate() = packageSyntax.forEach { (`package`, data) ->
    val keys = data.keys.joinToString(separator = "\n") {
      """
          |/**
          |  * Select $it key in [JsObject].
          |  */
          |inline val $Optional<$Json, $Json>.$it: $Optional<$Json, $Json>
          |    inline get() = select("$it")
          |
          |/**
          |  * Select $it key in [JsObject].
          |  */
          |inline val $Traversal<$Json, $Json>.$it: $Traversal<$Json, $Json>
          |    inline get() = select("$it")
          |""".trimMargin()
    }

    val types = data.typeNames.joinToString(separator = "\n") { name ->
      """
          |/**
          |  * Extract [$name] from [Json.Companion.path]
          |  */
          |fun $Optional<$Json, $Json>.to$name(): $Optional<$Json, $name>
          |    = extract($name.decoder(), $name.encoder())
          |
          |/**
          |  * Extract [$name] from [Json.Companion.path]
          |  */
          |fun $Traversal<$Json, $Json>.to$name(): $Traversal<$Json, $name>
          |    = extract($name.decoder(), $name.encoder())
          |""".trimMargin()
    }

    val file =
      File(generatedDir, "${jsonAnnotationClass.simpleName}.helios.dsl.syntax.$`package`.kt")
    file.writeText(
      """
        |package $`package`
        |
        |$imports
        |$keys
        |$types
      """.trimMargin()
    )
  }

}
