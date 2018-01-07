package helios.syntax.json

import helios.core.Json
import helios.typeclasses.*

inline fun <reified A> A.toJson(): Json =
        encoder<A>().encode(this)