package helios.sample

import arrow.core.Either
import arrow.optics.modify
import helios.core.Json
import helios.optics.JsonPath.Companion.root
import helios.optics.extract
import helios.optics.selectExtract
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
      "name": "John doe"
    },
    {
      "name": "Jane doe"
    }
  ]
}"""

fun main(args: Array<String>) {

    val companyDecoder: Decoder<Company> = decoder()
    val companyJson: Json = Json.parseUnsafe(companyJsonString)
    val errorOrCompany: Either<DecodingError, Company> = companyDecoder.decode(companyJson)

    errorOrCompany.fold({
        println("Something went wrong during decoding: $it")
    }, {
        println("Successfully decode the json: $it")
    })

    root.selectExtract<String>("name").modify(companyJson) { chrs ->
        chrs.toUpperCase()
    }.let(::println)

    root.select("address").select("street").selectExtract<String>("name").getOption(companyJson).let(::println)

    root.dynamic("address.street.name").string.getOption(companyJson).let(::println)

}