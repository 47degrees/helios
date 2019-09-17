package helios.instances

import arrow.test.UnitSpec
import helios.test.generators.*
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.matchers.maps.shouldContainExactly
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.should

class sCollectionsTest : UnitSpec() {

  init {

    "List should be encode and decode successfully"{
      assertAll(Gen.list(Gen.alphaStr())) { sample ->
        ListDecoder(String.decoder()).decode(ListEncoder(String.encoder()).run {
          sample.encode()
        }) should beRight(sample)
      }
    }

    "List should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ListDecoder(String.decoder()).decode(sample) should beLeft()
      }
    }

    "Array should be encode and decode successfully"{
      assertAll(Gen.array(Gen.alphaStr())) { sample ->
        ArrayDecoder(String.decoder()).decode(ArrayEncoder(String.encoder()).run {
          sample.encode()
        }).map { it.contentEquals(sample) } should beRight(true)
      }
    }

    "Array should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ArrayDecoder(String.decoder()).decode(sample) should beLeft()
      }
    }

    "DoubleArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.double())) { sample ->
        DoubleArrayDecoder().decode(DoubleArrayEncoder().run {
          sample.toDoubleArray().encode()
        }).map { it.contentEquals(sample.toDoubleArray()) } should beRight(true)
      }
    }

    "DoubleArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        DoubleArrayDecoder().decode(sample) should beLeft()
      }
    }

    "FloatArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.float())) { sample ->
        FloatArrayDecoder().decode(FloatArrayEncoder().run {
          sample.toFloatArray().encode()
        }).map { it.contentEquals(sample.toFloatArray()) } should beRight(true)
      }
    }

    "FloatArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        FloatArrayDecoder().decode(sample) should beLeft()
      }
    }

    "LongArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.long())) { sample ->
        LongArrayDecoder().decode(LongArrayEncoder().run {
          sample.toLongArray().encode()
        }).map { it.contentEquals(sample.toLongArray()) } should beRight(true)
      }
    }

    "LongArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        LongArrayDecoder().decode(sample) should beLeft()
      }
    }

    "IntArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.int())) { sample ->
        IntArrayDecoder().decode(IntArrayEncoder().run {
          sample.toIntArray().encode()
        }).map { it.contentEquals(sample.toIntArray()) } should beRight(true)
      }
    }

    "IntArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        IntArrayDecoder().decode(sample) should beLeft()
      }
    }

    "ShortArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.short())) { sample ->
        ShortArrayDecoder().decode(ShortArrayEncoder().run {
          sample.toShortArray().encode()
        }).map { it.contentEquals(sample.toShortArray()) } should beRight(true)
      }
    }

    "ShortArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ShortArrayDecoder().decode(sample) should beLeft()
      }
    }

    "ByteArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.byte())) { sample ->
        ByteArrayDecoder().decode(ByteArrayEncoder().run {
          sample.toByteArray().encode()
        }).map { it.contentEquals(sample.toByteArray()) } should beRight(true)
      }
    }

    "ByteArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ByteArrayDecoder().decode(sample) should beLeft()
      }
    }

    "BooleanArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.bool())) { sample ->
        BooleanArrayDecoder().decode(BooleanArrayEncoder().run {
          sample.toBooleanArray().encode()
        }).map { it.contentEquals(sample.toBooleanArray()) } should beRight(true)
      }
    }

    "BooleanArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        BooleanArrayDecoder().decode(sample) should beLeft()
      }
    }

    "Map should be encode and decode successfully"{
      assertAll(Gen.map(Gen.alphaStr(), Gen.alphaStr())) { sample ->
        MapDecoder(String.keyDecoder(), String.decoder()).decode(
          MapEncoder(String.keyEncoder(), String.encoder()).run {
            sample.encode()
          }).map { it shouldContainExactly (sample) } should beRight()
      }
    }

    "Map should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        MapDecoder(String.keyDecoder(), String.decoder()).decode(sample) should beLeft()
      }
    }

  }
}