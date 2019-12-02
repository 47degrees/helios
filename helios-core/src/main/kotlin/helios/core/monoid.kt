package helios.core

import arrow.core.ListK
import arrow.core.extensions.listk.semigroup.semigroup
import arrow.core.extensions.semigroup
import arrow.core.k
import arrow.typeclasses.Monoid
import arrow.typeclasses.Semigroup

interface JsIntSemigroup : Semigroup<JsInt> {
    override fun JsInt.combine(b: JsInt): JsInt = JsInt(Int.semigroup().run {
        value.combine(b.value)
    })
}

interface JsIntMonoid : Monoid<JsInt>, JsIntSemigroup {
    override fun empty(): JsInt = JsInt(0)
}

interface JsLongSemigroup : Semigroup<JsLong> {
    override fun JsLong.combine(b: JsLong): JsLong = JsLong(Long.semigroup().run {
        value.combine(b.value)
    })
}

interface JsLongMonoid : Monoid<JsLong>, JsLongSemigroup {
    override fun empty(): JsLong = JsLong(0L)
}

interface JsFloatSemigroup : Semigroup<JsFloat> {
    override fun JsFloat.combine(b: JsFloat): JsFloat = JsFloat(Float.semigroup().run {
        value.combine(b.value)
    })
}

interface JsFloatMonoid : Monoid<JsFloat>, JsFloatSemigroup {
    override fun empty(): JsFloat = JsFloat(0f)
}

interface JsDoubleSemigroup : Semigroup<JsDouble> {
    override fun JsDouble.combine(b: JsDouble): JsDouble = JsDouble(Double.semigroup().run {
        value.combine(b.value)
    })
}

interface JsDoubleMonoid : Monoid<JsDouble>, JsDoubleSemigroup {
    override fun empty(): JsDouble = JsDouble(0.0)
}

interface JsStringSemigroup : Semigroup<JsString> {
    override fun JsString.combine(b: JsString): JsString = JsString(String.semigroup().run {
        value.toString().combine(b.value.toString())
    })
}

interface JsStringMonoid : Monoid<JsString>, JsStringSemigroup {
    override fun empty(): JsString = JsString("")
}

interface JsArraySemigroup : Semigroup<JsArray> {
    override fun JsArray.combine(b: JsArray): JsArray = JsArray(ListK.semigroup<Json>().run {
        value.k().combine(b.value.k())
    })
}

interface JsArrayMonoid : Monoid<JsArray>, JsArraySemigroup {
    override fun empty(): JsArray = JsArray(emptyList())
}

interface JsObjectSemigroup : Semigroup<JsObject> {
    override fun JsObject.combine(b: JsObject): JsObject = JsObject(value + b.value)
}

interface JsObjectMonoid : Monoid<JsObject>, JsObjectSemigroup {
    override fun empty(): JsObject = JsObject(emptyMap())
}

fun JsInt.Companion.semigroup(): JsIntSemigroup = object : JsIntSemigroup {  }

fun JsInt.Companion.monoid(): JsIntMonoid = object : JsIntMonoid {  }

fun JsLong.Companion.semigroup(): JsLongSemigroup = object : JsLongSemigroup {  }

fun JsLong.Companion.monoid(): JsLongMonoid = object : JsLongMonoid {  }

fun JsFloat.Companion.semigroup(): JsFloatSemigroup = object : JsFloatSemigroup {  }

fun JsFloat.Companion.monoid(): JsFloatMonoid = object : JsFloatMonoid {  }

fun JsDouble.Companion.semigroup(): JsDoubleSemigroup = object : JsDoubleSemigroup {  }

fun JsDouble.Companion.monoid(): JsDoubleMonoid = object : JsDoubleMonoid {  }

fun JsString.Companion.semigroup(): JsStringSemigroup = object : JsStringSemigroup {  }

fun JsString.Companion.monoid(): JsStringMonoid = object : JsStringMonoid {  }

fun JsArray.Companion.semigroup(): JsArraySemigroup = object : JsArraySemigroup {  }

fun JsArray.Companion.monoid(): JsArrayMonoid = object : JsArrayMonoid {  }

fun JsObject.Companion.semigroup(): JsObjectSemigroup = object : JsObjectSemigroup {  }

fun JsObject.Companion.monoid(): JsObjectMonoid = object : JsObjectMonoid {  }
