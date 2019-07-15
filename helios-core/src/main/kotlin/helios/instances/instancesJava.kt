package helios.instances

import arrow.core.Either
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
    value.asJsString().map { UUID.fromString(it.value.toString()) }.toEither { StringDecodingError(value) }

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
    value.asJsNumber().map(JsNumber::toBigDecimal).toEither { NumberDecodingError(value) }

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
    value.asJsNumber().map(JsNumber::toBigInteger).toEither { NumberDecodingError(value) }

  companion object {
    operator fun invoke() = object : BigIntegerDecoderInstance {}
  }
}
