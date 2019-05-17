package helios.instances

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.applicative.map2
import arrow.extension
import helios.core.*
import helios.typeclasses.*

fun Int.Companion.encoder() = object : Encoder<Int> {
  override fun Int.encode(): Json = JsNumber(this)
}

fun Int.Companion.decoder() = object : Decoder<Int> {
  override fun decode(value: Json): Either<DecodingError, Int> =
    value.asJsNumber().flatMap { it.toInt() }.toEither { NumberDecodingError(value) }
}

fun Boolean.Companion.encoder() = object : Encoder<Boolean> {
  override fun Boolean.encode(): Json = JsBoolean(this)
}

fun Boolean.Companion.decoder() = object : Decoder<Boolean> {
  override fun decode(value: Json): Either<DecodingError, Boolean> =
    value.asJsBoolean().flatMap { it.value.some() }.toEither { BooleanDecodingError(value) }
}

fun String.Companion.encoder() = object : Encoder<String> {
  override fun String.encode(): Json = JsString(this)
}

fun String.Companion.decoder() = object : Decoder<String> {
  override fun decode(value: Json): Either<DecodingError, String> =
    value.asJsString().flatMap {
      it.value.toString().some()
    }.toEither { StringDecodingError(value) }
}

@extension
interface OptionEncoderInstance<in A> : Encoder<Option<A>> {

  fun encoderA(): Encoder<A>

  override fun Option<A>.encode(): Json =
    fold({ JsNull }, { encoderA().run { it.encode() } })

}

@extension
interface OptionDecoderInstance<out A> : Decoder<Option<A>> {

  fun decoderA(): Decoder<A>

  override fun decode(value: Json): Either<DecodingError, Option<A>> =
    if (value.isNull) None.right() else decoderA().decode(value).map { Some(it) }

}

@extension
interface EitherEncoderInstance<in A, in B> : Encoder<Either<A, B>> {

  fun encoderA(): Encoder<A>

  fun encoderB(): Encoder<B>

  override fun Either<A, B>.encode(): Json =
    fold({ encoderA().run { it.encode() } },
      { encoderB().run { it.encode() } })

}

@extension
interface EitherDecoderInstance<out A, out B> : Decoder<Either<A, B>> {

  fun decoderA(): Decoder<A>

  fun decoderB(): Decoder<B>

  override fun decode(value: Json): Either<DecodingError, Either<A, B>> =
    decoderB().decode(value).fold({ decoderA().decode(value).map { it.left() } },
      { v -> v.right().map { it.right() } })

}

@extension
interface Tuple2EncoderInstance<in A, in B> : Encoder<Tuple2<A, B>> {

  fun encoderA(): Encoder<A>

  fun encoderB(): Encoder<B>

  override fun Tuple2<A, B>.encode(): Json = JsArray(
    listOf(
      encoderA().run { a.encode() },
      encoderB().run { b.encode() }
    )
  )

}

@extension
interface Tuple2DecoderInstance<out A, out B> : Decoder<Tuple2<A, B>> {

  fun decoderA(): Decoder<A>

  fun decoderB(): Decoder<B>

  override fun decode(value: Json): Either<DecodingError, Tuple2<A, B>> {
    val arr = value.asJsArray().toList().flatMap { it.value }
    return if (arr.size >= 2)
      decoderA().decode(arr[1]).map2(decoderB().decode(arr[2])) { it }.fix()
    else ArrayDecodingError(value).left()
  }

}

@extension
interface Tuple3EncoderInstance<in A, in B, in C> : Encoder<Tuple3<A, B, C>> {

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

@extension
interface Tuple3DecoderInstance<out A, out B, out C> : Decoder<Tuple3<A, B, C>> {

  fun decoderA(): Decoder<A>

  fun decoderB(): Decoder<B>

  fun decoderC(): Decoder<C>

  override fun decode(value: Json): Either<DecodingError, Tuple3<A, B, C>> {
    val arr = value.asJsArray().toList().flatMap { it.value }
    return if (arr.size >= 3)
      Either.applicative<DecodingError>().map(decoderA().decode(arr[1]), decoderB().decode(arr[2]), decoderC().decode(arr[3])) { it }.fix()
    else ArrayDecodingError(value).left()
  }

}

