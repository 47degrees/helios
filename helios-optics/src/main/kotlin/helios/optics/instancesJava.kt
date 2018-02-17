package java_util

import arrow.Kind
import arrow.core.Predicate
import arrow.core.toT
import arrow.data.ListK
import arrow.data.k
import arrow.data.traverse
import arrow.optics.Traversal
import arrow.optics.typeclasses.FilterIndex
import arrow.typeclasses.Applicative

/**
 * Temp until java instances merged into arrow.
 */

interface MapFilterIndexInstance<K, V> : FilterIndex<Map<K, V>, K, V> {
    override fun filter(p: Predicate<K>) = object : Traversal<Map<K, V>, V> {
        override fun <F> modifyF(FA: Applicative<F>, s: Map<K, V>, f: (V) -> Kind<F, V>): Kind<F, Map<K, V>> =
                ListK.traverse().traverse(s.toList().k(), { (k, v) ->
                    FA.map(if (p(k)) f(v) else FA.pure(v)) {
                        k to it
                    }
                }, FA).let {
                    FA.map(it) {
                        it.toMap()
                    }
                }
    }
}

object MapFilterIndexInstanceImplicits {
    fun <K, V> instance(): FilterIndex<Map<K, V>, K, V> = object : MapFilterIndexInstance<K, V> {}
}

interface ListFilterIndexInstance<A> : FilterIndex<List<A>, Int, A> {
    override fun filter(p: (Int) -> Boolean): Traversal<List<A>, A> = object : Traversal<List<A>, A> {
        override fun <F> modifyF(FA: Applicative<F>, s: List<A>, f: (A) -> Kind<F, A>): Kind<F, List<A>> =
                ListK.traverse().traverse(s.mapIndexed { index, a -> a toT index }.k(), { (a, j) ->
                    if (p(j)) f(a) else FA.pure(a)
                }, FA)
    }
}

object ListFilterIndexInstanceImplicits {
    fun <A> instance(): FilterIndex<List<A>, Int, A> = object : ListFilterIndexInstance<A> {}
}