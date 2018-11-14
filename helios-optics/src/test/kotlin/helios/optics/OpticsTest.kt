package helios.optics

import arrow.core.None
import arrow.core.Some
import arrow.core.identity
import arrow.test.UnitSpec
import helios.core.*
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class OpticsTest : UnitSpec() {

  init {

    "You should be able to extract int from Json" {
      val json = JsObject("age" to JsInt(25))
      Json.path.select("age").int.getOption(json) shouldBe Some(25)
    }

    "You should be able to extract int JsLong if valid Int value" {
      val json = JsObject("age" to JsLong(25))
      Json.path.select("age").int.getOption(json) shouldBe Some(25)
    }

    "You should not be able to extract int JsLong if invalid Int value" {
      val json = JsObject("age" to JsLong(Long.MAX_VALUE))
      Json.path.select("age").int.getOption(json) shouldBe None
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
