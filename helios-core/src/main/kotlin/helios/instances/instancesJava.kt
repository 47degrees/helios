package helios.instances

import arrow.core.*
import arrow.core.extensions.either.applicative.applicative
import arrow.core.extensions.either.applicative.map2
import arrow.core.extensions.either.monoid.monoid
import arrow.data.extensions.list.foldable.fold
import arrow.data.extensions.list.foldable.foldLeft
import arrow.data.extensions.list.traverse.sequence
import arrow.data.fix
import arrow.extension
import arrow.typeclasses.Monoid
import helios.core.*
import helios.typeclasses.*
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
    value.asJsNumber().map { it.toBigDecimal() }.toEither { NumberDecodingError(value) }

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
    value.asJsNumber().map { it.toBigInteger() }.toEither { NumberDecodingError(value) }

  companion object {
    operator fun invoke() = object : BigIntegerDecoderInstance {}
  }
}
