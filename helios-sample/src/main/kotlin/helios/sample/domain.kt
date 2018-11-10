package helios.sample

import arrow.optics.optics
import helios.meta.json

@json
data class Company(val name: String, val address: Address, val employees: List<Employee>) {
  companion object
}

@json
data class Address(val city: String, val street: Street) {
  companion object
}

@json
data class Street(val number: Int, val name: String) {
  companion object
}

@json
data class Employee(val name: String, val lastName: String) {
  companion object
}

@optics
@json
data class Sibling(val first_name: String, val age: Int) {
  companion object
}