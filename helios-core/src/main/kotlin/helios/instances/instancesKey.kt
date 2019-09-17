package helios.instances

import arrow.core.Either
import arrow.core.Try
import helios.core.*
import helios.typeclasses.KeyDecoder
import helios.typeclasses.KeyEncoder
import java.math.BigDecimal
import java.math.BigInteger
import java.util.UUID

val DoubleKeyEncoder by lazy {
  object : KeyEncoder<Double> {
    override fun Double.keyEncode(): JsString = JsString(this.toString())
  }
}

fun Double.Companion.keyEncoder() = DoubleKeyEncoder

val DoubleKeyDecoder by lazy {
  object : KeyDecoder<Double> {
    override fun keyDecode(value: JsString): Either<DecodingError, Double> =
      Try(value.value.toString()::toDouble).toEither { JsNumberDecodingError.JsDoubleError(value) }
  }
}

fun Double.Companion.keyDecoder() = DoubleKeyDecoder

val FloatKeyEncoder by lazy {
  object : KeyEncoder<Float> {
    override fun Float.keyEncode(): JsString = JsString(this.toString())
  }
}

fun Float.Companion.keyEncoder() = FloatKeyEncoder

val FloatKeyDecoder by lazy {
  object : KeyDecoder<Float> {
    override fun keyDecode(value: JsString): Either<DecodingError, Float> =
      Try(value.value.toString()::toFloat).toEither { JsNumberDecodingError.JsFloatError(value) }
  }
}

fun Float.Companion.keyDecoder() = FloatKeyDecoder

val LongKeyEncoder by lazy {
  object : KeyEncoder<Long> {
    override fun Long.keyEncode(): JsString = JsString(this.toString())
  }
}

fun Long.Companion.keyEncoder() = LongKeyEncoder

val LongKeyDecoder by lazy {
  object : KeyDecoder<Long> {
    override fun keyDecode(value: JsString): Either<DecodingError, Long> =
      Try(value.value.toString()::toLong).toEither { JsNumberDecodingError.JsLongError(value) }
  }
}

fun Long.Companion.keyDecoder() = LongKeyDecoder

val IntKeyEncoder by lazy {
  object : KeyEncoder<Int> {
    override fun Int.keyEncode(): JsString = JsString(this.toString())
  }
}

fun Int.Companion.keyEncoder() = IntKeyEncoder

val IntKeyDecoder by lazy {
  object : KeyDecoder<Int> {
    override fun keyDecode(value: JsString): Either<DecodingError, Int> =
      Try(value.value.toString()::toInt).toEither { JsNumberDecodingError.JsIntError(value) }
  }
}

fun Int.Companion.keyDecoder() = IntKeyDecoder

val ShortKeyEncoder by lazy {
  object : KeyEncoder<Short> {
    override fun Short.keyEncode(): JsString = JsString(this.toString())
  }
}

fun Short.Companion.keyEncoder() = ShortKeyEncoder

val ShortKeyDecoder by lazy {
  object : KeyDecoder<Short> {
    override fun keyDecode(value: JsString): Either<DecodingError, Short> =
      Try(value.value.toString()::toShort).toEither { JsNumberDecodingError.JsShortError(value) }
  }
}

fun Short.Companion.keyDecoder() = ShortKeyDecoder

val ByteKeyEncoder by lazy {
  object : KeyEncoder<Byte> {
    override fun Byte.keyEncode(): JsString = JsString(this.toString())
  }
}

fun Byte.Companion.keyEncoder() = ByteKeyEncoder

val ByteKeyDecoder by lazy {
  object : KeyDecoder<Byte> {
    override fun keyDecode(value: JsString): Either<DecodingError, Byte> =
      Try(value.value.toString()::toByte).toEither { JsNumberDecodingError.JsByteError(value) }
  }
}

fun Byte.Companion.keyDecoder() = ByteKeyDecoder

val BooleanKeyEncoder by lazy {
  object : KeyEncoder<Boolean> {
    override fun Boolean.keyEncode(): JsString = JsString(this.toString())
  }
}

fun Boolean.Companion.keyEncoder() = BooleanKeyEncoder

val BooleanKeyDecoder by lazy {
  object : KeyDecoder<Boolean> {
    override fun keyDecode(value: JsString): Either<DecodingError, Boolean> =
      Try(value.value.toString()::toBoolean).toEither { JsBooleanDecodingError(value) }
  }
}

fun Boolean.Companion.keyDecoder() = BooleanKeyDecoder

val StringKeyEncoder by lazy {
  object : KeyEncoder<String> {
    override fun String.keyEncode(): JsString = JsString(this)
  }
}

fun String.Companion.keyEncoder() = StringKeyEncoder

val StringKeyDecoder by lazy {
  object : KeyDecoder<String> {
    override fun keyDecode(value: JsString): Either<DecodingError, String> =
      Try(value.value::toString).toEither { JsStringDecodingError(value) }
  }
}

fun String.Companion.keyDecoder() = StringKeyDecoder

val UUIDKeyEncoder by lazy {
  object : KeyEncoder<UUID> {
    override fun UUID.keyEncode(): JsString = JsString(this.toString())
  }
}

val UUIDKeyDecoder by lazy {
  object : KeyDecoder<UUID> {
    override fun keyDecode(value: JsString): Either<DecodingError, UUID> =
      Try { UUID.fromString(value.value.toString()) }.toEither { JsStringDecodingError(value) }
  }
}

val BigDecimalKeyEncoder by lazy {
  object : KeyEncoder<BigDecimal> {
    override fun BigDecimal.keyEncode(): JsString = JsString(this.toString())
  }
}

val BigDecimalKeyDecoder by lazy {
  object : KeyDecoder<BigDecimal> {
    override fun keyDecode(value: JsString): Either<DecodingError, BigDecimal> =
      Try(value.value.toString()::toBigDecimal).toEither { JsNumberDecodingError.JsBigDecimalError(value) }
  }
}

val BigIntegerKeyEncoder by lazy {
  object : KeyEncoder<BigInteger> {
    override fun BigInteger.keyEncode(): JsString = JsString(this.toString())
  }
}

val BigIntegerKeyDecoder by lazy {
  object : KeyDecoder<BigInteger> {
    override fun keyDecode(value: JsString): Either<DecodingError, BigInteger> =
      Try(value.value.toString()::toBigInteger).toEither { JsNumberDecodingError.JsBigIntegerError(value) }
  }
}
