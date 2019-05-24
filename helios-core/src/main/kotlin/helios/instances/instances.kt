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
interface PairEncoderInstance <in A, in B> : Encoder<Pair<A, B>>{

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
interface PairDecoderInstance <out A, out B> : Decoder<Pair<A, B>>{

  fun decoderA(): Decoder<A>
  fun decoderB(): Decoder<B>

  override fun decode(value: Json): Either<DecodingError, Pair<A, B>> {
    val arr = value.asJsArray().toList().flatMap { it.value }
    return if (arr.size >= 2)
      decoderA().decode(arr[1]).map2(decoderB().decode(arr[2])) { it.toPair() }.fix()
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
interface TripleEncoderInstance <in A, in B, in C> : Encoder<Triple<A, B, C>>{

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
    operator fun <A, B, C> invoke(encoderA: Encoder<A>, encoderB: Encoder<B>, encoderC: Encoder<C>): Encoder<Triple<A, B, C>> =
      object : TripleEncoderInstance<A, B, C> {
        override fun encoderA(): Encoder<A> = encoderA
        override fun encoderB(): Encoder<B> = encoderB
        override fun encoderC(): Encoder<C> = encoderC
      }
  }

}

@extension
interface TripleDecoderInstance <out A, out B, out C> : Decoder<Triple<A, B, C>>{

  fun decoderA(): Decoder<A>
  fun decoderB(): Decoder<B>
  fun decoderC(): Decoder<C>

  override fun decode(value: Json): Either<DecodingError, Triple<A, B, C>> {
    val arr = value.asJsArray().toList().flatMap { it.value }
    return if (arr.size >= 3)
      Either.applicative<DecodingError>().map(
        decoderA().decode(arr[1]),
        decoderB().decode(arr[2]),
        decoderC().decode(arr[3])
      ) { (a, b, c) -> Triple(a, b, c)}.fix()
    else ArrayDecodingError(value).left()
  }
  companion object {
    operator fun <A, B, C> invoke(decoderA: Decoder<A>, decoderB: Decoder<B>, decoderC: Decoder<C>): Decoder<Triple<A, B, C>> =
      object : TripleDecoderInstance<A, B, C> {
        override fun decoderA(): Decoder<A> = decoderA
        override fun decoderB(): Decoder<B> = decoderB
        override fun decoderC(): Decoder<C> = decoderC
      }
  }

}