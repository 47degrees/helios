package helios.instances

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.applicative.map2
import arrow.extension
import helios.core.*
import helios.typeclasses.*

fun Double.Companion.encoder() = object : Encoder<Double> {
  override fun Double.encode(): Json = JsNumber(this)
}

fun Double.Companion.decoder() = object : Decoder<Double> {
  override fun decode(value: Json): Either<DecodingError, Double> =
    value.asJsNumber().map { it.toDouble() }.toEither { NumberDecodingError(value) }
}

fun Float.Companion.encoder() = object : Encoder<Float> {
  override fun Float.encode(): Json = JsNumber(this)
}

fun Float.Companion.decoder() = object : Decoder<Float> {
  override fun decode(value: Json): Either<DecodingError, Float> =
    value.asJsNumber().map { it.toFloat() }.toEither { NumberDecodingError(value) }
}

fun Long.Companion.encoder() = object : Encoder<Long> {
  override fun Long.encode(): Json = JsNumber(this)
}

fun Long.Companion.decoder() = object : Decoder<Long> {
  override fun decode(value: Json): Either<DecodingError, Long> =
    value.asJsNumber().map { it.toLong() }.toEither { NumberDecodingError(value) }
}

fun Int.Companion.encoder() = object : Encoder<Int> {
  override fun Int.encode(): Json = JsNumber(this)
}

fun Int.Companion.decoder() = object : Decoder<Int> {
  override fun decode(value: Json): Either<DecodingError, Int> =
    value.asJsNumber().flatMap { it.toInt() }.toEither { NumberDecodingError(value) }
}

fun Short.Companion.encoder() = object : Encoder<Short> {
  override fun Short.encode(): Json = JsNumber(this)
}

fun Short.Companion.decoder() = object : Decoder<Short> {
  override fun decode(value: Json): Either<DecodingError, Short> =
    value.asJsNumber().flatMap { it.toShort() }.toEither { NumberDecodingError(value) }
}

fun Byte.Companion.encoder() = object : Encoder<Byte> {
  override fun Byte.encode(): Json = JsNumber(this)
}

fun Byte.Companion.decoder() = object : Decoder<Byte> {
  override fun decode(value: Json): Either<DecodingError, Byte> =
    value.asJsNumber().flatMap { it.toByte() }.toEither { NumberDecodingError(value) }
}

fun Boolean.Companion.encoder() = object : Encoder<Boolean> {
  override fun Boolean.encode(): Json = JsBoolean(this)
}

fun Boolean.Companion.decoder() = object : Decoder<Boolean> {
  override fun decode(value: Json): Either<DecodingError, Boolean> =
    value.asJsBoolean().map { it.value }.toEither { BooleanDecodingError(value) }
}

fun String.Companion.encoder() = object : Encoder<String> {
  override fun String.encode(): Json = JsString(this)
}

fun String.Companion.decoder() = object : Decoder<String> {
  override fun decode(value: Json): Either<DecodingError, String> =
    value.asJsString().map { it.value.toString() }.toEither { StringDecodingError(value) }
}

@extension
interface PairEncoderInstance<in A, in B> : Encoder<Pair<A, B>> {

  fun encoderA(): Encoder<A>
  fun encoderB(): Encoder<B>

  override fun Pair<A, B>.encode(): Json = JsArray(
    listOf(
      encoderA().run { first.encode() },
      encoderB().run { second.encode() }
    )
  )

  companion object {
    operator fun <A, B> invoke(encoderA: Encoder<A>, encoderB: Encoder<B>): Encoder<Pair<A, B>> =
      object : PairEncoderInstance<A, B> {
        override fun encoderA(): Encoder<A> = encoderA
        override fun encoderB(): Encoder<B> = encoderB
      }
  }

}

@extension
interface PairDecoderInstance<out A, out B> : Decoder<Pair<A, B>> {

  fun decoderA(): Decoder<A>
  fun decoderB(): Decoder<B>

  override fun decode(value: Json): Either<DecodingError, Pair<A, B>> {
    val arr = value.asJsArray().toList().flatMap { it.value }
    return if (arr.size == 2)
      decoderA().decode(arr.first()).map2(decoderB().decode(arr.last())) { it.toPair() }.fix()
    else ArrayDecodingError(value).left()
  }

  companion object {
    operator fun <A, B> invoke(decoderA: Decoder<A>, decoderB: Decoder<B>): Decoder<Pair<A, B>> =
      object : PairDecoderInstance<A, B> {
        override fun decoderA(): Decoder<A> = decoderA
        override fun decoderB(): Decoder<B> = decoderB
      }
  }

}

@extension
interface TripleEncoderInstance<in A, in B, in C> : Encoder<Triple<A, B, C>> {

  fun encoderA(): Encoder<A>
  fun encoderB(): Encoder<B>
  fun encoderC(): Encoder<C>

  override fun Triple<A, B, C>.encode(): Json = JsArray(
    listOf(
      encoderA().run { first.encode() },
      encoderB().run { second.encode() },
      encoderC().run { third.encode() }
    )
  )

  companion object {
    operator fun <A, B, C> invoke(
      encoderA: Encoder<A>,
      encoderB: Encoder<B>,
      encoderC: Encoder<C>
    ): Encoder<Triple<A, B, C>> =
      object : TripleEncoderInstance<A, B, C> {
        override fun encoderA(): Encoder<A> = encoderA
        override fun encoderB(): Encoder<B> = encoderB
        override fun encoderC(): Encoder<C> = encoderC
      }
  }

}

@extension
interface TripleDecoderInstance<out A, out B, out C> : Decoder<Triple<A, B, C>> {

  fun decoderA(): Decoder<A>
  fun decoderB(): Decoder<B>
  fun decoderC(): Decoder<C>

  override fun decode(value: Json): Either<DecodingError, Triple<A, B, C>> {
    val arr = value.asJsArray().toList().flatMap { it.value }
    return if (arr.size == 3)
      Either.applicative<DecodingError>().map(
        decoderA().decode(arr[0]),
        decoderB().decode(arr[1]),
        decoderC().decode(arr[2])
      ) { (a, b, c) -> Triple(a, b, c) }.fix()
    else ArrayDecodingError(value).left()
  }

  companion object {
    operator fun <A, B, C> invoke(
      decoderA: Decoder<A>,
      decoderB: Decoder<B>,
      decoderC: Decoder<C>
    ): Decoder<Triple<A, B, C>> =
      object : TripleDecoderInstance<A, B, C> {
        override fun decoderA(): Decoder<A> = decoderA
        override fun decoderB(): Decoder<B> = decoderB
        override fun decoderC(): Decoder<C> = decoderC
      }
  }

}

@extension
interface NullableEncoderInstance<in A> : Encoder<A?> {

  fun encoderA(): Encoder<A>

  override fun A?.encode(): Json {
    val maybeThis = toOption()
    return Option.encoder(encoderA()).run { maybeThis.encode() }
  }

  companion object {
    operator fun <A> invoke(
      encoderA: Encoder<A>
    ): Encoder<A?> =
      object : NullableEncoderInstance<A> {
        override fun encoderA(): Encoder<A> = encoderA
      }
  }

}

@extension
interface NullableDecoderInstance<out A> : Decoder<A?> {

  fun decoderA(): Decoder<A>

  override fun decode(value: Json): Either<DecodingError, A?> =
    Option.decoder(decoderA()).decode(value).map(Option<A>::orNull)

  companion object {
    operator fun <A> invoke(
      decoderA: Decoder<A>
    ): Decoder<A?> =
      object : NullableDecoderInstance<A> {
        override fun decoderA(): Decoder<A> = decoderA
      }
  }

}