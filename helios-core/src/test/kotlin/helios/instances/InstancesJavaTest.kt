package helios.instances

import arrow.test.UnitSpec
import helios.test.generators.bigDecimal
import helios.test.generators.jsString
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.should

class InstancesJavaTest : UnitSpec() {
  init {

    "UUID should be encoded and decoded successfully"{
      assertAll(Gen.uuid()) { sample ->
        UUIDDecoderInstance().decode(UUIDEncoderInstance().run { sample.encode() }) should beRight(sample)
      }
    }

    "UUID should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        UUIDDecoderInstance().decode(sample) should beLeft()
      }
    }

    "BigDecimal should be encoded and decoded successfully"{
      assertAll(Gen.bigDecimal()) { sample ->
        BigDecimalDecoderInstance().decode(BigDecimalEncoderInstance().run { sample.encode() }) should beRight(sample)
      }
    }

    "BigDecimal should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        BigDecimalDecoderInstance().decode(sample) should beLeft()
      }
    }

    "BigInteger should be encoded and decoded successfully"{
      assertAll(Gen.bigInteger()) { sample ->
        BigIntegerDecoderInstance().decode(BigIntegerEncoderInstance().run { sample.encode() }) should beRight(sample)
      }
    }

    "BigInteger should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        BigIntegerDecoderInstance().decode(sample) should beLeft()
      }
    }

  }
}
