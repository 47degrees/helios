package helios.instances

import arrow.core.Right
import arrow.data.NonEmptyList
import arrow.test.UnitSpec
import arrow.test.generators.nonEmptyList
import helios.core.JsArray
import helios.test.generators.alphaStr
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.should
import io.kotlintest.shouldBe

class InstancesArrowTest : UnitSpec() {

    init {

        "NonEmptyList should be encode and decode successfully" {
            assertAll(Gen.nonEmptyList(Gen.alphaStr())) { list ->
                NonEmptyList.decoder(String.decoder()).decode(NonEmptyList.encoder(String.encoder()).run { list.encode() }) shouldBe Right(list)
            }
        }

        "NonEmptyList should fail for empty lists" {
            val empty = JsArray(emptyList())
            NonEmptyList.decoder(String.decoder()).decode(empty) should beLeft()
        }

    }

}