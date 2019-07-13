package helios.core

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
    }
}
