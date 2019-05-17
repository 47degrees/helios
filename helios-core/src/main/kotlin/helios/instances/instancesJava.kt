package helios.instances

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.applicative.map2
import arrow.core.extensions.either.monoid.monoid
import arrow.data.extensions.list.foldable.fold
import arrow.data.extensions.list.foldable.foldLeft
import arrow.data.extensions.list.traverse.sequence
import arrow.data.fix
import arrow.typeclasses.Monoid
import helios.core.JsArray
import helios.core.JsObject
import helios.core.Json
import helios.typeclasses.*

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

interface ListDecoderInstance<A> : Decoder<List<A>> {

  fun decoderA(): Decoder<A>

  override fun decode(value: Json): Either<DecodingError, List<A>> =
    value.asJsArray().toList()
      .flatMap { arr ->
        arr.value.map { decoderA().decode(it) }
      }.sequence(Either.applicative()).fix().map { it.fix().toList() }

  companion object {
    operator fun <A> invoke(decoderA: Decoder<A>): Decoder<List<A>> =
      object : ListDecoderInstance<A> {
        override fun decoderA(): Decoder<A> = decoderA
      }
  }

}

interface MapEncoderInstance<A, B> : Encoder<Map<A, B>> {

  fun encoderB(): Encoder<B>

  override fun Map<A, B>.encode(): Json =
    JsObject(this.map { (key, value) -> (key.toString() to encoderB().run { value.encode() }) }.toMap())

  companion object {
    operator fun <A, B> invoke(encoderB: Encoder<B>): Encoder<Map<A, B>> =
      object : MapEncoderInstance<A, B>, Encoder<Map<A, B>> {
        override fun encoderB(): Encoder<B> = encoderB
      }
  }

}

interface MapDecoderInstance<A, B> : Decoder<Map<A, B>> {

  fun decoderA(): Decoder<A>

  fun decoderB(): Decoder<B>

  override fun decode(value: Json): Either<DecodingError, Map<A, B>> =
    value.asJsObject().fold({ ObjectDecodingError(value).left() }, { obj ->
      obj.value.map { (key, value) ->
        val maybeKey: Either<DecodingError, A> =
          Json.parseFromString(key).mapLeft { StringDecodingError(value) }.flatMap { decoderA().decode(it) }
        val maybeValue: Either<DecodingError, B> = decoderB().decode(value)
        maybeKey.map2(maybeValue) { mapOf(it.toPair()) }
      }.reduce { acc, either -> acc.map2(either) { it.a + it.b } }
    })

  companion object {
    operator fun <A, B> invoke(decoderA: Decoder<A>, decoderB: Decoder<B>): Decoder<Map<A, B>> =
      object : MapDecoderInstance<A, B> {
        override fun decoderA(): Decoder<A> = decoderA
        override fun decoderB(): Decoder<B> = decoderB
      }
  }

}
