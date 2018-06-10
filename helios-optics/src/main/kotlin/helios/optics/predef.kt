package helios.optics

import arrow.Kind
import arrow.core.identity
import arrow.optics.Traversal
import arrow.typeclasses.Applicative
import helios.core.*

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
