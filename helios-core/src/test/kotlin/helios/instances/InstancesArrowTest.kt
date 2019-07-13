package helios.instances

import arrow.core.Either
import arrow.core.Right
import arrow.data.NonEmptyList
import arrow.test.UnitSpec
import arrow.test.generators.nonEmptyList
import helios.core.Json
import helios.test.generators.alphaStr
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.should

class InstancesArrowTest : UnitSpec() {

    init {

        "NonEmptyList should be encode and decode successfully" {
            forAll(Gen.nonEmptyList(Gen.alphaStr())) { list ->
                NonEmptyList.decoder(String.decoder()).decode(NonEmptyList.encoder(String.encoder()).run { list.encode() }) == Right(list)
            }
        }

        "NonEmptyList should fail for empty lists" {
            val empty = (Json.parseFromString("[]") as Either.Right).b
            NonEmptyList.decoder(String.decoder()).decode(empty) should beLeft()
        }

    }

}