package helios.typeclasses

import helios.core.JsArray
import helios.core.Json

interface Encoder<in A> {
  fun A.encode(): Json
  fun Collection<A>.toJson(): JsArray = JsArray(map { it.encode() })
}