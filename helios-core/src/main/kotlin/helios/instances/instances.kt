package helios.instances

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.apply.map2
import arrow.extension
import helios.core.*
import helios.syntax.json.asJsArrayOrError
import helios.syntax.json.asJsNumberOrError
import helios.syntax.json.asJsStringOrError
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder

val doubleEncoder: Encoder<Double> = object : Encoder<Double> {
  override fun Double.encode(): Json = JsNumber(this)
}

fun Double.Companion.encoder(): Encoder<Double> = doubleEncoder

val doubleDecoder: Decoder<Double> = object : Decoder<Double> {
  override fun decode(value: Json): Either<DecodingError, Double> =
    value.asJsNumberOrError(JsNumberDecodingError.JsDoubleError(value)) {
      Try(it::toDouble).toEither { JsNumberDecodingError.JsDoubleError(value) }
    }
}

fun Double.Companion.decoder(): Decoder<Double> = doubleDecoder

val floatEncoder: Encoder<Float> = object : Encoder<Float> {
  override fun Float.encode(): Json = JsNumber(this)
}

fun Float.Companion.encoder(): Encoder<Float> = floatEncoder

val floatDecoder: Decoder<Float> = object : Decoder<Float> {
  override fun decode(value: Json): Either<DecodingError, Float> =
    value.asJsNumberOrError(JsNumberDecodingError.JsFloatError(value)) {
      Try(it::toFloat).toEither { JsNumberDecodingError.JsFloatError(value) }
    }
}

fun Float.Companion.decoder(): Decoder<Float> = floatDecoder

val longEncoder: Encoder<Long> = object : Encoder<Long> {
  override fun Long.encode(): Json = JsNumber(this)
}

fun Long.Companion.encoder(): Encoder<Long> = longEncoder

val longDecoder: Decoder<Long> = object : Decoder<Long> {
  override fun decode(value: Json): Either<DecodingError, Long> =
    value.asJsNumberOrError(JsNumberDecodingError.JsLongError(value)) {
      Try(it::toLong).toEither { JsNumberDecodingError.JsLongError(value) }
    }
}

fun Long.Companion.decoder(): Decoder<Long> = longDecoder

val intEncoder: Encoder<Int> = object : Encoder<Int> {
  override fun Int.encode(): Json = JsNumber(this)
}

fun Int.Companion.encoder(): Encoder<Int> = intEncoder

val intDecoder: Decoder<Int> = object : Decoder<Int> {
  override fun decode(value: Json): Either<DecodingError, Int> =
    value.asJsNumberOrError(JsNumberDecodingError.JsIntError(value)) {
      it.toInt().toEither { JsNumberDecodingError.JsIntError(value) }
    }
}

fun Int.Companion.decoder(): Decoder<Int> = intDecoder

val shortEncoder: Encoder<Short> = object : Encoder<Short> {
  override fun Short.encode(): Json = JsNumber(this)
}

fun Short.Companion.encoder(): Encoder<Short> = shortEncoder

val shortDecoder: Decoder<Short> = object : Decoder<Short> {
  override fun decode(value: Json): Either<DecodingError, Short> =
    value.asJsNumberOrError(JsNumberDecodingError.JsShortError(value)) {
      it.toShort().toEither { JsNumberDecodingError.JsShortError(value) }
    }
}

fun Short.Companion.decoder(): Decoder<Short> = shortDecoder

val byteEncoder: Encoder<Byte> = object : Encoder<Byte> {
  override fun Byte.encode(): Json = JsNumber(this)
}

fun Byte.Companion.encoder(): Encoder<Byte> = byteEncoder

val byteDecoder: Decoder<Byte> = object : Decoder<Byte> {
  override fun decode(value: Json): Either<DecodingError, Byte> =
    value.asJsNumberOrError(JsNumberDecodingError.JsByteError(value)) {
      it.toByte().toEither { JsNumberDecodingError.JsByteError(value) }
    }
}

fun Byte.Companion.decoder(): Decoder<Byte> = byteDecoder

val booleanEncoder: Encoder<Boolean> = object : Encoder<Boolean> {
  override fun Boolean.encode(): Json = JsBoolean(this)
}

fun Boolean.Companion.encoder(): Encoder<Boolean> = booleanEncoder

val booleanDecoder: Decoder<Boolean> = object : Decoder<Boolean> {
  override fun decode(value: Json): Either<DecodingError, Boolean> =
    value.asJsBoolean().map(JsBoolean::value).toEither { JsBooleanDecodingError(value) }
}

fun Boolean.Companion.decoder(): Decoder<Boolean> = booleanDecoder

