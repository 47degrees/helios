package helios.optics

import arrow.core.Option
import arrow.core.extensions.option.eq.eq
import arrow.core.identity
import arrow.core.some
import arrow.test.UnitSpec
import arrow.test.generators.functionAToB
import arrow.test.laws.PrismLaws
import arrow.typeclasses.Eq
import helios.core.*
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.assertions.arrow.option.*

class JsonDSLTest : UnitSpec() {

  init {

    testLaws(
      PrismLaws.laws(
        prism = parse(Street.decoder(), Street.encoder()),
        aGen = Gen.json(genStreet(), Street.encoder()),
        bGen = genStreet(),
        funcGen = Gen.functionAToB(genStreet()),
        EQA = Eq.any(),
        EQOptionB = Option.eq(Eq.any())
      )
    )

    "bool prism" {
      forAll(Gen.jsBoolean()) { jsBool ->
        Json.path.boolean.getOption(jsBool) == jsBool.value.some()
      }

      Json.path.boolean.getOption(JsString("false")).shouldBeNone()
    }

    "string prism" {
      forAll(Gen.jsString()) { jsString ->
        Json.path.string.getOption(jsString) == jsString.value.some()
      }

      Json.path.string.getOption(JsFalse).shouldBeNone()
    }

    "number prism" {
      forAll(Gen.jsNumber()) { jsNumber ->
        Json.path.jsnumber.getOption(jsNumber) == jsNumber.some()
      }

      Json.path.jsnumber.getOption(JsFalse).shouldBeNone()
    }

    "decimal prism" {
      forAll(Gen.jsDecimal()) { jsDecimal ->
        Json.path.decimal.getOption(jsDecimal) == jsDecimal.value.some()
      }

      Json.path.decimal.getOption(JsFalse).shouldBeNone()
    }

    "long prism" {
      forAll(Gen.jsLong()) { jsLong ->
        Json.path.long.getOption(jsLong) == jsLong.value.some()
      }

      Json.path.long.getOption(JsFalse).shouldBeNone()
    }

    "float prism" {
      forAll(Gen.jsFloat()) { jsFloat ->
        Json.path.float.getOption(jsFloat) == jsFloat.value.some()
      }

      Json.path.float.getOption(JsFalse).shouldBeNone()
    }

    "int prism" {
      forAll(Gen.jsInt()) { jsInt ->
        Json.path.int.getOption(jsInt) == jsInt.value.some()
      }

      Json.path.int.getOption(JsString("5")).shouldBeNone()
    }

    "array prism" {
      forAll(Gen.jsArray()) { jsArray ->
        Json.path.array.getOption(jsArray) == jsArray.value.some()
      }

      Json.path.array.getOption(JsString("5")).shouldBeNone()
    }

    "object prism" {
      forAll(Gen.jsObject()) { jsObj ->
        Json.path.`object`.getOption(jsObj) == jsObj.value.some()
      }

      Json.path.`object`.getOption(JsString("5")).shouldBeNone()
    }

    "null prism" {
      forAll(Gen.jsNull()) { jsNull ->
        Json.path.`null`.getOption(jsNull) == jsNull.some()
      }

      Json.path.`null`.getOption(JsString("5")).shouldBeNone()
    }

    "at from object" {
      forAll(Gen.json(genCity(), City.encoder())) { cityJson ->
        Json.path.at("streets").getOption(cityJson).flatMap(::identity) == cityJson["streets"]
      }
    }

    "select from object" {
      forAll(Gen.json(genCity(), City.encoder())) { cityJson ->
        Json.path.select("streets").getOption(cityJson) == cityJson["streets"]
      }
    }

    "extract from object" {
      forAll(Gen.json(genCity(), City.encoder())) { cityJson ->
        Json.path.extract(
          City.decoder(),
          City.encoder()
        ).getOption(cityJson) == City.decoder().decode(cityJson).toOption()
      }
    }

    "get from array" {
      forAll(Gen.json(genCity(), City.encoder())) { cityJson ->
        Json.path.select("streets")[0].extract(
          Street.decoder(),
          Street.encoder()
        ).getOption(cityJson) == City.decoder().decode(cityJson).toOption().flatMap {
          Option.fromNullable(it.streets.getOrNull(0))
        }
      }
    }

  }
}