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
import kotlin.collections.mapOf


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
    value.asJsArray().toList()
      .flatMap { (arrValue) ->
        arrValue.map(decoderA()::decode)
      }.sequence(Either.applicative()).fix().map { it.fix().toList() }

  companion object {
    operator fun <A> invoke(decoderA: Decoder<A>): Decoder<List<A>> =
      object : ListDecoderInstance<A> {
        override fun decoderA(): Decoder<A> = decoderA
      }
  }

}

@extension
interface MapEncoderInstance<A, in B> : Encoder<Map<A, B>> {

  fun keyEncoderA(): KeyEncoder<A>
  fun encoderB(): Encoder<B>

  override fun Map<A, B>.encode(): Json =
    JsObject(this.map { (key, value) -> (keyEncoderA().run { key.keyEncode() } to encoderB().run { value.encode() }) }.toMap())

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
    value.asJsObject().fold({ ObjectDecodingError(value).left() }, { (objValue) ->
      objValue.map { (key, value) ->
        val maybeKey: Either<DecodingError, A> =
          Json.parseFromString(key).mapLeft { StringDecodingError(value) }.flatMap(keyDecoderA()::keyDecode)
        val maybeValue: Either<DecodingError, B> = decoderB().decode(value)
        maybeKey.map2(maybeValue) { mapOf(it.toPair()) }
      }
        .foldLeft<Either<DecodingError, Map<A, B>>, Either<DecodingError, Map<A, B>>>(arrow.core.mapOf<A, B>().right()) { acc, either ->
          acc.map2(either) { it.a + it.b }
        }
    })

  companion object {
    operator fun <A, B> invoke(keyDecoderA: KeyDecoder<A>, decoderB: Decoder<B>): Decoder<Map<A, B>> =
      object : MapDecoderInstance<A, B> {
        override fun keyDecoderA(): KeyDecoder<A> = keyDecoderA
        override fun decoderB(): Decoder<B> = decoderB
      }
  }

}
