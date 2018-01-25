package helios.sample

import helios.meta.json

@json data class Company(val name: String, val address: Address, val employees: List<Employee>)
@json data class Address(val city: String, val street: Street)
@json data class Street(val number: Int, val name: String)
@json data class Employee(val name: String)
