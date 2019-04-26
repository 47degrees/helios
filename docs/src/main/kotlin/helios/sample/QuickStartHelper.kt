package helios.core

import helios.meta.json

@json
data class Person(val name: String, val age: Int) {
  companion object
}


