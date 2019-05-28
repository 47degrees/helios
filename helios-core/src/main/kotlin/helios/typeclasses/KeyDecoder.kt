package helios.typeclasses

import arrow.core.Either
import arrow.higherkind
import helios.core.DecodingError
import helios.core.Json

@higherkind
interface KeyDecoder<out A> {
  fun keyDecode(value: Json): Either<DecodingError, A>
}