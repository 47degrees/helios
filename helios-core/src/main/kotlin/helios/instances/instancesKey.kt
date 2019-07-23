package helios.instances

import arrow.core.Either
import arrow.core.Try
import helios.core.*
import helios.typeclasses.KeyDecoder
import helios.typeclasses.KeyEncoder
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

fun Double.Companion.keyEncoder() = object : KeyEncoder<Double> {
  override fun Double.keyEncode(): JsString = JsString(this.toString())
}

fun Double.Companion.keyDecoder() = object : KeyDecoder<Double> {
  override fun keyDecode(value: JsString): Either<DecodingError, Double> =
    Try(value.value.toString()::toDouble).toEither { JsNumberDecodingError.JsDoubleError(value) }
}

fun Float.Companion.keyEncoder() = object : KeyEncoder<Float> {
  override fun Float.keyEncode(): JsString = JsString(this.toString())
}

fun Float.Companion.keyDecoder() = object : KeyDecoder<Float> {
  override fun keyDecode(value: JsString): Either<DecodingError, Float> =
    Try(value.value.toString()::toFloat).toEither { JsNumberDecodingError.JsFloatError(value) }
}

fun Long.Companion.keyEncoder() = object : KeyEncoder<Long> {
  override fun Long.keyEncode(): JsString = JsString(this.toString())
}

fun Long.Companion.keyDecoder() = object : KeyDecoder<Long> {
  override fun keyDecode(value: JsString): Either<DecodingError, Long> =
    Try(value.value.toString()::toLong).toEither { JsNumberDecodingError.JsLongError(value) }
}

fun Int.Companion.keyEncoder() = object : KeyEncoder<Int> {
  override fun Int.keyEncode(): JsString = JsString(this.toString())
}

fun Int.Companion.keyDecoder() = object : KeyDecoder<Int> {
  override fun keyDecode(value: JsString): Either<DecodingError, Int> =
    Try(value.value.toString()::toInt).toEither { JsNumberDecodingError.JsIntError(value) }
}

fun Short.Companion.keyEncoder() = object : KeyEncoder<Short> {
  override fun Short.keyEncode(): JsString = JsString(this.toString())
}

fun Short.Companion.keyDecoder() = object : KeyDecoder<Short> {
  override fun keyDecode(value: JsString): Either<DecodingError, Short> =
    Try(value.value.toString()::toShort).toEither { JsNumberDecodingError.JsShortError(value) }
}

fun Byte.Companion.keyEncoder() = object : KeyEncoder<Byte> {
  override fun Byte.keyEncode(): JsString = JsString(this.toString())
}

fun Byte.Companion.keyDecoder() = object : KeyDecoder<Byte> {
  override fun keyDecode(value: JsString): Either<DecodingError, Byte> =
    Try(value.value.toString()::toByte).toEither { JsNumberDecodingError.JsByteError(value) }
}

fun Boolean.Companion.keyEncoder() = object : KeyEncoder<Boolean> {
  override fun Boolean.keyEncode(): JsString = JsString(this.toString())
}

fun Boolean.Companion.keyDecoder() = object : KeyDecoder<Boolean> {
  override fun keyDecode(value: JsString): Either<DecodingError, Boolean> =
    Try(value.value.toString()::toBoolean).toEither { JsBooleanDecodingError(value) }
}

fun String.Companion.keyEncoder() = object : KeyEncoder<String> {
  override fun String.keyEncode(): JsString = JsString(this)
}

fun String.Companion.keyDecoder() = object : KeyDecoder<String> {
  override fun keyDecode(value: JsString): Either<DecodingError, String> =
    Try(value.value::toString).toEither { JsStringDecodingError(value) }
}

val UUIDKeyEncoder = object : KeyEncoder<UUID> {
  override fun UUID.keyEncode(): JsString = JsString(this.toString())
}

val UUIDKeyDecoder = object : KeyDecoder<UUID> {
  override fun keyDecode(value: JsString): Either<DecodingError, UUID> =
    Try { UUID.fromString(value.value.toString()) }.toEither { JsStringDecodingError(value) }
}

val BigDecimalKeyEncoder = object : KeyEncoder<BigDecimal> {
  override fun BigDecimal.keyEncode(): JsString = JsString(this.toString())
}

val BigDecimalKeyDecoder = object : KeyDecoder<BigDecimal> {
  override fun keyDecode(value: JsString): Either<DecodingError, BigDecimal> =
    Try(value.value.toString()::toBigDecimal).toEither { JsNumberDecodingError.JsBigDecimalError(value) }
}

val BigIntegerKeyEncoder = object : KeyEncoder<BigInteger> {
  override fun BigInteger.keyEncode(): JsString = JsString(this.toString())
}

val BigIntegerKeyDecoder = object : KeyDecoder<BigInteger> {
  override fun keyDecode(value: JsString): Either<DecodingError, BigInteger> =
    Try(value.value.toString()::toBigInteger).toEither { JsNumberDecodingError.JsBigIntegerError(value) }
}
