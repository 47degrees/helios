package helios.instances

import arrow.test.UnitSpec
import arrow.test.generators.intSmall
import helios.test.generators.*
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.should

class sTest : UnitSpec() {
  init {

    "Double should be encoded and decoded successfully"{
      assertAll(Gen.double()) { sample ->
        Double.decoder().decode(Double.encoder().run { sample.encode() }) should beRight(sample)
      }
    }

    "Double should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Double.decoder().decode(sample) should beLeft()
      }
    }

    "Float should be encoded and decoded successfully"{
      assertAll(Gen.float()) { sample ->
        Float.decoder().decode(Float.encoder().run { sample.encode() }) should beRight(sample)
      }
    }

    "Float should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Float.decoder().decode(sample) should beLeft()
      }
    }

    "Long should be encoded and decoded successfully"{
      assertAll(Gen.long()) { sample ->
        Long.decoder().decode(Long.encoder().run { sample.encode() }) should beRight(sample)
      }
    }

    "Long should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Long.decoder().decode(sample) should beLeft()
      }
    }

    "Int should be encoded and decoded successfully"{
      assertAll(Gen.int()) { sample ->
        Int.decoder().decode(Int.encoder().run { sample.encode() }) should beRight(sample)
      }
    }

    "Int should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Int.decoder().decode(sample) should beLeft()
      }
    }

    "Short should be encoded and decoded successfully"{
      assertAll(Gen.short()) { sample ->
        Short.decoder().decode(Short.encoder().run { sample.encode() }) should beRight(sample)
      }
    }

    "Short should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Short.decoder().decode(sample) should beLeft()
      }
    }

    "Byte should be encoded and decoded successfully"{
      assertAll(Gen.byte()) { sample ->
        Byte.decoder().decode(Byte.encoder().run { sample.encode() }) should beRight(sample)
      }
    }

    "Byte should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Byte.decoder().decode(sample) should beLeft()
      }
    }

    "Boolean should be encoded and decoded successfully"{
      assertAll(Gen.bool()) { sample ->
        Boolean.decoder().decode(Boolean.encoder().run { sample.encode() }) should beRight(sample)
      }
    }

    "Boolean should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        Boolean.decoder().decode(sample) should beLeft()
      }
    }

    "String should be encoded and decoded successfully"{
      assertAll(Gen.alphaStr()) { sample ->
        String.decoder().decode(String.encoder().run { sample.encode() }) should beRight(sample)
      }
    }

    "String should fail for wrong content"{
      assertAll(Gen.jsInt()) { sample ->
        String.decoder().decode(sample) should beLeft()
      }
    }

    "Pair should be encoded and decoded successfully"{
      assertAll(Gen.pair(Gen.double(), Gen.alphaStr())) { sample ->
        PairDecoder(Double.decoder(), String.decoder()).decode(
          PairEncoder(Double.encoder(), String.encoder()).run {
            sample.encode()
          }) should beRight(sample)
      }
    }

    "Pair should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        PairDecoder(Double.decoder(), String.decoder()).decode(sample) should beLeft()
      }
    }

    "Triple should be encoded and decoded successfully"{
      assertAll(Gen.triple(Gen.bool(), Gen.intSmall(), Gen.alphaStr())) { sample ->
        TripleDecoder(Boolean.decoder(), Int.decoder(), String.decoder()).decode(
          TripleEncoder(Boolean.encoder(), Int.encoder(), String.encoder()).run {
            sample.encode()
          }
        ) should beRight(sample)
      }
    }

    "Triple should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        TripleDecoder(Boolean.decoder(), Int.decoder(), String.decoder()).decode(sample) should beLeft()
      }
    }

  }
}