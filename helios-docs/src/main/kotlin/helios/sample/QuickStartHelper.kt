package helios.core

import helios.meta.json

import arrow.*
import arrow.core.*
import helios.core.*
import helios.instances.*
import helios.typeclasses.*
import helios.syntax.json.*
import arrow.instances.either.applicative.*

@json
data class Person(val name: String, val age: Int) {
  companion object
}


