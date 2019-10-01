package helios.core

import helios.json

@json
data class Person(val name: String, val age: Int) {
  companion object
}


