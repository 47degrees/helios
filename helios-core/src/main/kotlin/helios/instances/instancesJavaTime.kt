package helios.instances

import arrow.core.Either
import arrow.core.None
import arrow.core.Try
import arrow.core.some
import helios.core.DateDecodingError
import helios.core.DecodingError
import helios.core.JsString
import helios.core.Json
import helios.syntax.json.asJsStringOrError
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import java.time.*
import java.time.format.DateTimeFormatter

interface InstantEncoderInstance : Encoder<Instant> {

  fun formatter(): DateTimeFormatter

  override fun Instant.encode() = JsString(formatter().format(this))

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT) =
      object : InstantEncoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface InstantDecoderInstance : Decoder<Instant> {
  override fun decode(value: Json): Either<DecodingError, Instant> =
    value.asJsStringOrError {
      Try {
        Instant.parse(it.value)
      }.toEither { DateDecodingError(value, None) }
    }

  companion object {
    operator fun invoke() = object : InstantDecoderInstance {}
  }
}

interface ZonedDateTimeEncoderInstance : Encoder<ZonedDateTime> {

  fun formatter(): DateTimeFormatter

  override fun ZonedDateTime.encode() = JsString(formatter().format(this))

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME) =
      object : ZonedDateTimeEncoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface ZonedDateTimeDecoderInstance : Decoder<ZonedDateTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, ZonedDateTime> =
    value.asJsStringOrError {
      Try { ZonedDateTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME) =
      object : ZonedDateTimeDecoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface LocalDateTimeEncoderInstance : Encoder<LocalDateTime> {

  fun formatter(): DateTimeFormatter

  override fun LocalDateTime.encode() = JsString(formatter().format(this))

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME) =
      object : LocalDateTimeEncoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface LocalDateTimeDecoderInstance : Decoder<LocalDateTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, LocalDateTime> =
    value.asJsStringOrError {
      Try { LocalDateTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME) =
      object : LocalDateTimeDecoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface LocalDateEncoderInstance : Encoder<LocalDate> {

  fun formatter(): DateTimeFormatter

  override fun LocalDate.encode() = JsString(formatter().format(this))

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE) =
      object : LocalDateEncoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface LocalDateDecoderInstance : Decoder<LocalDate> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, LocalDate> =
    value.asJsStringOrError {
      Try { LocalDate.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE) =
      object : LocalDateDecoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface LocalTimeEncoderInstance : Encoder<LocalTime> {

  fun formatter(): DateTimeFormatter

  override fun LocalTime.encode() = JsString(formatter().format(this))

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME) =
      object : LocalTimeEncoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface LocalTimeDecoderInstance : Decoder<LocalTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, LocalTime> =
    value.asJsStringOrError {
      Try { LocalTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME) =
      object : LocalTimeDecoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface OffsetDateTimeEncoderInstance : Encoder<OffsetDateTime> {

  fun formatter(): DateTimeFormatter

  override fun OffsetDateTime.encode() = JsString(formatter().format(this))

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME) =
      object : OffsetDateTimeEncoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface OffsetDateTimeDecoderInstance : Decoder<OffsetDateTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, OffsetDateTime> =
    value.asJsStringOrError {
      Try { OffsetDateTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME) =
      object : OffsetDateTimeDecoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface OffsetTimeEncoderInstance : Encoder<OffsetTime> {

  fun formatter(): DateTimeFormatter

  override fun OffsetTime.encode() = JsString(formatter().format(this))

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_TIME) =
      object : OffsetTimeEncoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface OffsetTimeDecoderInstance : Decoder<OffsetTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, OffsetTime> =
    value.asJsStringOrError {
      Try { OffsetTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    operator fun invoke(formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_TIME) =
      object : OffsetTimeDecoderInstance {
        override fun formatter(): DateTimeFormatter = formatter
      }
  }
}

interface MonthDayEncoderInstance : Encoder<MonthDay> {
  override fun MonthDay.encode() = JsString(this.toString())

  companion object {
    operator fun invoke() = object : MonthDayEncoderInstance {}
  }
}

interface MonthDayDecoderInstance : Decoder<MonthDay> {
  override fun decode(value: Json): Either<DecodingError, MonthDay> =
    value.asJsStringOrError {
      Try { MonthDay.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    operator fun invoke() = object : MonthDayDecoderInstance {}
  }
}

interface YearEncoderInstance : Encoder<Year> {
  override fun Year.encode() = JsString(this.toString())

  companion object {
    operator fun invoke() = object : YearEncoderInstance {}
  }
}

interface YearDecoderInstance : Decoder<Year> {
  override fun decode(value: Json): Either<DecodingError, Year> =
    value.asJsStringOrError {
      Try { Year.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    operator fun invoke() = object : YearDecoderInstance {}
  }
}

interface YearMonthEncoderInstance : Encoder<YearMonth> {
  override fun YearMonth.encode() = JsString(this.toString())

  companion object {
    operator fun invoke() = object : YearMonthEncoderInstance {}
  }
}

interface YearMonthDecoderInstance : Decoder<YearMonth> {
  override fun decode(value: Json): Either<DecodingError, YearMonth> =
    value.asJsStringOrError {
      Try { YearMonth.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    operator fun invoke() = object : YearMonthDecoderInstance {}
  }
}

interface PeriodEncoderInstance : Encoder<Period> {
  override fun Period.encode() = JsString(this.toString())

  companion object {
    operator fun invoke() = object : PeriodEncoderInstance {}
  }
}

interface PeriodDecoderInstance : Decoder<Period> {
  override fun decode(value: Json): Either<DecodingError, Period> =
    value.asJsStringOrError {
      Try { Period.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    operator fun invoke() = object : PeriodDecoderInstance {}
  }
}

interface DurationEncoderInstance : Encoder<Duration> {
  override fun Duration.encode() = JsString(this.toString())

  companion object {
    operator fun invoke() = object : DurationEncoderInstance {}
  }
}

interface DurationDecoderInstance : Decoder<Duration> {
  override fun decode(value: Json): Either<DecodingError, Duration> =
    value.asJsStringOrError {
      Try { Duration.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    operator fun invoke() = object : DurationDecoderInstance {}
  }
}

interface ZoneIdEncoderInstance : Encoder<ZoneId> {
  override fun ZoneId.encode() = JsString(this.toString())

  companion object {
    operator fun invoke() = object : ZoneIdEncoderInstance {}
  }
}

interface ZoneIdDecoderInstance : Decoder<ZoneId> {
  override fun decode(value: Json): Either<DecodingError, ZoneId> =
    value.asJsStringOrError {
      Try { ZoneId.of(it.value.toString()) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    operator fun invoke() = object : ZoneIdDecoderInstance {}
  }
}

interface ZoneOffsetEncoderInstance : Encoder<ZoneOffset> {
  override fun ZoneOffset.encode() = JsString(this.toString())

  companion object {
    operator fun invoke() = object : ZoneOffsetEncoderInstance {}
  }
}

interface ZoneOffsetDecoderInstance : Decoder<ZoneOffset> {
  override fun decode(value: Json): Either<DecodingError, ZoneOffset> =
    value.asJsStringOrError {
      Try { ZoneOffset.of(it.value.toString()) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    operator fun invoke() = object : ZoneOffsetDecoderInstance {}
  }
}

