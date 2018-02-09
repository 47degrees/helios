package helios.optics

import arrow.Kind
import arrow.core.identity
import arrow.optics.Traversal
import arrow.typeclasses.Applicative
import helios.core.JsArray
import helios.core.JsNull
import helios.core.JsObject
import helios.core.Json

/**
 * [Traversal] with focus in all values of a JsObject of JsArray.
 */
val jsonDescendants = object : Traversal<Json, Json> {
    override fun <F> modifyF(FA: Applicative<F>, s: Json, f: (Json) -> Kind<F, Json>): Kind<F, Json> = s.fold(
            { FA.pure(it) },
            { FA.pure(it) },
            { arr -> FA.map(each<JsArray, Json>().each().modifyF(FA, arr, f), ::identity) },
            { obj -> FA.map(each<JsObject, Json>().each().modifyF(FA, obj, f), ::identity) },
            { FA.pure(it) },
            { FA.pure(JsNull) }
    )
}
