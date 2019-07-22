package helios.instances

import arrow.core.Either
import arrow.core.Try
import helios.core.DecodingError
import helios.core.DecodingError.ExceptionOnDecoding
import helios.core.JsNumber
import helios.core.JsString
import helios.core.Json
import helios.syntax.json.asJsNumberOrError
import helios.syntax.json.asJsStringOrError
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

interface UUIDEncoderInstance : Encoder<UUID> {
  override fun UUID.encode(): Json = JsString(this.toString())

  companion object {
    operator fun invoke() = object : UUIDEncoderInstance {}
  }
}

interface UUIDDecoderInstance : Decoder<UUID> {
  override fun decode(value: Json): Either<DecodingError, UUID> =
    value.asJsStringOrError {
      Try { UUID.fromString(it.value.toString()) }.toEither { ex ->
        ExceptionOnDecoding(value, "Invalid String cannot be decoded to UUID", ex)
      }
    }

  companion object {
    operator fun invoke() = object : UUIDDecoderInstance {}
  }
}

interface BigDecimalEncoderInstance : Encoder<BigDecimal> {
  override fun BigDecimal.encode(): Json = JsNumber(this)

  companion object {
    operator fun invoke() = object : BigDecimalEncoderInstance {}
  }
}

interface BigDecimalDecoderInstance : Decoder<BigDecimal> {
  override fun decode(value: Json): Either<DecodingError, BigDecimal> =
    value.asJsNumberOrError {
      Try(it::toBigDecimal).toEither { ex ->
        ExceptionOnDecoding(value, "Cannot convert content to BigDecimal", ex)
      }
    }

  companion object {
    operator fun invoke() = object : BigDecimalDecoderInstance {}
  }
}

interface BigIntegerEncoderInstance : Encoder<BigInteger> {
  override fun BigInteger.encode(): Json = JsNumber(this)

  companion object {
    operator fun invoke() = object : BigIntegerEncoderInstance {}
  }
}

interface BigIntegerDecoderInstance : Decoder<BigInteger> {
  override fun decode(value: Json): Either<DecodingError, BigInteger> =
    value.asJsNumberOrError {
      Try(it::toBigInteger).toEither { ex ->
        ExceptionOnDecoding(value, "Cannot convert content to BigInteger", ex)
      }
    }

  companion object {
    operator fun invoke() = object : BigIntegerDecoderInstance {}
  }
}
