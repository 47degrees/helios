package helios.instances

import arrow.test.UnitSpec
import helios.test.generators.jsString
import io.kotlintest.assertions.arrow.either.beLeft
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.should
import java.time.*

class InstancesJavaTimeTest : UnitSpec() {
  init {

    "Instant should be encoded and decoded successfully"{
      val now = Instant.now()
      InstantDecoderInstance().decode(InstantEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "Instant should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        InstantDecoderInstance().decode(sample) should beLeft()
      }
    }

    "ZonedDateTime should be encoded and decoded successfully"{
      val now = ZonedDateTime.now()
      ZonedDateTimeDecoderInstance().decode(ZonedDateTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "ZonedDateTime should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ZonedDateTimeDecoderInstance().decode(sample) should beLeft()
      }
    }

    "LocalDateTime should be encoded and decoded successfully"{
      val now = LocalDateTime.now()
      LocalDateTimeDecoderInstance().decode(LocalDateTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "LocalDateTime should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        LocalDateTimeDecoderInstance().decode(sample) should beLeft()
      }
    }

    "LocalDate should be encoded and decoded successfully"{
      val now = LocalDate.now()
      LocalDateDecoderInstance().decode(LocalDateEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "LocalDate should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        LocalDateDecoderInstance().decode(sample) should beLeft()
      }
    }

    "LocalTime should be encoded and decoded successfully"{
      val now = LocalTime.now()
      LocalTimeDecoderInstance().decode(LocalTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "LocalTime should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        LocalTimeDecoderInstance().decode(sample) should beLeft()
      }
    }

    "OffsetDateTime should be encoded and decoded successfully"{
      val now = OffsetDateTime.now()
      OffsetDateTimeDecoderInstance().decode(OffsetDateTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "OffsetDateTime should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        OffsetDateTimeDecoderInstance().decode(sample) should beLeft()
      }
    }

    "OffsetTime should be encoded and decoded successfully"{
      val now = OffsetTime.now()
      OffsetTimeDecoderInstance().decode(OffsetTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "OffsetTime should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        OffsetTimeDecoderInstance().decode(sample) should beLeft()
      }
    }

    "MonthDay should be encoded and decoded successfully"{
      val now = MonthDay.now()
      MonthDayDecoderInstance().decode(MonthDayEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "MonthDay should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        MonthDayDecoderInstance().decode(sample) should beLeft()
      }
    }

    "Year should be encoded and decoded successfully"{
      val now = Year.now()
      YearDecoderInstance().decode(YearEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "Year should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        YearDecoderInstance().decode(sample) should beLeft()
      }
    }

    "YearMonth should be encoded and decoded successfully"{
      val now = YearMonth.now()
      YearMonthDecoderInstance().decode(YearMonthEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "YearMonth should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        YearMonthDecoderInstance().decode(sample) should beLeft()
      }
    }

    "Period should be encoded and decoded successfully"{
      val now = Period.ofDays(1)
      PeriodDecoderInstance().decode(PeriodEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "Period should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        PeriodDecoderInstance().decode(sample) should beLeft()
      }
    }

    "Duration should be encoded and decoded successfully"{
      val now = Duration.ofDays(1)
      DurationDecoderInstance().decode(DurationEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "Duration should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        DurationDecoderInstance().decode(sample) should beLeft()
      }
    }

    "ZoneId should be encoded and decoded successfully"{
      val now = ZoneId.systemDefault()
      ZoneIdDecoderInstance().decode(ZoneIdEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "ZoneId should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ZoneIdDecoderInstance().decode(sample) should beLeft()
      }
    }

    "ZoneOffset should be encoded and decoded successfully"{
      val now = ZoneOffset.UTC
      ZoneOffsetDecoderInstance().decode(ZoneOffsetEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "ZoneOffset should fail for wrong content"{
      assertAll(Gen.jsString()) { sample ->
        ZoneOffsetDecoderInstance().decode(sample) should beLeft()
      }
    }

  }
}