package helios.core

import helios.annotations.json

@json
data class Person(val name: String, val age: Int) {
  companion object
}


