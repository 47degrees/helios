package helios.instances

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.applicative.map2
import arrow.data.extensions.list.foldable.foldLeft
import arrow.data.extensions.list.traverse.sequence
import arrow.data.fix
import arrow.extension
import helios.core.*
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import helios.typeclasses.KeyDecoder
import helios.typeclasses.KeyEncoder

@extension
interface ListEncoderInstance<in A> : Encoder<List<A>> {

  fun encoderA(): Encoder<A>

  override fun List<A>.encode(): Json =
    JsArray(map { encoderA().run { it.encode() } })

  companion object {
    operator fun <A> invoke(encoderA: Encoder<A>): Encoder<List<A>> =
      object : ListEncoderInstance<A> {
        override fun encoderA(): Encoder<A> = encoderA
      }
  }

}

@extension
interface ListDecoderInstance<out A> : Decoder<List<A>> {

  fun decoderA(): Decoder<A>

  override fun decode(value: Json): Either<DecodingError, List<A>> =
    with(decoderA()) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toList() }
    }

  companion object {
    operator fun <A> invoke(decoderA: Decoder<A>): Decoder<List<A>> =
      object : ListDecoderInstance<A> {
        override fun decoderA(): Decoder<A> = decoderA
      }
  }

}

@extension
interface ArrayEncoderInstance<A> : Encoder<Array<A>> {

  fun encoderA(): Encoder<A>

  override fun Array<A>.encode(): Json =
    with(encoderA()) { JsArray(map { it.encode() }) }

  companion object {
    operator fun <A> invoke(encoderA: Encoder<A>): Encoder<Array<A>> =
      object : ArrayEncoderInstance<A> {
        override fun encoderA(): Encoder<A> = encoderA
      }
  }

}

inline fun <reified A> ArrayDecoderInstance(decoderA: Decoder<A>): Decoder<Array<A>> = object : Decoder<Array<A>> {

  override fun decode(value: Json): Either<DecodingError, Array<A>> =
    with(decoderA) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toTypedArray() }
    }

}

@extension
interface DoubleArrayEncoderInstance : Encoder<DoubleArray> {

  override fun DoubleArray.encode(): Json =
    with(Double.encoder()) { JsArray(map { it.encode() }) }

  companion object {
    operator fun invoke(): Encoder<DoubleArray> =
      object : DoubleArrayEncoderInstance {}
  }

}

@extension
interface DoubleArrayDecoderInstance : Decoder<DoubleArray> {

  override fun decode(value: Json): Either<DecodingError, DoubleArray> =
    with(Double.decoder()) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toTypedArray().toDoubleArray() }
    }

  companion object {
    operator fun invoke(): Decoder<DoubleArray> = object : DoubleArrayDecoderInstance {}
  }

}

@extension
interface FloatArrayEncoderInstance : Encoder<FloatArray> {

  override fun FloatArray.encode(): Json =
    with(Float.encoder()) { JsArray(map { it.encode() }) }

  companion object {
    operator fun invoke(): Encoder<FloatArray> = object : FloatArrayEncoderInstance {}
  }

}

@extension
interface FloatArrayDecoderInstance : Decoder<FloatArray> {

  override fun decode(value: Json): Either<DecodingError, FloatArray> =
    with(Float.decoder()) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toTypedArray().toFloatArray() }
    }

  companion object {
    operator fun invoke(): Decoder<FloatArray> = object : FloatArrayDecoderInstance {}
  }

}

@extension
interface LongArrayEncoderInstance : Encoder<LongArray> {

  override fun LongArray.encode(): Json =
    with(Long.encoder()) { JsArray(map { it.encode() }) }

  companion object {
    operator fun invoke(): Encoder<LongArray> = object : LongArrayEncoderInstance {}
  }

}

@extension
interface LongArrayDecoderInstance : Decoder<LongArray> {

  override fun decode(value: Json): Either<DecodingError, LongArray> =
    with(Long.decoder()) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toTypedArray().toLongArray() }
    }

  companion object {
    operator fun invoke(): Decoder<LongArray> = object : LongArrayDecoderInstance {}
  }

}

@extension
interface IntArrayEncoderInstance : Encoder<IntArray> {

  override fun IntArray.encode(): Json =
    with(Int.encoder()) { JsArray(map { it.encode() }) }

  companion object {
    operator fun invoke(): Encoder<IntArray> = object : IntArrayEncoderInstance {}
  }

}

@extension
interface IntArrayDecoderInstance : Decoder<IntArray> {

