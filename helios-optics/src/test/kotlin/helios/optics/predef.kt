package helios.optics

import helios.core.*
import helios.typeclasses.*
import io.kotlintest.properties.ConstGen
import io.kotlintest.properties.Gen
import io.kotlintest.properties.map

fun Gen.Companion.jsInt(): Gen<JsInt> = Gen.int().map(::JsInt)

fun Gen.Companion.jsLong(): Gen<JsLong> = Gen.long().map(::JsLong)

fun Gen.Companion.jsFloat(): Gen<JsFloat> = Gen.float().map(::JsFloat)

fun Gen.Companion.jsDouble(): Gen<JsDouble> = Gen.double().map(::JsDouble)

fun Gen.Companion.jsDecimal(): Gen<JsDecimal> = Gen.double().map { JsDecimal(it.toString()) }

fun Gen.Companion.jsNumber(): Gen<JsNumber> =
  Gen.oneOf(Gen.jsInt(), Gen.jsLong(), Gen.jsFloat(), Gen.jsDouble())

fun Gen.Companion.jsString(): Gen<JsString> = Gen.string().map(::JsString)

fun Gen.Companion.jsBoolean(): Gen<JsBoolean> = Gen.bool().map(::JsBoolean)

fun Gen.Companion.jsNull(): Gen<JsNull> = ConstGen(JsNull)

fun Gen.Companion.jsArray(): Gen<JsArray> = Gen.list(genJson()).map(::JsArray)

fun <T> Gen.Companion.jsArray(EN: Encoder<T>, valid: Gen<T>): Gen<JsArray> =
  Gen.list(valid).map { JsArray(it.map { EN.run { it.encode() } }) }

fun Gen.Companion.jsObject(): Gen<JsObject> = Gen.map(Gen.string(), genJson()).map(::JsObject)

fun <T> Gen.Companion.json(EN: Encoder<T>, valid: Gen<T>): Gen<Json> =
  valid.map { EN.run { it.encode() } }

fun Gen.Companion.json(): Gen<Json> = Gen.oneOf(
  Gen.jsInt(),
  Gen.jsLong(),
  Gen.jsDouble(),
  Gen.jsString(),
  Gen.jsNull(),
  Gen.jsArray(),
  Gen.jsObject()
)

private fun genJson(): Gen<Json> =
  Gen.oneOf(Gen.jsInt(), Gen.jsLong(), Gen.jsDouble(), Gen.jsString(), Gen.jsNull())