package helios.instances

import arrow.core.Either
import arrow.core.Option
import arrow.core.Tuple2
import arrow.core.Tuple3
import arrow.instance
import arrow.syntax.option.some
import arrow.syntax.option.toEither
import helios.core.*
import helios.typeclasses.*

object IntEncoderInstance : Encoder<Int> {
    override fun encode(value: Int): Json = JsNumber(value)
}

object IntEncoderInstanceImplicits {
    fun instance(): IntEncoderInstance = IntEncoderInstance
}

object IntDecoderInstance : Decoder<Int> {
    override fun decode(value: Json): Either<DecodingError, Int> =
            value.asJsNumber().flatMap { it.toInt() }.toEither { NumberDecodingError(value) }
}

object IntDecoderInstanceImplicits {
    fun instance(): IntDecoderInstance = IntDecoderInstance
}

object BooleanEncoderInstance : Encoder<Boolean> {
    override fun encode(value: Boolean): Json = JsBoolean(value)
}

object BooleanEncoderInstanceImplicits {
    fun instance(): BooleanEncoderInstance = BooleanEncoderInstance
}

object BooleanDecoderInstance : Decoder<Boolean> {
    override fun decode(value: Json): Either<DecodingError, Boolean> =
            value.asJsBoolean().flatMap { it.value.some() }.toEither { BooleanDecodingError(value) }
}

object BooleanDecoderInstanceImplicits {
    fun instance(): BooleanDecoderInstance = BooleanDecoderInstance
}

object StringEncoderInstance : Encoder<String> {
    override fun encode(value: String): Json = JsString(value)
}

object StringEncoderInstanceImplicits {
    fun instance(): StringEncoderInstance = StringEncoderInstance
}

object StringDecoderInstance : Decoder<String> {
    override fun decode(value: Json): Either<DecodingError, String> =
            value.asJsString().flatMap { it.value.toString().some() }.toEither { StringDecodingError(value) }
}

object StringDecoderInstanceImplicits {
    fun instance(): StringDecoderInstance = StringDecoderInstance
}

@instance(Option::class)
interface OptionEncoderInstance<in A> : Encoder<Option<A>> {

    fun encoderA(): Encoder<A>

    override fun encode(value: Option<A>): Json =
            value.fold({ JsNull }, { encoderA().encode(it) })
}

@instance(Tuple2::class)
interface Tuple2EncoderInstance<A, B> : Encoder<Tuple2<A, B>> {

    fun encoderA(): Encoder<A>

    fun encoderB(): Encoder<B>

    override fun encode(value: Tuple2<A, B>): Json = JsArray(
            listOf(
                    encoderA().encode(value.a),
                    encoderB().encode(value.b)
            )
    )

}

@instance(Tuple3::class)
interface Tuple3EncoderInstance<A, B, C> : Encoder<Tuple3<A, B, C>> {

    fun encoderA(): Encoder<A>

    fun encoderB(): Encoder<B>

    fun encoderC(): Encoder<C>

    override fun encode(value: Tuple3<A, B, C>): Json = JsArray(
            listOf(
                    encoderA().encode(value.a),
                    encoderB().encode(value.b),
                    encoderC().encode(value.c)
            )
    )

}
