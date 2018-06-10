package helios.optics

import helios.core.*
import helios.typeclasses.*
import io.kotlintest.properties.Gen

fun genJsInt(): Gen<JsInt> = Gen.int().let { intGen ->
    Gen.create { JsInt(intGen.generate()) }
}

fun genJsLong(): Gen<JsLong> = Gen.long().let { longGen ->
    Gen.create { JsLong(longGen.generate()) }
}

fun genJsFloat(): Gen<JsFloat> = Gen.float().let { floatGen ->
    Gen.create { JsFloat(floatGen.generate()) }
}

fun genJsDouble(): Gen<JsDouble> = Gen.double().let { doubleGen ->
    Gen.create { JsDouble(doubleGen.generate()) }
}

fun genJsDecimal(): Gen<JsDecimal> = Gen.double().let {
    Gen.create { JsDecimal(it.generate().toString()) }
}

fun genJsNumber(): Gen<JsNumber> = Gen.oneOf(genJsInt(), genJsLong(), genJsFloat(), genJsDouble())

fun genJsString(): Gen<JsString> = Gen.string().let { strGen ->
    Gen.create { JsString(strGen.generate()) }
}

fun genJsBoolean(): Gen<JsBoolean> = Gen.bool().let { boolGen ->
    Gen.create { JsBoolean(boolGen.generate()) }
}

fun genJsNull(): Gen<JsNull> = Gen.create { JsNull }


fun genJsArray(): Gen<JsArray> = _genJson().let { gen ->
    Gen.create { Gen.list(gen).generate().let(::JsArray) }
}

fun <T> genJsArray(EN: Encoder<T>, valid: Gen<T>): Gen<JsArray> = Gen.create {
    Gen.list(valid).generate().map { EN.run { it.encode() } }.let(::JsArray)
}


fun genJsObject(): Gen<JsObject> = Gen.map(Gen.string(), _genJson()).let { gen ->
    Gen.create { gen.generate().let(::JsObject) }
}

fun <T> genJson(EN: Encoder<T>, valid: Gen<T>): Gen<Json> = Gen.create {
    valid.generate().let{ EN.run { it.encode() } }
}

fun genJson(): Gen<Json> = Gen.oneOf(
        genJsInt(),
        genJsLong(),
        genJsDouble(),
        genJsString(),
        genJsNull(),
        genJsArray(),
        genJsObject()
)

private fun _genJson(): Gen<Json> = Gen.oneOf(
        genJsInt(),
        genJsLong(),
        genJsDouble(),
        genJsString(),
        genJsNull()
)