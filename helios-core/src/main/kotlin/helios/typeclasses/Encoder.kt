package helios.typeclasses

import arrow.TC
import arrow.typeclass
import helios.core.Json

@typeclass
interface Encoder<in A> : TC {
    fun encode(value: A): Json
}