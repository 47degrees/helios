package helios.typeclasses

import helios.core.JsString

interface KeyEncoder<in A> {
  fun A.keyEncode(): JsString
}