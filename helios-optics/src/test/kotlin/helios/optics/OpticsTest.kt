package helios.optics

import arrow.core.identity
import helios.arrow.UnitSpec
import helios.core.*
import helios.instances.jsnumber.eq.eq
import helios.instances.json.eq.eq
import io.kotest.assertions.arrow.option.shouldBeNone
import io.kotest.assertions.arrow.option.shouldBeSome
import io.kotest.shouldBe

class OpticsTest : UnitSpec() {

  init {

    "You should be able to extract int from Json" {
      val json = JsObject("age" to JsInt(25))
      Json.path.select("age").int.getOption(json).shouldBeSome(25)
    }

    "You should be able to extract int JsLong if valid Int value" {
      val json = JsObject("age" to JsLong(25))
      Json.path.select("age").int.getOption(json).shouldBeSome(25)
    }

    "You should not be able to extract int JsLong if invalid Int value" {
      val json = JsObject("age" to JsLong(Long.MAX_VALUE))
      Json.path.select("age").int.getOption(json).shouldBeNone()
    }

    "JsInt should be equal to JsLong underlying numbers are equal" {
      JsNumber.eq().run { JsLong(25).eqv(JsInt(25)) shouldBe true }
    }

    "Nested JsInt should be equal to JsLong if underlying numbers are equal" {
      val json = JsObject("age" to JsLong(25))
      val updatedJson = Json.path.select("age").int.modify(json, ::identity)

      Json.eq().run {
        updatedJson.eqv(JsObject("age" to JsLong(25))) shouldBe true
      }
    }

  }

}
