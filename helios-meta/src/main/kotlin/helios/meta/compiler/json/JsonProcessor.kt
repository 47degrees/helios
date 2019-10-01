package helios.meta.compiler.json

import arrow.common.utils.AbstractProcessor
import arrow.common.utils.knownError
import com.google.auto.service.AutoService
import java.io.File
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class JsonProcessor : AbstractProcessor() {

  private val jsonAnnotatedList: MutableList<JsonAnnotated> = mutableListOf()

  override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

  override fun getSupportedAnnotationTypes(): Set<String> = setOf(jsonAnnotationClass.canonicalName)

  /**
   * Processor entry point
   */
  override fun onProcess(annotations: Set<TypeElement>, roundEnv: RoundEnvironment) {
    jsonAnnotatedList += roundEnv
      .getElementsAnnotatedWith(jsonAnnotationClass)
      .map { element ->
        when (element.kind) {
          ElementKind.CLASS -> processClass(element as TypeElement)
          else              -> knownError("$jsonAnnotationName can only be used on immutable data classes")
        }
      }

    if (roundEnv.processingOver()) {
      val generatedDir =
        File(generatedDir ?: return, jsonAnnotationClass.simpleName).also { it.mkdirs() }
      JsonFileGenerator(generatedDir, jsonAnnotatedList).generate()
    }
  }

  private fun processClass(element: TypeElement): JsonAnnotated {
    val proto = getClassOrPackageDataWrapper(element)
    return JsonAnnotated(element, proto)
  }

}
