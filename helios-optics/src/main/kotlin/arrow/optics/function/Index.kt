package arrow.optics.function

import arrow.TC
import arrow.optics.Iso
import arrow.optics.Optional
import arrow.typeclass

@typeclass
interface Index<S, I, A> : TC {

    fun index(i: I): Optional<S, A>

    companion object {

        fun <S, A, I , B> fromIso(iso: Iso<S, A>, ID: Index<A, I, B>): Index<S, I, B> = object : Index<S, I, B> {
            override fun index(i: I): Optional<S, B> = iso compose ID.index(i)
        }

    }

}

inline fun <reified S, reified I, reified A> index(i: I, ID: Index<S, I, A> = index()): Optional<S, A> = ID.index(i)