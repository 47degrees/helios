package helios.meta.compiler.json

import arrow.common.utils.ClassOrPackageDataWrapper
import org.jetbrains.kotlin.serialization.ProtoBuf
import javax.lang.model.element.TypeElement

class JsonAnnotated(
        val classElement: TypeElement,
        val classOrPackageProto: ClassOrPackageDataWrapper)