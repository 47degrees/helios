package helios.optics

import arrow.Kind
import arrow.core.Predicate
import arrow.core.toT
import arrow.data.k
import arrow.optics.Traversal
import arrow.optics.typeclasses.FilterIndex
import arrow.typeclasses.Applicative

/**
 * Temp until java instances merged into arrow.
 */

interface MapFilterIndexInstance<K, V> : FilterIndex<Map<K, V>, K, V> {
    override fun filter(p: Predicate<K>) = object : Traversal<Map<K, V>, V> {
        override fun <F> modifyF(FA: Applicative<F>, s: Map<K, V>, f: (V) -> Kind<F, V>): Kind<F, Map<K, V>> = FA.run {
            s.toList().k().traverse(FA) { (k, v) ->
                (if (p(k)) f(v) else FA.just(v)).map {
                    k to it
                }
            }.map { it.toMap() }
        }
    }

    companion object {
        operator fun <K, V> invoke(): FilterIndex<Map<K, V>, K, V> = object : MapFilterIndexInstance<K, V> {}
    }
}

interface ListFilterIndexInstance<A> : FilterIndex<List<A>, Int, A> {
    override fun filter(p: (Int) -> Boolean): Traversal<List<A>, A> = object : Traversal<List<A>, A> {
        override fun <F> modifyF(FA: Applicative<F>, s: List<A>, f: (A) -> Kind<F, A>): Kind<F, List<A>> = FA.run {
            s.mapIndexed { index, a -> a toT index }.k().traverse(FA) { (a, j) ->
                if (p(j)) f(a) else a.just()
            }
        }

    }

    companion object {
        operator fun <A> invoke(): FilterIndex<List<A>, Int, A> = object : ListFilterIndexInstance<A> {}
    }
}
