package helios.instances

import arrow.core.Either
import arrow.core.Try
import arrow.core.flatMap
import helios.core.*
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
    value.asJsString().toEither { JsStringDecodingError(value) }.flatMap {
      Try { UUID.fromString(it.value.toString()) }.toEither {
        ExceptionOnDecoding(value, "Invalid String cannot be decoded to UUID", it)
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
    value.asJsNumber().toEither { JsNumberDecodingError(value) }.flatMap { Try{ it.toBigDecimal() }.toEither {
      ExceptionOnDecoding(value, "Cannot convert content to BigDecimal", it)
    } }

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
    value.asJsNumber().toEither { JsNumberDecodingError(value) }.flatMap { Try{ it.toBigInteger() }.toEither {
      ExceptionOnDecoding(value, "Cannot convert content to BigInteger", it)
    } }

  companion object {
    operator fun invoke() = object : BigIntegerDecoderInstance {}
  }
}