  override fun decode(value: Json): Either<DecodingError, IntArray> =
    with(Int.decoder()) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toTypedArray().toIntArray() }
    }

  companion object {
    operator fun invoke(): Decoder<IntArray> = object : IntArrayDecoderInstance {}
  }

}

@extension
interface ShortArrayEncoderInstance : Encoder<ShortArray> {

  override fun ShortArray.encode(): Json =
    with(Short.encoder()) { JsArray(map { it.encode() }) }

  companion object {
    operator fun invoke(): Encoder<ShortArray> = object : ShortArrayEncoderInstance {}
  }

}

@extension
interface ShortArrayDecoderInstance : Decoder<ShortArray> {

  override fun decode(value: Json): Either<DecodingError, ShortArray> =
    with(Short.decoder()) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toTypedArray().toShortArray() }
    }

  companion object {
    operator fun invoke(): Decoder<ShortArray> = object : ShortArrayDecoderInstance {}
  }

}

@extension
interface ByteArrayEncoderInstance : Encoder<ByteArray> {

  override fun ByteArray.encode(): Json =
    with(Byte.encoder()) { JsArray(map { it.encode() }) }

  companion object {
    operator fun invoke(): Encoder<ByteArray> = object : ByteArrayEncoderInstance {}
  }

}

@extension
interface ByteArrayDecoderInstance : Decoder<ByteArray> {

  override fun decode(value: Json): Either<DecodingError, ByteArray> =
    with(Byte.decoder()) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toTypedArray().toByteArray() }
    }

  companion object {
    operator fun invoke(): Decoder<ByteArray> = object : ByteArrayDecoderInstance {}
  }

}

@extension
interface BooleanArrayEncoderInstance : Encoder<BooleanArray> {

  override fun BooleanArray.encode(): Json =
    with(Boolean.encoder()) { JsArray(map { it.encode() }) }

  companion object {
    operator fun invoke(): Encoder<BooleanArray> = object : BooleanArrayEncoderInstance {}
  }

}

@extension
interface BooleanArrayDecoderInstance : Decoder<BooleanArray> {

  override fun decode(value: Json): Either<DecodingError, BooleanArray> =
    with(Boolean.decoder()) {
      value.asJsArray().toList()
        .flatMap { (arrValue) ->
          arrValue.map(this::decode)
        }.sequence(Either.applicative()).fix()
        .map { it.fix().toTypedArray().toBooleanArray() }
    }

  companion object {
    operator fun invoke(): Decoder<BooleanArray> = object : BooleanArrayDecoderInstance {}
  }

}

@extension
interface MapEncoderInstance<A, in B> : Encoder<Map<A, B>> {

  fun keyEncoderA(): KeyEncoder<A>
  fun encoderB(): Encoder<B>

  override fun Map<A, B>.encode(): Json =
    with(keyEncoderA()) {
      with(encoderB()) {
        JsObject(map { (key, value) ->
          (key.keyEncode().value.toString() to value.encode())
        }.toMap())
      }
    }

  companion object {
    operator fun <A, B> invoke(keyEncoderA: KeyEncoder<A>, encoderB: Encoder<B>): Encoder<Map<A, B>> =
      object : MapEncoderInstance<A, B>, Encoder<Map<A, B>> {
        override fun keyEncoderA(): KeyEncoder<A> = keyEncoderA
        override fun encoderB(): Encoder<B> = encoderB
      }
  }

}

@extension
interface MapDecoderInstance<A, out B> : Decoder<Map<A, B>> {

  fun keyDecoderA(): KeyDecoder<A>
  fun decoderB(): Decoder<B>

  override fun decode(value: Json): Either<DecodingError, Map<A, B>> =
    with(keyDecoderA()) {
      with(decoderB()) {
        value.asJsObject().fold({ ObjectDecodingError(value).left() }, { (arrValue) ->
          arrValue.map { (key, value) ->
            val maybeKey: Either<DecodingError, A> = keyDecode(JsString(key))
            val maybeValue: Either<DecodingError, B> = decode(value)
            maybeKey.map2(maybeValue) { mapOf(it.toPair()) }
          }
            .foldLeft<Either<DecodingError, Map<A, B>>, Either<DecodingError, Map<A, B>>>(mapOf<A, B>().right()) { acc, either ->
              acc.map2(either) { it.a + it.b }
            }
        })
      }
    }

  companion object {
    operator fun <A, B> invoke(keyDecoderA: KeyDecoder<A>, decoderB: Decoder<B>): Decoder<Map<A, B>> =
      object : MapDecoderInstance<A, B> {
        override fun keyDecoderA(): KeyDecoder<A> = keyDecoderA
        override fun decoderB(): Decoder<B> = decoderB
      }
  }

}
