package helios.syntax.json

import helios.core.*
import java.math.BigDecimal
import java.math.BigInteger

fun String.toJson(): JsString = JsString(this)

fun Boolean.toJson(): JsBoolean = JsBoolean(this)

fun Int.toJson(): JsInt = JsInt(this)

fun Double.toJson(): JsDouble = JsDouble(this)

fun Float.toJson(): JsFloat = JsFloat(this)

fun Long.toJson(): JsLong = JsLong(this)

fun BigInteger.toJson(): JsDecimal = JsNumber.fromDecimalStringUnsafe(this.toString())

fun BigInteger.toJson(radix: Int): JsDecimal =
  JsNumber.fromDecimalStringUnsafe(this.toString(radix))

fun BigDecimal.toJson(): JsDecimal = JsNumber.fromDecimalStringUnsafe(this.toString())

//TODO add more syntax