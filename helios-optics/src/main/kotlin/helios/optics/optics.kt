package helios.optics

import arrow.Kind
import arrow.core.identity
import arrow.core.left
import arrow.core.right
import arrow.optics.Iso
import arrow.optics.Prism
import arrow.optics.Traversal
import arrow.typeclasses.Applicative
import helios.core.*

inline val Json.Companion.jsBoolean: Prism<Json, JsBoolean>
    inline get() = Prism(getOrModify = { (it as? JsBoolean)?.right() ?: it.left() })

inline val JsBoolean.Companion.value: Iso<JsBoolean, Boolean>
    inline get() = Iso(JsBoolean::value, ::JsBoolean)

inline val Json.Companion.jsString: Prism<Json, JsString>
    inline get() = Prism(getOrModify = { (it as? JsString)?.right() ?: it.left() })

inline val JsString.Companion.value: Iso<JsString, CharSequence>
    inline get() = Iso(JsString::value, ::JsString)

inline val Json.Companion.jsArray: Prism<Json, JsArray>
    inline get() = Prism(getOrModify = { (it as? JsArray)?.right() ?: it.left() })

inline val JsArray.Companion.value: Iso<JsArray, List<Json>>
    inline get() = Iso(JsArray::value, ::JsArray)

inline val Json.Companion.jsObject: Prism<Json, JsObject>
    inline get() = Prism(getOrModify = { (it as? JsObject)?.right() ?: it.left() })

inline val JsObject.Companion.value: Iso<JsObject, Map<String, Json>>
    inline get() = Iso(JsObject::value, ::JsObject)

inline val Json.Companion.jsNull: Prism<Json, JsNull>
    inline get() = Prism(getOrModify = { (it as? JsNull)?.right() ?: it.left() })

inline val Json.Companion.jsNumber: Prism<Json, JsNumber>
    inline get() = Prism(getOrModify = { (it as? JsNumber)?.right() ?: it.left() })

inline val JsNumber.Companion.jsDouble: Prism<JsNumber, JsDouble>
    inline get() = Prism(getOrModify = { (it as? JsDouble)?.right() ?: it.left() })

inline val JsDouble.Companion.value: Iso<JsDouble, Double>
    inline get() = Iso(JsDouble::value, ::JsDouble)

inline val JsNumber.Companion.jsFloat: Prism<JsNumber, JsFloat>
    inline get() = Prism(getOrModify = { (it as? JsFloat)?.right() ?: it.left() })

inline val JsFloat.Companion.value: Iso<JsFloat, Float>
    inline get() = Iso(JsFloat::value, ::JsFloat)

inline val JsNumber.Companion.jsInt: Prism<JsNumber, JsInt>
    inline get() = Prism(getOrModify = { (it as? JsInt)?.right() ?: it.left() })

inline val JsInt.Companion.value: Iso<JsInt, Int>
    inline get() = Iso(JsInt::value, ::JsInt)

inline val JsNumber.Companion.jsLong: Prism<JsNumber, JsLong>
    inline get() = Prism(getOrModify = { (it as? JsLong)?.right() ?: it.left() })

inline val JsLong.Companion.value: Iso<JsLong, Long>
    inline get() = Iso(JsLong::value, ::JsLong)

inline val JsNumber.Companion.jsDecimal: Prism<JsNumber, JsDecimal>
    inline get() = Prism(getOrModify = { (it as? JsDecimal)?.right() ?: it.left() })

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
