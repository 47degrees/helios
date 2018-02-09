package java_util

import arrow.core.Either
import arrow.core.applicative
import arrow.core.identity
import arrow.core.reify
import arrow.data.k
import helios.core.*
import helios.typeclasses.Decoder
import helios.typeclasses.DecodingError
import helios.typeclasses.Encoder

interface ListEncoderInstance<in A> : Encoder<List<A>> {

    fun encoderA(): Encoder<A>

    override fun encode(value: List<A>): Json =
            JsArray(value.map { encoderA().encode(it) })
}

object ListEncoderInstanceImplicits {
    fun <A> instance(encoderA: Encoder<A>): ListEncoderInstance<A> = object : ListEncoderInstance<A> {
        override fun encoderA(): Encoder<A> = encoderA
    }
}

interface ListDecoderInstance<A> : Decoder<List<A>> {

    fun decoderA(): Decoder<A>

    override fun decode(value: Json): Either<DecodingError, List<A>> =
            value.asJsArray().toList()
                    .flatMap {
                        it.value.map { decoderA().decode(it) }
                    }.k().traverse(::identity, Either.applicative()).reify().map { it.list }
}

object ListDecoderInstanceImplicits {
    fun <A> instance(decoderA: Decoder<A>): ListDecoderInstance<A> = object : ListDecoderInstance<A> {
        override fun decoderA(): Decoder<A> = decoderA
    }
}