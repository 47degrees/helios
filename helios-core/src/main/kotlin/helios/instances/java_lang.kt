package java_lang

import helios.instances.IntEncoderInstance
import helios.instances.StringEncoderInstance

object IntegerEncoderInstanceImplicits {
    fun instance(): IntEncoderInstance = IntEncoderInstance
}

object StringEncoderInstanceImplicits {
    fun instance(): StringEncoderInstance = StringEncoderInstance
}