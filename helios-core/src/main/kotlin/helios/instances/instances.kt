package helios.instances

import arrow.core.*
import arrow.instance
import arrow.instances.BooleanInstances
import helios.core.*
import helios.typeclasses.*

fun Int.Companion.encoder() = object : Encoder<Int> {
    override fun Int.encode(): Json = JsNumber(this)
}

fun Int.Companion.decoder() = object : Decoder<Int> {
    override fun decode(value: Json): Either<DecodingError, Int> =
            value.asJsNumber().flatMap { it.toInt() }.toEither { NumberDecodingError(value) }
}

fun BooleanInstances.encoder() = object : Encoder<Boolean> {
    override fun Boolean.encode(): Json = JsBoolean(this)
}

fun BooleanInstances.decoder() = object : Decoder<Boolean> {
    override fun decode(value: Json): Either<DecodingError, Boolean> =
            value.asJsBoolean().flatMap { it.value.some() }.toEither { BooleanDecodingError(value) }
}

fun String.Companion.encoder() = object : Encoder<String> {
    override fun String.encode(): Json = JsString(this)
}

fun String.Companion.decoder() = object : Decoder<String> {
    override fun decode(value: Json): Either<DecodingError, String> =
            value.asJsString().flatMap { it.value.toString().some() }.toEither { StringDecodingError(value) }
}

@instance(Option::class)
interface OptionEncoderInstance<in A> : Encoder<Option<A>> {

    fun encoderA(): Encoder<A>

    override fun Option<A>.encode(): Json =
            fold({ JsNull }, { encoderA().run { it.encode() } })

}

@instance(Tuple2::class)
interface Tuple2EncoderInstance<A, B> : Encoder<Tuple2<A, B>> {

    fun encoderA(): Encoder<A>

    fun encoderB(): Encoder<B>

    override fun Tuple2<A, B>.encode(): Json = JsArray(
            listOf(
                    encoderA().run { a.encode() },
                    encoderB().run { b.encode() }
            )
    )

}

@instance(Tuple3::class)
interface Tuple3EncoderInstance<A, B, C> : Encoder<Tuple3<A, B, C>> {

    fun encoderA(): Encoder<A>

    fun encoderB(): Encoder<B>

    fun encoderC(): Encoder<C>

    override fun Tuple3<A, B, C>.encode(): Json = JsArray(
            listOf(
                    encoderA().run { a.encode() },
                    encoderB().run { b.encode() },
                    encoderC().run { c.encode() }
            )
    )

}

