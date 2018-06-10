package helios.sample

import arrow.core.Either
import helios.core.Json
import helios.optics.path
import helios.typeclasses.DecodingError

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

    errorOrCompany.fold({
        println("Something went wrong during decoding: $it")
    }, {
        println("Successfully decode the json: $it")
    })

    Json.path.select("name").string.modify(companyJson, String::toUpperCase).let(::println)
    Json.path.name.string.modify(companyJson, String::toUpperCase).let(::println)

    Json.path.select("address").select("street").select("name").string.getOption(companyJson).let(::println)
    Json.path.address.street.name.string.getOption(companyJson).let(::println)

    Json.path.select("employees").every().select("lastName").string
    val employeeLastNames = Json.path.employees.every().lastName.string

    employeeLastNames.modify(companyJson, String::capitalize).let {
        employeeLastNames.getAll(it)
    }.let(::println)

    Json.path.employees.filterIndex { it == 0 }.name.string.getAll(companyJson).let(::println)

    Json.path.employees.every().filterKeys { it == "name" }.string.getAll(companyJson).let(::println)

}
