package helios.instances

import arrow.core.Either
import arrow.core.Option
import arrow.core.Tuple2
import arrow.instance
import helios.core.*
import helios.typeclasses.Encoder
import helios.typeclasses.encoder

object IntEncoderInstance : Encoder<Int> {
    override fun encode(value: Int): Json = jsNumber(value)
}

object IntEncoderInstanceImplicits {
    fun instance(): IntEncoderInstance = IntEncoderInstance
}

object StringEncoderInstance : Encoder<String> {
    override fun encode(value: String): Json = jsString(value)
}

@instance(Option::class)
interface OptionEncoderInstance<in A> : Encoder<Option<A>> {

    fun encoderA(): Encoder<A>

    override fun encode(value: Option<A>): Json =
            value.fold(::jsNull, { encoderA().encode(it) })
}

@instance(Either::class)
interface EitherEncoderInstance<in A, in B> : Encoder<Either<A, B>> {

    fun encoderA(): Encoder<A>

    fun encoderB(): Encoder<B>

    override fun encode(value: Either<A, B>): Json =
            value.fold(
                    { jsObject(mapOf("left" to encoderA().encode(it))) },
                    { jsObject(mapOf("right" to encoderB().encode(it))) }
            )
}

@instance(Tuple2::class)
interface Tuple2EncoderInstance<A, B> : Encoder<Tuple2<A, B>> {

    override fun encode(value: Tuple2<A, B>): Json = encoder<List<Any?>>().encode(listOf(value.a, value.b))

}