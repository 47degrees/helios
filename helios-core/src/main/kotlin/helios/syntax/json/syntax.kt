package helios.syntax.json

import helios.core.*
import helios.typeclasses.*
import java.math.BigInteger

inline fun <reified A> A.toJson(): Json = encoder<A>().encode(this)

fun String.toJson(): JsString = JsString(this)

fun Boolean.toJson(): JsBoolean = JsBoolean(this)

fun Int.toJson(): JsInt = JsInt(this)

fun Double.toJson(): JsDouble = JsDouble(this)

fun Float.toJson(): JsFloat = JsFloat(this)

fun Long.toJson(): JsLong = JsLong(this)

fun BigInteger.toJson(): JsDecimal = JsNumber.fromDecimalStringUnsafe(this.toString())

fun BigInteger.toJson(radix: Int): JsDecimal = JsNumber.fromDecimalStringUnsafe(this.toString(radix))

inline fun <reified A> Iterable<A>.toJson(EA: Encoder<A> = encoder()): JsArray = JsArray(map(EA::encode))
