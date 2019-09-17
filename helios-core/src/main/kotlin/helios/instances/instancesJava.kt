package helios.instances

import arrow.core.Either
import arrow.core.Try
import helios.core.*
import helios.syntax.json.asJsNumberOrError
import helios.syntax.json.asJsStringOrError
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import java.math.BigDecimal
import java.math.BigInteger
import java.util.UUID

val UUIDEncoder: Encoder<UUID> by lazy {
  object : Encoder<UUID> {
    override fun UUID.encode(): Json = JsString(this.toString())
  }
}

val UUIDDecoder: Decoder<UUID> by lazy {
  object : Decoder<UUID> {
    override fun decode(value: Json): Either<DecodingError, UUID> =
      value.asJsStringOrError {
        Try { UUID.fromString(it.value.toString()) }.toEither { ex ->
          ExceptionOnDecoding(value, "Invalid String cannot be decoded to UUID", ex)
        }
      }
  }
}

val bigDecimalEncoder: Encoder<BigDecimal> by lazy {
  object : Encoder<BigDecimal> {
    override fun BigDecimal.encode(): Json = JsNumber(this)
  }
}

val bigDecimalDecoder: Decoder<BigDecimal> by lazy {
  object : Decoder<BigDecimal> {
    override fun decode(value: Json): Either<DecodingError, BigDecimal> =
      value.asJsNumberOrError(JsNumberDecodingError.JsBigDecimalError(value)) {
        Try(it::toBigDecimal).toEither { JsNumberDecodingError.JsBigDecimalError(value) }
      }
  }
}

val bigIntegerEncoder: Encoder<BigInteger> by lazy {
  object : Encoder<BigInteger> {
    override fun BigInteger.encode(): Json = JsNumber(this)
  }
}

val bigIntegerDecoder: Decoder<BigInteger> by lazy {
  object : Decoder<BigInteger> {
    override fun decode(value: Json): Either<DecodingError, BigInteger> =
      value.asJsNumberOrError(JsNumberDecodingError.JsBigIntegerError(value)) {
        Try(it::toBigInteger).toEither { JsNumberDecodingError.JsBigIntegerError(value) }
      }
  }
}
