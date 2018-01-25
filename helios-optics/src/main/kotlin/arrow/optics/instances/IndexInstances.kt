package arrow.optics.instances

import arrow.data.ListKW
import arrow.data.ListKWKind
import arrow.data.MapKW
import arrow.data.MapKWKind
import arrow.data.NonEmptyList
import arrow.data.NonEmptyListKind
import arrow.data.ev
import arrow.data.k
import arrow.data.updated
import arrow.instance
import arrow.optics.Optional
import arrow.optics.POptional
import arrow.optics.function.Index
import arrow.syntax.either.left
import arrow.syntax.either.right

@instance(ListKW::class)
interface ListKWIndexInstance<A> : Index<ListKWKind<A>, Int, A> {
    override fun index(i: Int): Optional<ListKWKind<A>, A> = POptional(
            getOrModify = { it.ev().getOrNull(i)?.right() ?: it.ev().left() },
            set = { a -> { l -> l.ev().mapIndexed { index: Int, aa: A -> if (index == i) a else aa }.k() } }
    )
}

@instance(MapKW::class)
interface MapKWIndexInstance<K, V> : Index<MapKWKind<K, V>, K, V> {
    override fun index(i: K): Optional<MapKWKind<K, V>, V> = POptional(
            getOrModify = { it.ev()[i]?.right() ?: it.left() },
            set = { v -> { m -> m.ev().updated(i, v) } }
    )
}

object StringIndexInstance : Index<String, Int, Char> {

    override fun index(i: Int): Optional<String, Char> =
            stringToList compose listToListKW() compose arrow.optics.function.index<ListKW<Char>, Int, Char>().index(i)
}

object StringIndexInstanceImplicits {
    @JvmStatic
    fun instance(): StringIndexInstance = StringIndexInstance
}

@instance(NonEmptyList::class)
interface NonEmptyListIndexInstance<A> : Index<NonEmptyListKind<A>, Int, A> {

    override fun index(i: Int): Optional<NonEmptyListKind<A>, A> = POptional(
            getOrModify = { l ->
                l.ev().all.getOrNull(i)?.right() ?: l.ev().left()
            },
            set = { a ->
                { l ->
                    NonEmptyList.fromListUnsafe(
                            l.ev().all.mapIndexed { index: Int, aa: A -> if (index == i) a else aa }
                    )
                }
            }
    )
}