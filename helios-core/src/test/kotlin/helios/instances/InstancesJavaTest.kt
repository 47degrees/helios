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
        UUIDDecoder.instance.decode(UUIDEncoder.instance.run { sample.encode() }) should beRight(sample)
      }
    }

    "UUID should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        UUIDDecoder.instance.decode(sample) should beLeft()
      }
    }

    "BigDecimal should be encoded and decoded successfully"{
      assertAll(Gen.bigDecimal()) { sample ->
        BigDecimalDecoder.instance.decode(BigDecimalEncoder.instance.run { sample.encode() }) should beRight(sample)
      }
    }

    "BigDecimal should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        BigDecimalDecoder.instance.decode(sample) should beLeft()
      }
    }

    "BigInteger should be encoded and decoded successfully"{
      assertAll(Gen.bigInteger()) { sample ->
        BigIntegerDecoder.instance.decode(BigIntegerEncoder.instance.run { sample.encode() }) should beRight(sample)
      }
    }

    "BigInteger should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        BigIntegerDecoder.instance.decode(sample) should beLeft()
      }
    }

  }
}
