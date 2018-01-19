package helios.typeclasses

import arrow.TC
import arrow.core.Either
import arrow.typeclass
import helios.core.Json

sealed class DecodingError
data class StringDecodingError(val value: Json) : DecodingError()
data class BooleanDecodingError(val value: Json) : DecodingError()
data class NumberDecodingError(val value: Json) : DecodingError()
data class KeyNotFound(val name: String) : DecodingError()

@typeclass
interface Decoder<out A> : TC {
    fun decode(value: Json): Either<DecodingError, A>
}