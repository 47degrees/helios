package helios.typeclasses

import helios.core.JsArray
import helios.core.Json

interface Encoder<in A> {
  fun A.encode(): Json
  fun Collection<A>.encode(): JsArray = JsArray(map { it.encode() })
}
