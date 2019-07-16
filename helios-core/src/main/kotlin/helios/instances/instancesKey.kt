package helios.instances

import arrow.core.Either
import helios.core.DecodingError
import helios.core.Json
import helios.core.StringDecodingError
import helios.typeclasses.KeyDecoder
import helios.typeclasses.KeyEncoder
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

fun Double.Companion.keyEncoder() = object : KeyEncoder<Double> {
  override fun Double.keyEncode(): String = this.toString()
}

fun Double.Companion.keyDecoder() = object : KeyDecoder<Double> {
  override fun keyDecode(value: Json): Either<DecodingError, Double> =
    value.asJsString().map { it.value.toString().toDouble() }.toEither { StringDecodingError(value) }
}

fun Float.Companion.keyEncoder() = object : KeyEncoder<Float> {
  override fun Float.keyEncode(): String = this.toString()
}

fun Float.Companion.keyDecoder() = object : KeyDecoder<Float> {
  override fun keyDecode(value: Json): Either<DecodingError, Float> =
    value.asJsString().map { it.value.toString().toFloat() }.toEither { StringDecodingError(value) }
}

fun Long.Companion.keyEncoder() = object : KeyEncoder<Long> {
  override fun Long.keyEncode(): String = this.toString()
}

fun Long.Companion.keyDecoder() = object : KeyDecoder<Long> {
  override fun keyDecode(value: Json): Either<DecodingError, Long> =
    value.asJsString().map { it.value.toString().toLong() }.toEither { StringDecodingError(value) }
}

fun Int.Companion.keyEncoder() = object : KeyEncoder<Int> {
  override fun Int.keyEncode(): String = this.toString()
}

fun Int.Companion.keyDecoder() = object : KeyDecoder<Int> {
  override fun keyDecode(value: Json): Either<DecodingError, Int> =
    value.asJsString().map { it.value.toString().toInt() }.toEither { StringDecodingError(value) }
}

fun Short.Companion.keyEncoder() = object : KeyEncoder<Short> {
  override fun Short.keyEncode(): String = this.toString()
}

fun Short.Companion.keyDecoder() = object : KeyDecoder<Short> {
  override fun keyDecode(value: Json): Either<DecodingError, Short> =
    value.asJsString().map { (it.value.toString().toShort()) }.toEither { StringDecodingError(value) }
}

fun Byte.Companion.keyEncoder() = object : KeyEncoder<Byte> {
  override fun Byte.keyEncode(): String = this.toString()
}

fun Byte.Companion.keyDecoder() = object : KeyDecoder<Byte> {
  override fun keyDecode(value: Json): Either<DecodingError, Byte> =
    value.asJsString().map { (it.value.toString().toByte()) }.toEither { StringDecodingError(value) }
}

fun Boolean.Companion.keyEncoder() = object : KeyEncoder<Boolean> {
  override fun Boolean.keyEncode(): String = this.toString()
}

fun Boolean.Companion.keyDecoder() = object : KeyDecoder<Boolean> {
  override fun keyDecode(value: Json): Either<DecodingError, Boolean> =
    value.asJsString().map { (it.value.toString().toBoolean()) }.toEither { StringDecodingError(value) }
}

fun String.Companion.keyEncoder() = object : KeyEncoder<String> {
  override fun String.keyEncode(): String = this
}

fun String.Companion.keyDecoder() = object : KeyDecoder<String> {
  override fun keyDecode(value: Json): Either<DecodingError, String> =
    value.asJsString().map { it.value.toString() }.toEither { StringDecodingError(value) }
}

val UUIDKeyEncoder = object : KeyEncoder<UUID> {
  override fun UUID.keyEncode(): String = this.toString()
}

val UUIDKeyDecoder = object : KeyDecoder<UUID> {
  override fun keyDecode(value: Json): Either<DecodingError, UUID> =
    value.asJsString().map { UUID.fromString(it.value.toString()) }.toEither { StringDecodingError(value) }
}

val BigDecimalKeyEncoder = object : KeyEncoder<BigDecimal> {
  override fun BigDecimal.keyEncode(): String = this.toString()
}

val BigDecimalKeyDecoder = object : KeyDecoder<BigDecimal> {
  override fun keyDecode(value: Json): Either<DecodingError, BigDecimal> =
    value.asJsString().map { it.value.toString().toBigDecimal() }.toEither { StringDecodingError(value) }
}

val BigIntegerKeyEncoder = object : KeyEncoder<BigInteger> {
  override fun BigInteger.keyEncode(): String = this.toString()
}

val BigIntegerKeyDecoder = object : KeyDecoder<BigInteger> {
  override fun keyDecode(value: Json): Either<DecodingError, BigInteger> =
    value.asJsString().map { it.value.toString().toBigInteger() }.toEither { StringDecodingError(value) }
}
