package helios.sample

import arrow.core.Either
import arrow.optics.Traversal
import arrow.optics.modify
import helios.core.Json
import helios.optics.JsonPath
import helios.typeclasses.Decoder
import helios.typeclasses.DecodingError
import helios.typeclasses.decoder

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

    val companyDecoder: Decoder<Company> = decoder()
    val errorOrCompany: Either<DecodingError, Company> = companyDecoder.decode(companyJson)

    errorOrCompany.fold({
        println("Something went wrong during decoding: $it")
    }, {
        println("Successfully decode the json: $it")
    })

    JsonPath.root.select("name").string.modify(companyJson, String::toUpperCase).let(::println)
    JsonPath.root.name.string.modify(companyJson, String::toUpperCase).let(::println)

    JsonPath.root.select("address").select("street").select("name").string.getOption(companyJson).let(::println)
    JsonPath.root.address.street.name.string.getOption(companyJson).let(::println)

    val employeeLastNames: Traversal<Json, String> = JsonPath.root.select("employees").every().select("lastName").extract()

    employeeLastNames.modify(companyJson, String::capitalize).let {
        employeeLastNames.getAll(it)
    }.let(::println)

    JsonPath.root.select("employees").filterIndex { it == 0 }.select("name").extract<String>().getAll(companyJson).let(::println)

    JsonPath.root.select("employees").every().filterKeys { it == "name" }.string.getAll(companyJson).let(::println)

}
