package java_util

import helios.core.Json
import helios.core.jsArray
import helios.core.jsObject
import helios.core.jsString
import helios.typeclasses.Encoder

interface ListEncoderInstance<in A> : Encoder<List<A>> {

    fun encoderA(): Encoder<A>

    override fun encode(value: List<A>): Json =
            jsArray(value.map { encoderA().encode(it) })
}

object ListEncoderInstanceImplicits {
    fun <A> instance(encoderA: Encoder<A>): ListEncoderInstance<A> = object : ListEncoderInstance<A> {
        override fun encoderA(): Encoder<A> = encoderA
    }
}

interface MapEncoderInstance : Encoder<Map<String, String>> {

    override fun encode(value: Map<String, String>): Json =
            jsObject(value.map { it.key to jsString(it.value) }.toMap())
}

object MapEncoderInstanceImplicits {
    fun instance(): MapEncoderInstance = object : MapEncoderInstance {}
}