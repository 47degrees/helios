package helios.core

import arrow.core.Left
import arrow.core.Right
import arrow.test.UnitSpec
import helios.instances.EnumDecoderInstance
import helios.instances.EnumEncoderInstance
import io.kotlintest.shouldBe

private enum class Foo {
    A
}

internal class EnumTest : UnitSpec() {

    init {
        "helios serialization works both ways" {
            val encoded = with(EnumEncoderInstance<Foo>()) { Foo.A.encode() }
            val decoded = encoded.decode(EnumDecoderInstance<Foo>())
            decoded shouldBe Right(Foo.A)
        }

        "invalid enum value produces the correct error" {
            val decoded = EnumDecoderInstance<Foo>().decode(JsString("B"))
            decoded shouldBe Left(EnumValueNotFound(JsString("B")))
        }

        "invalid json produces the correct error" {
            val decoded = EnumDecoderInstance<Foo>().decode(JsInt(1))
            decoded shouldBe Left(StringDecodingError(JsInt(1)))
        }
    }
}
