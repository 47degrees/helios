@file:JvmName("JsonOptics")

package helios.optics

import arrow.Kind
import arrow.core.*
import arrow.optics.Iso
import arrow.optics.PPrism
import arrow.optics.Prism
import arrow.optics.Traversal
import arrow.typeclasses.Applicative
import helios.core.*
import helios.optics.jsarray.each.each
import helios.optics.jsobject.each.each

fun <S, A : S> PPrism.Companion.fromOption(getOption: (S) -> Option<A>, reverseGet: (A) -> S): Prism<S, A> =
  Prism({ s: S -> getOption(s).toEither { s } }, { a: A -> reverseGet(a) })

@PublishedApi
internal val boolean = Prism.fromOption<Json, JsBoolean>(Json::asJsBoolean) { identity(it) }
inline val Json.Companion.jsBoolean: Prism<Json, JsBoolean>
  inline get() = boolean

@PublishedApi
internal val booleanValue = Iso(JsBoolean::value, ::JsBoolean)
inline val JsBoolean.Companion.value: Iso<JsBoolean, Boolean>
  inline get() = booleanValue

@PublishedApi
internal val string = Prism.fromOption<Json, JsString>(Json::asJsString) { identity(it) }
inline val Json.Companion.jsString: Prism<Json, JsString>
  inline get() = string

@PublishedApi
internal val stringValue = Iso(JsString::value, ::JsString)
inline val JsString.Companion.value: Iso<JsString, CharSequence>
  inline get() = stringValue

@PublishedApi
internal val array = Prism.fromOption<Json, JsArray>(Json::asJsArray) { identity(it) }
inline val Json.Companion.jsArray: Prism<Json, JsArray>
  inline get() = array

@PublishedApi
internal val arrayValue = Iso(JsArray::value, ::JsArray)
inline val JsArray.Companion.value: Iso<JsArray, List<Json>>
  inline get() = arrayValue

@PublishedApi
internal val `object` = Prism.fromOption<Json, JsObject>(Json::asJsObject) { identity(it) }
inline val Json.Companion.jsObject: Prism<Json, JsObject>
  inline get() = `object`

@PublishedApi
internal val objectValue = Iso(JsObject::value, ::JsObject)
inline val JsObject.Companion.value: Iso<JsObject, Map<String, Json>>
  inline get() = objectValue

@PublishedApi
internal val `null` = Prism.fromOption<Json, JsNull>(Json::asJsNull) { identity(it) }
inline val Json.Companion.jsNull: Prism<Json, JsNull>
  inline get() = `null`

@PublishedApi
internal val number = Prism.fromOption<Json, JsNumber>({ (it as? JsNumber).toOption() }, { identity(it) })
inline val Json.Companion.jsNumber: Prism<Json, JsNumber>
  inline get() = number

@PublishedApi
internal val double = Prism.fromOption<JsNumber, JsDouble>({ JsDouble(it.toDouble()).some() }, { identity(it) })
inline val JsNumber.Companion.jsDouble: Prism<JsNumber, JsDouble>
  inline get() = double

@PublishedApi
internal val doubleValue = Iso(JsDouble::value, ::JsDouble)
inline val JsDouble.Companion.value: Iso<JsDouble, Double>
  inline get() = doubleValue

@PublishedApi
internal val float = Prism.fromOption<JsNumber, JsFloat>({ JsFloat(it.toFloat()).some() }, { identity(it) })
inline val JsNumber.Companion.jsFloat: Prism<JsNumber, JsFloat>
  inline get() = float

@PublishedApi
internal val floatValue = Iso(JsFloat::value, ::JsFloat)
inline val JsFloat.Companion.value: Iso<JsFloat, Float>
  inline get() = floatValue

@PublishedApi
internal val long = Prism.fromOption<JsNumber, JsLong>({ JsLong(it.toLong()).some() }, { identity(it) })
inline val JsNumber.Companion.jsLong: Prism<JsNumber, JsLong>
  inline get() = long

@PublishedApi
internal val longValue = Iso(JsLong::value, ::JsLong)
inline val JsLong.Companion.value: Iso<JsLong, Long>
  inline get() = longValue

@PublishedApi
internal val int = Prism.fromOption<JsNumber, JsInt>({ it.toInt().map(::JsInt) }, { identity(it) })
inline val JsNumber.Companion.jsInt: Prism<JsNumber, JsInt>
  inline get() = int

@PublishedApi
internal val intValue = Iso(JsInt::value, ::JsInt)
inline val JsInt.Companion.value: Iso<JsInt, Int>
  inline get() = intValue

@PublishedApi
internal val decimal = Prism.fromOption<JsNumber, JsDecimal>({ (it as? JsDecimal).toOption() }, { identity(it) })
inline val JsNumber.Companion.jsDecimal: Prism<JsNumber, JsDecimal>
  inline get() = decimal

@PublishedApi
internal val decimalValue = Iso(JsDecimal::value, ::JsDecimal)
inline val JsDecimal.Companion.value: Iso<JsDecimal, String>
  inline get() = decimalValue

/**
 * [Traversal] with focus in all values of a JsObject of JsArray.
 */
fun Json.Companion.traversal(): Traversal<Json, Json> = object : Traversal<Json, Json> {
  override fun <F> modifyF(FA: Applicative<F>, s: Json, f: (Json) -> Kind<F, Json>): Kind<F, Json> =
    with(FA) {
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
