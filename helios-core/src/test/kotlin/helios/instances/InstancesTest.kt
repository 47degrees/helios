package helios.instances

import arrow.test.UnitSpec
import arrow.test.generators.intSmall
import helios.test.generators.alphaStr
import helios.test.generators.byte
import helios.test.generators.short
import helios.test.generators.triple
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.should

class InstancesTest : UnitSpec() {
  init {

    "Double should be encoded and decoded successfully"{
      assertAll(Gen.double()) { sample ->
        Double.decoder().decode(Double.encoder().run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "Float should be encoded and decoded successfully"{
      assertAll(Gen.float()) { sample ->
        Float.decoder().decode(Float.encoder().run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "Long should be encoded and decoded successfully"{
      assertAll(Gen.long()) { sample ->
        Long.decoder().decode(Long.encoder().run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "Int should be encoded and decoded successfully"{
      assertAll(Gen.int()) { sample ->
        Int.decoder().decode(Int.encoder().run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "Short should be encoded and decoded successfully"{
      assertAll(Gen.short()) { sample ->
        Short.decoder().decode(Short.encoder().run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "Byte should be encoded and decoded successfully"{
      assertAll(Gen.byte()) { sample ->
        Byte.decoder().decode(Byte.encoder().run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "Boolean should be encoded and decoded successfully"{
      assertAll(Gen.bool()) { sample ->
        Boolean.decoder().decode(Boolean.encoder().run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "String should be encoded and decoded successfully"{
      assertAll(Gen.alphaStr()) { sample ->
        String.decoder().decode(String.encoder().run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "Pair should be encoded and decoded successfully"{
      assertAll(Gen.pair(Gen.double(), Gen.alphaStr())) { sample ->
        PairDecoderInstance(
          Double.decoder(),
          String.decoder()
        ).decode(PairEncoderInstance(
          Double.encoder(),
          String.encoder()
        ).run { sample.encode() }
        ) should beRight(sample)
      }
    }

    "Triple should be encoded and decoded successfully"{
      assertAll(Gen.triple(Gen.bool(), Gen.intSmall(), Gen.alphaStr())) { sample ->
        TripleDecoderInstance(
          Boolean.decoder(),
          Int.decoder(),
          String.decoder()
        ).decode(TripleEncoderInstance(
          Boolean.encoder(),
          Int.encoder(),
          String.encoder()
        ).run { sample.encode() }
        ) should beRight(sample)
      }
    }
    
  }
}