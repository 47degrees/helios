package helios.core

import arrow.core.some
import helios.arrow.UnitSpec
import helios.instances.jsnumber.eq.eq
import io.kotest.properties.Gen
import io.kotest.properties.list
import io.kotest.properties.long
import io.kotest.properties.forAll
import io.kotest.shouldBe

class JsNumberTest : UnitSpec() {

  init {

    val jsLongs: Gen<JsArray> = Gen.list(Gen.long().map(::JsLong)).map(::JsArray)

    "helios serialization works both ways" {
      forAll(jsLongs) { longs: JsArray ->
        val string = longs.noSpaces()
        Json.parseUnsafe(string).asJsArray() == longs.some()
      }
    }

    "JsNumbers are equal if the underlying numbers are equal" {
      JsNumber.eq().run { JsLong(25).eqv(JsInt(25)) shouldBe true }
      JsNumber.eq().run { JsLong(25).eqv(JsDecimal("25")) shouldBe true }
      JsNumber.eq().run { JsLong(25).eqv(JsDouble(25.toDouble())) shouldBe true }
      JsNumber.eq().run { JsLong(25).eqv(JsFloat(25.toFloat())) shouldBe true }
    }

    "JsNumbers overrides equals so that they are equal if the underlying numbers match" {
      JsLong(25) shouldBe JsInt(25)
      JsLong(25) shouldBe JsDecimal("25")
      JsLong(25) shouldBe JsDouble(25.toDouble())
      JsLong(25) shouldBe JsFloat(25.toFloat())
    }

  }

}