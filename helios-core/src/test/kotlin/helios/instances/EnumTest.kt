package helios.instances

import arrow.test.UnitSpec
import helios.core.DecodingError.EnumValueNotFound
import helios.core.DecodingError.JsStringDecodingError
import helios.core.JsInt
import helios.core.JsString
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight

private enum class Foo {
  A
}

internal class EnumTest : UnitSpec() {
  init {

    "Enums should be encoded and decoded successfully" {
      Enum.decoder<Foo>().decode(Enum.encoder<Foo>().run {
        Foo.A.encode()
      }).shouldBeRight(Foo.A)
    }

    "invalid enum value produces the correct error" {
      val decoded = Enum.decoder<Foo>().decode(JsString("B"))
      decoded.shouldBeLeft(EnumValueNotFound(JsString("B")))
    }

    "invalid json produces the correct error" {
      val decoded = Enum.decoder<Foo>().decode(JsInt(1))
      decoded.shouldBeLeft(JsStringDecodingError(JsInt(1)))
    }

  }
}
