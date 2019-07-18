package helios.instances

import arrow.test.UnitSpec
import io.kotlintest.assertions.arrow.either.beRight
import io.kotlintest.should
import java.time.*

class InstancesJavaTimeTest : UnitSpec() {
  init {

    "Instant should be encode and decode successfully"{
      val now = Instant.now()
      InstantDecoderInstance().decode(InstantEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "ZonedDateTime should be encode and decode successfully"{
      val now = ZonedDateTime.now()
      ZonedDateTimeDecoderInstance().decode(ZonedDateTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "LocalDateTime should be encode and decode successfully"{
      val now = LocalDateTime.now()
      LocalDateTimeDecoderInstance().decode(LocalDateTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "LocalDate should be encode and decode successfully"{
      val now = LocalDate.now()
      LocalDateDecoderInstance().decode(LocalDateEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "LocalTime should be encode and decode successfully"{
      val now = LocalTime.now()
      LocalTimeDecoderInstance().decode(LocalTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "OffsetDateTime should be encode and decode successfully"{
      val now = OffsetDateTime.now()
      OffsetDateTimeDecoderInstance().decode(OffsetDateTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "OffsetTime should be encode and decode successfully"{
      val now = OffsetTime.now()
      OffsetTimeDecoderInstance().decode(OffsetTimeEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "MonthDay should be encode and decode successfully"{
      val now = MonthDay.now()
      MonthDayDecoderInstance().decode(MonthDayEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "Year should be encode and decode successfully"{
      val now = Year.now()
      YearDecoderInstance().decode(YearEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "YearMonth should be encode and decode successfully"{
      val now = YearMonth.now()
      YearMonthDecoderInstance().decode(YearMonthEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "Period should be encode and decode successfully"{
      val now = Period.ofDays(1)
      PeriodDecoderInstance().decode(PeriodEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "Duration should be encode and decode successfully"{
      val now = Duration.ofDays(1)
      DurationDecoderInstance().decode(DurationEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "ZoneId should be encode and decode successfully"{
      val now = ZoneId.systemDefault()
      ZoneIdDecoderInstance().decode(ZoneIdEncoderInstance().run { now.encode() }) should beRight(now)
    }

    "ZoneOffset should be encode and decode successfully"{
      val now = ZoneOffset.UTC
      ZoneOffsetDecoderInstance().decode(ZoneOffsetEncoderInstance().run { now.encode() }) should beRight(now)
    }

  }
}