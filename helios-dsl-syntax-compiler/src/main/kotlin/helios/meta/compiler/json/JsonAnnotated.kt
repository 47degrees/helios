package helios.meta.compiler.json

import arrow.common.utils.ClassOrPackageDataWrapper
import javax.lang.model.element.TypeElement

class JsonAnnotated(
  val classElement: TypeElement,
  val classOrPackageProto: ClassOrPackageDataWrapper
)