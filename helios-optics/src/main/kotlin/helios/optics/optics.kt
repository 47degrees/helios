package helios.optics

import arrow.Kind
import arrow.core.*
import arrow.optics.Iso
import arrow.optics.PPrism
import arrow.optics.Prism
import arrow.optics.Traversal
import arrow.typeclasses.Applicative
import helios.core.*

fun <S, A : S> PPrism.Companion.fromOption(getOption: (S) -> Option<A>): Prism<S, A> =
        Prism(getOrModify = { getOption(it).toEither { it } })

inline val Json.Companion.jsBoolean: Prism<Json, JsBoolean>
    inline get() = Prism.fromOption { it.asJsBoolean() }

inline val JsBoolean.Companion.value: Iso<JsBoolean, Boolean>
    inline get() = Iso(JsBoolean::value, ::JsBoolean)

inline val Json.Companion.jsString: Prism<Json, JsString>
    inline get() = Prism.fromOption { it.asJsString() }

inline val JsString.Companion.value: Iso<JsString, CharSequence>
    inline get() = Iso(JsString::value, ::JsString)

inline val Json.Companion.jsArray: Prism<Json, JsArray>
    inline get() = Prism.fromOption { it.asJsArray() }

inline val JsArray.Companion.value: Iso<JsArray, List<Json>>
    inline get() = Iso(JsArray::value, ::JsArray)

inline val Json.Companion.jsObject: Prism<Json, JsObject>
    inline get() = Prism.fromOption { it.asJsObject() }

inline val JsObject.Companion.value: Iso<JsObject, Map<String, Json>>
    inline get() = Iso(JsObject::value, ::JsObject)

inline val Json.Companion.jsNull: Prism<Json, JsNull>
    inline get() = Prism.fromOption { it.asJsNull() }

inline val Json.Companion.jsNumber: Prism<Json, JsNumber>
    inline get() = Prism.fromOption { it.asJsNumber() }

inline val JsNumber.Companion.jsDouble: Prism<JsNumber, JsDouble>
    inline get() = Prism(getOrModify = { JsDouble(it.toDouble()).right() })

inline val JsDouble.Companion.value: Iso<JsDouble, Double>
    inline get() = Iso(JsDouble::value, ::JsDouble)

inline val JsNumber.Companion.jsFloat: Prism<JsNumber, JsFloat>
    inline get() = Prism(getOrModify = { JsFloat(it.toDouble().toFloat()).right() })

inline val JsFloat.Companion.value: Iso<JsFloat, Float>
    inline get() = Iso(JsFloat::value, ::JsFloat)

inline val JsNumber.Companion.jsInt: Prism<JsNumber, JsInt>
    inline get() = Prism.fromOption { it.toInt().map(::JsInt) }

inline val JsInt.Companion.value: Iso<JsInt, Int>
    inline get() = Iso(JsInt::value, ::JsInt)

inline val JsNumber.Companion.jsLong: Prism<JsNumber, JsLong>
    inline get() = Prism.fromOption { it.toLong().map(::JsLong) }

inline val JsLong.Companion.value: Iso<JsLong, Long>
    inline get() = Iso(JsLong::value, ::JsLong)

inline val JsNumber.Companion.jsDecimal: Prism<JsNumber, JsDecimal>
    inline get() = Prism.fromOption { (it as? JsDecimal).toOption() }

inline val JsDecimal.Companion.value: Iso<JsDecimal, String>
    inline get() = Iso(JsDecimal::value, ::JsDecimal)

/**
 * [Traversal] with focus in all values of a JsObject of JsArray.
 */
fun Json.Companion.traversal(): Traversal<Json, Json> = object : Traversal<Json, Json> {
    override fun <F> modifyF(FA: Applicative<F>, s: Json, f: (Json) -> Kind<F, Json>): Kind<F, Json> = with(FA) {
        s.fold(
                { it.just() },
                { it.just() },
                { arr -> JsArray.each().each().modifyF(FA, arr, f).map(::identity) },
                { obj -> JsObject.each().each().modifyF(FA, obj, f).map(::identity) },
                { it.just() },
                { JsNull.just() }
        )
    }
}
