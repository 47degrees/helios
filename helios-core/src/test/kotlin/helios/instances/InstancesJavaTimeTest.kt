package helios.instances

import helios.arrow.UnitSpec
import helios.test.generators.jsBoolean
import io.kotest.assertions.arrow.either.beLeft
import io.kotest.assertions.arrow.either.beRight
import io.kotest.properties.Gen
import io.kotest.properties.assertAll
import io.kotest.should
import java.time.*

class InstancesJavaTimeTest : UnitSpec() {
  init {

    "Instant should be encoded and decoded successfully"{
      val now = Instant.now()
      InstantDecoder.instance.decode(InstantEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "Instant should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        InstantDecoder.instance.decode(sample) should beLeft()
      }
    }

    "ZonedDateTime should be encoded and decoded successfully"{
      val now = ZonedDateTime.now()
      ZonedDateTimeDecoder.instance.decode(ZonedDateTimeEncoder.instance.run {
        now.encode()
      }) should beRight(now)
    }

    "ZonedDateTime should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        ZonedDateTimeDecoder.instance.decode(sample) should beLeft()
      }
    }

    "LocalDateTime should be encoded and decoded successfully"{
      val now = LocalDateTime.now()
      LocalDateTimeDecoder.instance.decode(LocalDateTimeEncoder.instance.run {
        now.encode()
      }) should beRight(now)
    }

    "LocalDateTime should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        LocalDateTimeDecoder.instance.decode(sample) should beLeft()
      }
    }

    "LocalDate should be encoded and decoded successfully"{
      val now = LocalDate.now()
      LocalDateDecoder.instance.decode(LocalDateEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "LocalDate should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        LocalDateDecoder.instance.decode(sample) should beLeft()
      }
    }

    "LocalTime should be encoded and decoded successfully"{
      val now = LocalTime.now()
      LocalTimeDecoder.instance.decode(LocalTimeEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "LocalTime should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        LocalTimeDecoder.instance.decode(sample) should beLeft()
      }
    }

    "OffsetDateTime should be encoded and decoded successfully"{
      val now = OffsetDateTime.now()
      OffsetDateTimeDecoder.instance.decode(OffsetDateTimeEncoder.instance.run {
        now.encode()
      }) should beRight(now)
    }

    "OffsetDateTime should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        OffsetDateTimeDecoder.instance.decode(sample) should beLeft()
      }
    }

    "OffsetTime should be encoded and decoded successfully"{
      val now = OffsetTime.now()
      OffsetTimeDecoder.instance.decode(OffsetTimeEncoder.instance.run {
        now.encode()
      }) should beRight(now)
    }

    "OffsetTime should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        OffsetTimeDecoder.instance.decode(sample) should beLeft()
      }
    }

    "MonthDay should be encoded and decoded successfully"{
      val now = MonthDay.now()
      MonthDayDecoder.instance.decode(MonthDayEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "MonthDay should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        MonthDayDecoder.instance.decode(sample) should beLeft()
      }
    }

    "Year should be encoded and decoded successfully"{
      val now = Year.now()
      YearDecoder.instance.decode(YearEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "Year should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        YearDecoder.instance.decode(sample) should beLeft()
      }
    }

    "YearMonth should be encoded and decoded successfully"{
      val now = YearMonth.now()
      YearMonthDecoder.instance.decode(YearMonthEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "YearMonth should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        YearMonthDecoder.instance.decode(sample) should beLeft()
      }
    }

    "Period should be encoded and decoded successfully"{
      val now = Period.ofDays(1)
      PeriodDecoder.instance.decode(PeriodEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "Period should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        PeriodDecoder.instance.decode(sample) should beLeft()
      }
    }

    "Duration should be encoded and decoded successfully"{
      val now = Duration.ofDays(1)
      DurationDecoder.instance.decode(DurationEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "Duration should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        DurationDecoder.instance.decode(sample) should beLeft()
      }
    }

    "ZoneId should be encoded and decoded successfully"{
      val now = ZoneId.systemDefault()
      ZoneIdDecoder.instance.decode(ZoneIdEncoder.instance.run { now.encode() }) should beRight(now)
    }

    "ZoneId should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        ZoneIdDecoder.instance.decode(sample) should beLeft()
      }
    }

    "ZoneOffset should be encoded and decoded successfully"{
      val now = ZoneOffset.UTC
      ZoneOffsetDecoder.instance.decode(ZoneOffsetEncoder.instance.run { now.encode() }) should beRight(
        now
      )
    }

    "ZoneOffset should fail for wrong content"{
      assertAll(Gen.jsBoolean()) { sample ->
        ZoneOffsetDecoder.instance.decode(sample) should beLeft()
      }
    }

  }
}