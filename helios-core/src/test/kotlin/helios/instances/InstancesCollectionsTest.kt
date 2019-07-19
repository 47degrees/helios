package helios.instances

import arrow.core.flatMap
import arrow.test.UnitSpec
import helios.core.Json
import helios.test.generators.*
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

    "Array should be encode and decode successfully"{
      assertAll(Gen.array(Gen.alphaStr())) { sample ->
        ArrayDecoderInstance(String.decoder()).decode(ArrayEncoderInstance(String.encoder()).run {
          sample.encode()
        }).map { it.contentEquals(sample) } should beRight(true)
      }
    }

    "DoubleArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.double())) { sample ->
        DoubleArrayDecoderInstance().decode(DoubleArrayEncoderInstance().run {
          sample.toDoubleArray().encode()
        }).map { it.contentEquals(sample.toDoubleArray()) } should beRight(true)
      }
    }

    "FloatArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.float())) { sample ->
        FloatArrayDecoderInstance().decode(FloatArrayEncoderInstance().run {
          sample.toFloatArray().encode()
        }).map { it.contentEquals(sample.toFloatArray()) } should beRight(true)
      }
    }

    "LongArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.long())) { sample ->
        LongArrayDecoderInstance().decode(LongArrayEncoderInstance().run {
          sample.toLongArray().encode()
        }).map { it.contentEquals(sample.toLongArray()) } should beRight(true)
      }
    }

    "IntArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.int())) { sample ->
        IntArrayDecoderInstance().decode(IntArrayEncoderInstance().run {
          sample.toIntArray().encode()
        }).map { it.contentEquals(sample.toIntArray()) } should beRight(true)
      }
    }

    "ShortArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.short())) { sample ->
        ShortArrayDecoderInstance().decode(ShortArrayEncoderInstance().run {
          sample.toShortArray().encode()
        }).map { it.contentEquals(sample.toShortArray()) } should beRight(true)
      }
    }

    "ByteArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.byte())) { sample ->
        ByteArrayDecoderInstance().decode(ByteArrayEncoderInstance().run {
          sample.toByteArray().encode()
        }).map { it.contentEquals(sample.toByteArray()) } should beRight(true)
      }
    }

    "BooleanArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.bool())) { sample ->
        BooleanArrayDecoderInstance().decode(BooleanArrayEncoderInstance().run {
          sample.toBooleanArray().encode()
        }).map { it.contentEquals(sample.toBooleanArray()) } should beRight(true)
      }
    }

    "Map should be encode and decode successfully"{
      assertAll(Gen.map(Gen.alphaStr(), Gen.alphaStr())) { sample ->
        MapDecoderInstance(String.keyDecoder(), String.decoder()).decode(
          MapEncoderInstance(
            String.keyEncoder(),
            String.encoder()
          ).run { sample.encode() }).map { it shouldContainExactly (sample) } should beRight()
      }
    }

  }
}