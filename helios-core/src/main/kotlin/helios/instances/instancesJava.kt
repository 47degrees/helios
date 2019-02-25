package helios.instances

import arrow.core.Either
import arrow.core.fix
import arrow.data.ListK
import arrow.data.k
import arrow.data.sequence
import arrow.instances.either.applicative.applicative
import helios.core.JsArray
import helios.core.Json
import helios.typeclasses.Decoder
import helios.typeclasses.DecodingError
import helios.typeclasses.Encoder

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
      }.k().sequence(Either.applicative()).fix().map(ListK<A>::list)

  companion object {
    operator fun <A> invoke(decoderA: Decoder<A>): Decoder<List<A>> =
      object : ListDecoderInstance<A> {
        override fun decoderA(): Decoder<A> = decoderA
      }
  }

}
