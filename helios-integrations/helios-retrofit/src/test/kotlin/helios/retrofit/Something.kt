package helios.retrofit

import helios.json

@json
data class Something(val name: String, val quantity: Int) {
  companion object
}