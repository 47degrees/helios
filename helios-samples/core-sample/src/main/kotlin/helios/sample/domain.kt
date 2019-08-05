package helios.sample

import arrow.core.Either
import arrow.core.Option
import arrow.optics.optics
import helios.json
import java.util.*

@json
data class Company(
  val id: UUID,
  val name: String,
  val address: Address,
  val employees: List<Employee>,
  val private: Boolean
) {
  companion object
}

@json
data class Building(val door: String) {
  companion object
}

@json
data class Address(val city: String, val street: Street, val number: Option<Int>, val extra: Building) {
  companion object
}

@json
data class Street(val number: Int, val name: String) {
  companion object
}

typealias MomSide = String
typealias DadSide = String

@json
data class Child(
  val name: String,
  val age: Int,
  val family: Map<UUID, Either<MomSide, DadSide>>
) {
  companion object
}

typealias Wife = String
typealias Husband = String

@json
data class Employee(
  val name: String,
  val lastName: String,
  val married: Either<Wife, Husband>?,
  val childs: Option<List<Child>>
) {
  companion object
}

@optics
@json
data class Sibling(val first_name: String, val age: Int) {
  companion object
}
