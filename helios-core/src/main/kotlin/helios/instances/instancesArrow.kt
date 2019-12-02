package helios.instances

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.apply.map2
import helios.core.*
import helios.syntax.json.asJsArrayOrError
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder

fun <A> Option.Companion.encoder(encoderA: Encoder<A>) = object : Encoder<Option<A>> {
  override fun Option<A>.encode(): Json =
    fold({ JsNull }, { encoderA.run { it.encode() } })
}

fun <A> Option.Companion.decoder(decoderA: Decoder<A>) = object : Decoder<Option<A>> {
  override fun decode(value: Json): Either<DecodingError, Option<A>> =
    if (value.isNull) None.right() else decoderA.decode(value).map { Some(it) }
}

fun <A, B> Either.Companion.encoder(encoderA: Encoder<A>, encoderB: Encoder<B>) =
  object : Encoder<Either<A, B>> {
    override fun Either<A, B>.encode(): Json =
      fold({ encoderA.run { it.encode() } },
        { encoderB.run { it.encode() } })
  }

fun <A, B> Either.Companion.decoder(decoderA: Decoder<A>, decoderB: Decoder<B>) =
  object : Decoder<Either<A, B>> {
    override fun decode(value: Json): Either<DecodingError, Either<A, B>> =
      decoderB.decode(value).fold({ decoderA.decode(value).map { it.left() } },
        { v -> v.right().map { it.right() } })
  }

fun <A> NonEmptyList.Companion.encoder(encoderA: Encoder<A>) = object : Encoder<NonEmptyList<A>> {
  override fun NonEmptyList<A>.encode(): Json =
    ListEncoder(encoderA).run { all.encode() }
}

fun <A> NonEmptyList.Companion.decoder(decoderA: Decoder<A>) = object : Decoder<NonEmptyList<A>> {
  override fun decode(value: Json): Either<DecodingError, NonEmptyList<A>> =
    ListDecoder(decoderA).decode(value).flatMap {
      fromList(it).toEither {
        ExceptionOnDecoding(value, "Empty JsonArray cannot be decoded to NonEmptyList")
      }
    }
}

fun <A, B> Tuple2.Companion.encoder(encoderA: Encoder<A>, encoderB: Encoder<B>) =
  object : Encoder<Tuple2<A, B>> {
    override fun Tuple2<A, B>.encode(): Json = JsArray(
      listOf(
        encoderA.run { a.encode() },
        encoderB.run { b.encode() }
      )
    )
  }

fun <A, B> Tuple2.Companion.decoder(decoderA: Decoder<A>, decoderB: Decoder<B>) =
  object : Decoder<Tuple2<A, B>> {
    override fun decode(value: Json): Either<DecodingError, Tuple2<A, B>> =
      value.asJsArrayOrError { (arr) ->
        if (arr.size == 2)
          decoderA.decode(arr.first()).map2(decoderB.decode(arr.last())) { it }.fix()
        else JsArrayDecodingError(value).left()
      }
  }

fun <A, B, C> Tuple3.Companion.encoder(
  encoderA: Encoder<A>,
  encoderB: Encoder<B>,
  encoderC: Encoder<C>
) = object : Encoder<Tuple3<A, B, C>> {
  override fun Tuple3<A, B, C>.encode(): Json = JsArray(
    listOf(
      encoderA.run { a.encode() },
      encoderB.run { b.encode() },
      encoderC.run { c.encode() }
    )
  )
}

fun <A, B, C> Tuple3.Companion.decoder(
  decoderA: Decoder<A>,
  decoderB: Decoder<B>,
  decoderC: Decoder<C>
) = object : Decoder<Tuple3<A, B, C>> {

  override fun decode(value: Json): Either<DecodingError, Tuple3<A, B, C>> =
    value.asJsArrayOrError { (arr) ->
      if (arr.size == 3)
        Either.applicative<DecodingError>().map(
          decoderA.decode(arr[0]),
          decoderB.decode(arr[1]),
          decoderC.decode(arr[2])
        ) { it }.fix()
      else JsArrayDecodingError(value).left()
    }

}

