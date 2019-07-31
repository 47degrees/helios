package helios.instances

import arrow.core.Either
import arrow.core.Option
import arrow.core.Tuple2
import arrow.core.Tuple3
import arrow.data.NonEmptyList
import arrow.test.UnitSpec
import arrow.test.generators.*
import helios.core.JsArray
import helios.test.generators.alphaStr
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.should

class InstancesArrowTest : UnitSpec() {
  init {

    "Option should be encoded and decoded successfully"{
      assertAll(Gen.option(Gen.alphaStr())) { sample ->
        Option.decoder(String.decoder()).decode(Option.encoder(String.encoder()).run {
          sample.encode()
        }) should beRight(sample)
      }
    }

    "Either should be encoded and decoded successfully"{
      assertAll(Gen.either(Gen.alphaStr(), Gen.double())) { sample ->
        Either.decoder(String.decoder(), Double.decoder()).decode(
          Either.encoder(String.encoder(), Double.encoder()).run {
            sample.encode()
          }) should beRight(sample)
      }
    }

    "Tuple2 should be encoded and decoded successfully"{
      assertAll(Gen.tuple2(Gen.alphaStr(), Gen.double())) { sample ->
        Tuple2.decoder(String.decoder(), Double.decoder()).decode(
          Tuple2.encoder(String.encoder(), Double.encoder()).run {
            sample.encode()
          }) should beRight(sample)
      }
    }

    "Tuple3 should be encoded and decoded successfully"{
      assertAll(Gen.tuple3(Gen.alphaStr(), Gen.double(), Gen.bool())) { sample ->
        Tuple3.decoder(String.decoder(), Double.decoder(), Boolean.decoder()).decode(
          Tuple3.encoder(String.encoder(), Double.encoder(), Boolean.encoder()).run {
            sample.encode()
          }) should beRight(sample)
      }
    }

    "NonEmptyList should be encode and decode successfully" {
      assertAll(Gen.nonEmptyList(Gen.alphaStr())) { list ->
        NonEmptyList.decoder(String.decoder()).decode(NonEmptyList.encoder(String.encoder()).run {
          list.encode()
        }) should beRight(list)
      }
    }

    "NonEmptyList should fail for empty lists" {
      val empty = JsArray(emptyList())
      NonEmptyList.decoder(String.decoder()).decode(empty) should beLeft()
    }

  }
}
