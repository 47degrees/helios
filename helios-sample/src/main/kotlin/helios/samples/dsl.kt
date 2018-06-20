package helios.samples

import arrow.core.*
import helios.core.*
import helios.optics.*
import helios.typeclasses.*

const val companyJsonString = """
{
  "name": "Arrow",
  "address": {
    "city": "Functional Town",
    "street": {
      "number": 1337,
      "name": "Functional street"
    }
  },
  "employees": [
    {
      "name": "John",
      "lastName": "doe"
    },
    {
      "name": "Jane",
      "lastName": "doe"
    }
  ]
}"""

fun main(args: Array<String>) {

  val companyJson: Json = Json.parseUnsafe(companyJsonString)

  val errorOrCompany: Either<DecodingError, Company> = Company.decoder().decode(companyJson)

  errorOrCompany.fold(
    ifLeft = { println("Something went wrong during decoding: $it") },
    ifRight = { println("Successfully decode the json: $it") }
  )

  //Use select to focus in on an optional key
  Json.path.select("name").string.modify(companyJson, String::toUpperCase).let(::println)
  //Or use generated static dsl
  Json.path.name.string.modify(companyJson, String::toUpperCase).let(::println)

  //Use multiple selects to focus in on nested optional keys.
  Json.path.select("address").select("street").select("name").string.getOption(companyJson).let(::println)
  //Or use generated static dsl to focus in on nested optional keys.
  Json.path.address.street.name.string.getOption(companyJson).let(::println)

  //Use every to focus in on every element found at given point in path - here array of employees
  val employeeLastNames = Json.path.employees.every.lastName.string

  employeeLastNames.modify(companyJson, String::capitalize).let {
    employeeLastNames.getAll(it) //Use same optic to query data.
  }.let(::println)

  //Filter array of employees by predicate `it == 0`
  Json.path.employees.filterIndex { it == 0 }.name.string.getAll(companyJson).let(::println)

  //Filter map of json by filtering keys by `it == name`
  Json.path.employees.every.filterKeys { it == "name" }.string.getAll(companyJson).let(::println)

}