val stringEncoder: Encoder<String> = object : Encoder<String> {
  override fun String.encode(): Json = JsString(this)
}

fun String.Companion.encoder(): Encoder<String> = stringEncoder

val stringDecoder: Decoder<String> = object : Decoder<String> {
  override fun decode(value: Json): Either<DecodingError, String> =
    value.asJsString().map { it.value.toString() }.toEither { JsStringDecodingError(value) }
}

fun String.Companion.decoder(): Decoder<String> = stringDecoder

@extension
interface PairEncoder<in A, in B> : Encoder<Pair<A, B>> {

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
      object : PairEncoder<A, B> {
        override fun encoderA(): Encoder<A> = encoderA
        override fun encoderB(): Encoder<B> = encoderB
      }
  }

}

@extension
interface PairDecoder<out A, out B> : Decoder<Pair<A, B>> {

  fun decoderA(): Decoder<A>
  fun decoderB(): Decoder<B>

  override fun decode(value: Json): Either<DecodingError, Pair<A, B>> =
    value.asJsArrayOrError { (arr) ->
      if (arr.size == 2)
        decoderA().decode(arr.first()).map2(decoderB().decode(arr.last())) { it.toPair() }.fix()
      else JsArrayDecodingError(value).left()
    }

  companion object {
    operator fun <A, B> invoke(decoderA: Decoder<A>, decoderB: Decoder<B>): Decoder<Pair<A, B>> =
      object : PairDecoder<A, B> {
        override fun decoderA(): Decoder<A> = decoderA
        override fun decoderB(): Decoder<B> = decoderB
      }
  }

}

@extension
interface TripleEncoder<in A, in B, in C> : Encoder<Triple<A, B, C>> {

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
      object : TripleEncoder<A, B, C> {
        override fun encoderA(): Encoder<A> = encoderA
        override fun encoderB(): Encoder<B> = encoderB
        override fun encoderC(): Encoder<C> = encoderC
      }
  }

}

@extension
interface TripleDecoder<out A, out B, out C> : Decoder<Triple<A, B, C>> {

  fun decoderA(): Decoder<A>
  fun decoderB(): Decoder<B>
  fun decoderC(): Decoder<C>

  override fun decode(value: Json): Either<DecodingError, Triple<A, B, C>> =
    value.asJsArrayOrError { (arr) ->
      if (arr.size == 3)
        Either.applicative<DecodingError>().map(
          decoderA().decode(arr[0]),
          decoderB().decode(arr[1]),
          decoderC().decode(arr[2])
        ) { (a, b, c) -> Triple(a, b, c) }.fix()
      else JsArrayDecodingError(value).left()
    }

  companion object {
    operator fun <A, B, C> invoke(
      decoderA: Decoder<A>,
      decoderB: Decoder<B>,
      decoderC: Decoder<C>
    ): Decoder<Triple<A, B, C>> =
      object : TripleDecoder<A, B, C> {
        override fun decoderA(): Decoder<A> = decoderA
        override fun decoderB(): Decoder<B> = decoderB
        override fun decoderC(): Decoder<C> = decoderC
      }
  }

}

@extension
interface NullableEncoder<in A> : Encoder<A?> {

  fun encoderA(): Encoder<A>

  override fun A?.encode(): Json =
    this?.let { a -> encoderA().let { a.encode() } } ?: JsNull

  companion object {
    operator fun <A> invoke(encoderA: Encoder<A>): Encoder<A?> =
      object : NullableEncoder<A> {
        override fun encoderA(): Encoder<A> = encoderA
      }
  }

}

@extension
interface NullableDecoder<out A> : Decoder<A?> {

  fun decoderA(): Decoder<A>

  override fun decode(value: Json): Either<DecodingError, A?> =
    if (value.isNull) null.right() else decoderA().decode(value)

  companion object {
    operator fun <A> invoke(decoderA: Decoder<A>): Decoder<A?> =
      object : NullableDecoder<A> {
        override fun decoderA(): Decoder<A> = decoderA
      }
  }

}

fun <E : Enum<E>> Enum.Companion.encoder(): Encoder<Enum<E>> = object : Encoder<Enum<E>> {
  override fun Enum<E>.encode(): Json = JsString(name)
}

inline fun <reified E : Enum<E>> Enum.Companion.decoder(): Decoder<E> = object : Decoder<E> {

  override fun decode(value: Json): Either<DecodingError, E> =
    value.asJsStringOrError {
      Try {
        java.lang.Enum.valueOf(E::class.java, it.value.toString())
      }.toEither { EnumValueNotFound(value) }
    }

}

