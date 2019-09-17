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
        UUIDDecoder.decode(UUIDEncoder.run { sample.encode() }) should beRight(sample)
      }
    }

    "UUID should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        UUIDDecoder.decode(sample) should beLeft()
      }
    }

    "BigDecimal should be encoded and decoded successfully"{
      assertAll(Gen.bigDecimal()) { sample ->
        bigDecimalDecoder.decode(bigDecimalEncoder.run { sample.encode() }) should beRight(sample)
      }
    }

    "BigDecimal should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        bigDecimalDecoder.decode(sample) should beLeft()
      }
    }

    "BigInteger should be encoded and decoded successfully"{
      assertAll(Gen.bigInteger()) { sample ->
        bigIntegerDecoder.decode(bigIntegerEncoder.run { sample.encode() }) should beRight(sample)
      }
    }

    "BigInteger should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        bigIntegerDecoder.decode(sample) should beLeft()
      }
    }

  }
}
