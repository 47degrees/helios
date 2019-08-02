package helios.instances

import arrow.test.UnitSpec
import helios.test.generators.*
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.matchers.maps.shouldContainExactly
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.should

class InstancesCollectionsTest : UnitSpec() {

  init {

    "List should be encode and decode successfully"{
      assertAll(Gen.list(Gen.alphaStr())) { sample ->
        ListDecoderInstance(String.decoder()).decode(ListEncoderInstance(String.encoder()).run {
          sample.encode()
        }) should beRight(sample)
      }
    }

    "List should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ListDecoderInstance(String.decoder()).decode(sample) should beLeft()
      }
    }

    "Array should be encode and decode successfully"{
      assertAll(Gen.array(Gen.alphaStr())) { sample ->
        ArrayDecoderInstance(String.decoder()).decode(ArrayEncoderInstance(String.encoder()).run {
          sample.encode()
        }).map { it.contentEquals(sample) } should beRight(true)
      }
    }

    "Array should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ArrayDecoderInstance(String.decoder()).decode(sample) should beLeft()
      }
    }

    "DoubleArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.double())) { sample ->
        DoubleArrayDecoderInstance().decode(DoubleArrayEncoderInstance().run {
          sample.toDoubleArray().encode()
        }).map { it.contentEquals(sample.toDoubleArray()) } should beRight(true)
      }
    }

    "DoubleArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        DoubleArrayDecoderInstance().decode(sample) should beLeft()
      }
    }

    "FloatArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.float())) { sample ->
        FloatArrayDecoderInstance().decode(FloatArrayEncoderInstance().run {
          sample.toFloatArray().encode()
        }).map { it.contentEquals(sample.toFloatArray()) } should beRight(true)
      }
    }

    "FloatArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        FloatArrayDecoderInstance().decode(sample) should beLeft()
      }
    }

    "LongArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.long())) { sample ->
        LongArrayDecoderInstance().decode(LongArrayEncoderInstance().run {
          sample.toLongArray().encode()
        }).map { it.contentEquals(sample.toLongArray()) } should beRight(true)
      }
    }

    "LongArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        LongArrayDecoderInstance().decode(sample) should beLeft()
      }
    }

    "IntArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.int())) { sample ->
        IntArrayDecoderInstance().decode(IntArrayEncoderInstance().run {
          sample.toIntArray().encode()
        }).map { it.contentEquals(sample.toIntArray()) } should beRight(true)
      }
    }

    "IntArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        IntArrayDecoderInstance().decode(sample) should beLeft()
      }
    }

    "ShortArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.short())) { sample ->
        ShortArrayDecoderInstance().decode(ShortArrayEncoderInstance().run {
          sample.toShortArray().encode()
        }).map { it.contentEquals(sample.toShortArray()) } should beRight(true)
      }
    }

    "ShortArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ShortArrayDecoderInstance().decode(sample) should beLeft()
      }
    }

    "ByteArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.byte())) { sample ->
        ByteArrayDecoderInstance().decode(ByteArrayEncoderInstance().run {
          sample.toByteArray().encode()
        }).map { it.contentEquals(sample.toByteArray()) } should beRight(true)
      }
    }

    "ByteArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ByteArrayDecoderInstance().decode(sample) should beLeft()
      }
    }

    "BooleanArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.bool())) { sample ->
        BooleanArrayDecoderInstance().decode(BooleanArrayEncoderInstance().run {
          sample.toBooleanArray().encode()
        }).map { it.contentEquals(sample.toBooleanArray()) } should beRight(true)
      }
    }

    "BooleanArray should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        BooleanArrayDecoderInstance().decode(sample) should beLeft()
      }
    }

    "Map should be encode and decode successfully"{
      assertAll(Gen.map(Gen.alphaStr(), Gen.alphaStr())) { sample ->
        MapDecoderInstance(String.keyDecoder(), String.decoder()).decode(
          MapEncoderInstance(String.keyEncoder(), String.encoder()).run {
            sample.encode()
          }).map { it shouldContainExactly (sample) } should beRight()
      }
    }

    "Map should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        MapDecoderInstance(String.keyDecoder(), String.decoder()).decode(sample) should beLeft()
      }
    }

  }
}