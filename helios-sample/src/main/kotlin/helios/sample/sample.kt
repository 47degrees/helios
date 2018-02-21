package helios.sample

import arrow.core.Either
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
      "name": "John doe"
    },
    {
      "name": "Jane doe"
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

    JsonPath.root.dynamic("address.street.name").charseq.getOption(companyJson).let(::println) //Some(Right(b=Functional street))

    JsonPath.root.dynamic("blabla.street.name").charseq.getOption(companyJson).let(::println) //Some(Left(a=PathNotFound(value=blabla)))

    JsonPath.root.dynamic("address.fff.name").charseq.getOption(companyJson).let(::println) //Some(Left(a=PathNotFound(value=fff)))

    JsonPath.root.dynamic("address.street.name").int.getOption(companyJson).let(::println) //None since name is not Int

}
