package helios.sample

import helios.meta.json

@json data class Company(val name: String, val address: Address, val employees: List<Employee>) {
    companion object
}
@json data class Address(val city: String, val street: Street) {
    companion object
}
@json data class Street(val number: Int, val name: String) {
    companion object
}
@json data class Employee(val name: String, val lastName: String) {
    companion object
}
