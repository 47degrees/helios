package helios.syntax.json

import arrow.core.Either
import arrow.core.left
import helios.core.*
import helios.core.DecodingError.*
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

//TODO add syntax for all supported types

fun <B> Json.asJsStringOrError(f: (JsString) -> Either<DecodingError, B>) =
  asJsString().fold({ JsStringDecodingError(this).left() }, { f(it) })

fun <B> Json.asJsNumberOrError(f: (JsNumber) -> Either<DecodingError, B>) =
  asJsNumber().fold({ JsNumberDecodingError(this).left() }, { f(it) })

fun <B> Json.asJsBooleanOrError(f: (JsBoolean) -> Either<DecodingError, B>) =
  asJsBoolean().fold({ JsBooleanDecodingError(this).left() }, { f(it) })

fun <B> Json.asJsArrayOrError(f: (JsArray) -> Either<DecodingError, B>) =
  asJsArray().fold({ JsArrayDecodingError(this).left() }, { f(it) })

fun <B> Json.asJsObjectOrError(f: (JsObject) -> Either<DecodingError, B>) =
  asJsObject().fold({ JsObjectDecodingError(this).left() }, { f(it) })

fun <B> Json.asJsNullOrError(f: (JsNull) -> Either<DecodingError, B>) =
  asJsNull().fold({ JsNullDecodingError(this).left() }, { f(it) })
