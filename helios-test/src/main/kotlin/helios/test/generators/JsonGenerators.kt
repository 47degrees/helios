package helios.test.generators

import arrow.core.identity
import helios.core.*
import helios.typeclasses.Encoder
import io.kotlintest.properties.Gen

  private fun genJson(): Gen<Json> =
    Gen.oneOf(
      Gen.jsInt().map { identity<Json>(it) },
      Gen.jsLong().map { identity<Json>(it) },
      Gen.jsDouble().map { identity<Json>(it) },
      Gen.jsString().map { identity<Json>(it) },
      Gen.jsNull().map { identity<Json>(it) })

  fun Gen.Companion.alphaStr() = Gen.string().filter { str -> str.filter { it.isLetterOrDigit() }.isNotBlank() }

  fun Gen.Companion.jsInt(): Gen<JsInt> = Gen.int().map(::JsInt)

  fun Gen.Companion.jsLong(): Gen<JsLong> = Gen.long().map(::JsLong)

  fun Gen.Companion.jsFloat(): Gen<JsFloat> = Gen.float().filterNot { it.isNaN() }.map(::JsFloat)

  fun Gen.Companion.jsDouble(): Gen<JsDouble> = Gen.double().filterNot { it.isNaN() }.map(::JsDouble)

  fun Gen.Companion.jsDecimal(): Gen<JsDecimal> = Gen.double().filterNot { it.isNaN() }.map { JsDecimal(it.toString()) }

  fun Gen.Companion.jsNumber(): Gen<JsNumber> =
    Gen.oneOf(
      Gen.jsInt().map { identity<JsNumber>(it) },
      Gen.jsLong().map { identity<JsNumber>(it) },
      Gen.jsFloat().map { identity<JsNumber>(it) },
      Gen.jsDouble().map { identity<JsNumber>(it) })

  fun Gen.Companion.jsString(): Gen<JsString> = Gen.alphaStr().map(::JsString)

  fun Gen.Companion.jsBoolean(): Gen<JsBoolean> = Gen.bool().map(::JsBoolean)

  fun Gen.Companion.jsNull(): Gen<JsNull> = Gen.constant(JsNull)

  fun Gen.Companion.jsArray(): Gen<JsArray> = Gen.list(genJson()).map(::JsArray)

  fun <T> Gen.Companion.jsArray(valid: Gen<T>, EN: Encoder<T>): Gen<JsArray> =
    Gen.list(valid).map { list -> JsArray(list.map { elem -> EN.run { elem.encode() } }) }

  fun Gen.Companion.jsObject(): Gen<JsObject> = Gen.map(Gen.alphaStr(), genJson()).map(::JsObject)

  fun <T> Gen.Companion.json(valid: Gen<T>, EN: Encoder<T>): Gen<Json> =
    valid.map { EN.run { it.encode() } }

  fun Gen.Companion.json(): Gen<Json> = Gen.oneOf(
    Gen.jsInt().map { identity<Json>(it) },
    Gen.jsLong().map { identity<Json>(it) },
    Gen.jsDouble().map { identity<Json>(it) },
    Gen.jsString().map { identity<Json>(it) },
    Gen.jsNull().map { identity<Json>(it) },
    Gen.jsArray().map { identity<Json>(it) },
    Gen.jsObject().map { identity<Json>(it) }
  )
