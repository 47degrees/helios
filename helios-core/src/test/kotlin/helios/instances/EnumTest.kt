package helios.instances

import arrow.test.UnitSpec
import helios.core.EnumValueNotFound
import helios.core.JsInt
import helios.core.JsString
import helios.core.StringDecodingError
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.should

private enum class Foo {
    A
}

internal class EnumTest : UnitSpec() {

    init {
        "Enums should be encoded and decoded successfully" {
            val encoded = with(EnumEncoderInstance<Foo>()) { Foo.A.encode() }
            val decoded = encoded.decode(EnumDecoderInstance<Foo>())
            decoded should beRight(Foo.A)
        }

        "invalid enum value produces the correct error" {
            val decoded = EnumDecoderInstance<Foo>().decode(JsString("B"))
            decoded should beLeft(EnumValueNotFound(JsString("B")))
        }

        "invalid json produces the correct error" {
            val decoded = EnumDecoderInstance<Foo>().decode(JsInt(1))
            decoded should beLeft(StringDecodingError(JsInt(1)))
        }
    }
}
