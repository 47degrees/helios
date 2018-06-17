package helios.optics

import helios.core.*
import helios.typeclasses.*
import io.kotlintest.properties.Gen

fun Gen.Companion.jsInt(): Gen<JsInt> = Gen.int().let { intGen ->
    Gen.create { JsInt(intGen.generate()) }
}

fun Gen.Companion.jsLong(): Gen<JsLong> = Gen.long().let { longGen ->
    Gen.create { JsLong(longGen.generate()) }
}

fun Gen.Companion.jsFloat(): Gen<JsFloat> = Gen.float().let { floatGen ->
    Gen.create { JsFloat(floatGen.generate()) }
}

fun Gen.Companion.jsDouble(): Gen<JsDouble> = Gen.double().let { doubleGen ->
    Gen.create { JsDouble(doubleGen.generate()) }
}

fun Gen.Companion.jsDecimal(): Gen<JsDecimal> = Gen.double().let {
    Gen.create { JsDecimal(it.generate().toString()) }
}

fun Gen.Companion.jsNumber(): Gen<JsNumber> = Gen.oneOf(Gen.jsInt(), Gen.jsLong(), Gen.jsFloat(), Gen.jsDouble())

fun Gen.Companion.jsString(): Gen<JsString> = Gen.string().let { strGen ->
    Gen.create { JsString(strGen.generate()) }
}

fun Gen.Companion.jsBoolean(): Gen<JsBoolean> = Gen.bool().let { boolGen ->
    Gen.create { JsBoolean(boolGen.generate()) }
}

fun Gen.Companion.jsNull(): Gen<JsNull> = Gen.create { JsNull }


fun Gen.Companion.jsArray(): Gen<JsArray> = genJson().let { gen ->
    Gen.create { Gen.list(gen).generate().let(::JsArray) }
}

fun <T> Gen.Companion.jsArray(EN: Encoder<T>, valid: Gen<T>): Gen<JsArray> = Gen.create {
    Gen.list(valid).generate().map { EN.run { it.encode() } }.let(::JsArray)
}


fun Gen.Companion.jsObject(): Gen<JsObject> = Gen.map(Gen.string(), genJson()).let { gen ->
    Gen.create { gen.generate().let(::JsObject) }
}

fun <T> Gen.Companion.json(EN: Encoder<T>, valid: Gen<T>): Gen<Json> = Gen.create {
    valid.generate().let { EN.run { it.encode() } }
}

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