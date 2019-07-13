package helios.instances

import arrow.test.UnitSpec
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.assertions.arrow.either.shouldBeRight
import io.kotlintest.should

import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll

class InstancesCollectionsTest : UnitSpec() {

  inline fun <reified A> Gen.Companion.array(genA: Gen<A>): Gen<Array<A>> = Gen.list(genA).map { it.toTypedArray() }

  fun Gen.Companion.short(): Gen<Short> = choose(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).map { it.toShort() }

  fun Gen.Companion.byte(): Gen<Byte> = choose(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).map { it.toByte() }

  init {

    "List should be encode and decode successfully"{
      assertAll(Gen.list(Gen.string())) { sample ->
        ListDecoderInstance(String.decoder()).decode(ListEncoderInstance(String.encoder()).run {
          sample.encode()
        }).shouldBeRight(sample)
      }
    }

    "Array should be encode and decode successfully"{
      assertAll(Gen.array(Gen.string())) { sample ->
        ArrayDecoderInstance(String.decoder()).decode(ArrayEncoderInstance(String.encoder()).run {
          sample.encode()
        }).shouldBeRight(sample)
      }
    }

    "DoubleArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.double())) { sample ->
        DoubleArrayDecoderInstance().decode(DoubleArrayEncoderInstance().run {
          sample.toDoubleArray().encode()
        }).shouldBeRight(sample)
      }
    }

    "FloatArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.float())) { sample ->
        FloatArrayDecoderInstance().decode(FloatArrayEncoderInstance().run {
          sample.toFloatArray().encode()
        }).shouldBeRight(sample)
      }
    }

    "LongArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.long())) { sample ->
        LongArrayDecoderInstance().decode(LongArrayEncoderInstance().run {
          sample.toLongArray().encode()
        }).shouldBeRight(sample)
      }
    }

    "IntArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.int())) { sample ->
        IntArrayDecoderInstance().decode(IntArrayEncoderInstance().run {
          sample.toIntArray().encode()
        }).shouldBeRight(sample)
      }
    }

    "ShortArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.short())) { sample ->
        ShortArrayDecoderInstance().decode(ShortArrayEncoderInstance().run {
          sample.toShortArray().encode()
        }).shouldBeRight(sample)
      }
    }

    "ByteArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.byte())) { sample ->
        ByteArrayDecoderInstance().decode(ByteArrayEncoderInstance().run {
          sample.toByteArray().encode()
        }).shouldBeRight(sample)
      }
    }

    "BooleanArray should be encode and decode successfully"{
      assertAll(Gen.array(Gen.bool())) { sample ->
        BooleanArrayDecoderInstance().decode(BooleanArrayEncoderInstance().run {
          sample.toBooleanArray().encode()
        }).shouldBeRight(sample)
      }
    }

    "Map should be encode and decode successfully"{
      assertAll(Gen.map(Gen.string(), Gen.string())) { sample ->
        MapDecoderInstance(String.keyDecoder(), String.decoder()).decode(
          MapEncoderInstance(
            String.keyEncoder(),
            String.encoder()
          ).run { sample.encode() }).shouldBeRight(sample)
      }
    }

  }
}