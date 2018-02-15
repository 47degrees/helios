package helios.optics

import arrow.Kind
import arrow.Kind3
import arrow.core.identity
import arrow.optics.typeclasses.each
import arrow.typeclasses.Applicative
import helios.core.JsArray
import helios.core.JsNull
import helios.core.JsObject
import helios.core.Json

/**
 * [Traversal] with focus in all values of a JsObject of JsArray.
 */
val jsonTraversal = object : Traversal<Json, Json> {
    override fun <F> modifyF(FA: Applicative<F>, s: Json, f: (Json) -> Kind<F, Json>): Kind<F, Json> = s.fold(
            { FA.pure(it) },
            { FA.pure(it) },
            { arr -> FA.map(each<JsArray, Json>().each().modifyF(FA, arr, f), ::identity) },
            { obj -> FA.map(each<JsObject, Json>().each().modifyF(FA, obj, f), ::identity) },
            { FA.pure(it) },
            { FA.pure(JsNull) }
    )
}

typealias Kind4<F, A, B, C, D> = Kind<Kind3<F, A, B, C>, D>

fun <S, T, A, B> arrow.optics.POptional<S, T, A, B>.asHPOptional(): POptional<S, T, A, B> = POptional(
        getOrModify = this::getOrModify,
        set = { b -> { s -> this.set(s, b) } }
)

fun <S, T, A, B> arrow.optics.PTraversal<S, T, A, B>.asHPTraversal() = object : PTraversal<S, T, A, B> {
    override fun <F> modifyF(FA: Applicative<F>, s: S, f: (A) -> Kind<F, B>): Kind<F, T> =
            this@asHPTraversal.modifyF(FA, s, f)
}