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

interface InstantEncoder : Encoder<Instant> {

  fun formatter(): DateTimeFormatter

  override fun Instant.encode() = JsString(formatter().format(this))

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : InstantEncoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance: Encoder<Instant> by lazy { withFormatter(DateTimeFormatter.ISO_INSTANT) }
  }
}

interface InstantDecoder : Decoder<Instant> {
  override fun decode(value: Json): Either<DecodingError, Instant> =
    value.asJsStringOrError {
      Try {
        Instant.parse(it.value)
      }.toEither { DateDecodingError(value, None) }
    }

  companion object {
    val instance by lazy { object : InstantDecoder {} }
  }
}

interface ZonedDateTimeEncoder : Encoder<ZonedDateTime> {

  fun formatter(): DateTimeFormatter

  override fun ZonedDateTime.encode() = JsString(formatter().format(this))

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : ZonedDateTimeEncoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_ZONED_DATE_TIME) }
  }
}

interface ZonedDateTimeDecoder : Decoder<ZonedDateTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, ZonedDateTime> =
    value.asJsStringOrError {
      Try { ZonedDateTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : ZonedDateTimeDecoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_ZONED_DATE_TIME) }
  }
}

interface LocalDateTimeEncoder : Encoder<LocalDateTime> {

  fun formatter(): DateTimeFormatter

  override fun LocalDateTime.encode() = JsString(formatter().format(this))

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : LocalDateTimeEncoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
  }
}

interface LocalDateTimeDecoder : Decoder<LocalDateTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, LocalDateTime> =
    value.asJsStringOrError {
      Try { LocalDateTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : LocalDateTimeDecoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance: Decoder<LocalDateTime> by lazy { withFormatter(DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
  }
}

interface LocalDateEncoder : Encoder<LocalDate> {

  fun formatter(): DateTimeFormatter

  override fun LocalDate.encode() = JsString(formatter().format(this))

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : LocalDateEncoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_LOCAL_DATE) }
  }
}

interface LocalDateDecoder : Decoder<LocalDate> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, LocalDate> =
    value.asJsStringOrError {
      Try { LocalDate.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : LocalDateDecoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance: Decoder<LocalDate> by lazy { withFormatter(DateTimeFormatter.ISO_LOCAL_DATE) }
  }
}

interface LocalTimeEncoder : Encoder<LocalTime> {

  fun formatter(): DateTimeFormatter

  override fun LocalTime.encode() = JsString(formatter().format(this))

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : LocalTimeEncoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance: Encoder<LocalTime> by lazy { withFormatter(DateTimeFormatter.ISO_LOCAL_TIME) }
  }
}

interface LocalTimeDecoder : Decoder<LocalTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, LocalTime> =
    value.asJsStringOrError {
      Try { LocalTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : LocalTimeDecoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_LOCAL_TIME) }
  }
}

interface OffsetDateTimeEncoder : Encoder<OffsetDateTime> {

  fun formatter(): DateTimeFormatter

  override fun OffsetDateTime.encode() = JsString(formatter().format(this))

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : OffsetDateTimeEncoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
  }
}

interface OffsetDateTimeDecoder : Decoder<OffsetDateTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, OffsetDateTime> =
    value.asJsStringOrError {
      Try { OffsetDateTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : OffsetDateTimeDecoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
  }
}

interface OffsetTimeEncoder : Encoder<OffsetTime> {

  fun formatter(): DateTimeFormatter

  override fun OffsetTime.encode() = JsString(formatter().format(this))

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : OffsetTimeEncoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_OFFSET_TIME) }
  }
}

interface OffsetTimeDecoder : Decoder<OffsetTime> {

  fun formatter(): DateTimeFormatter

  override fun decode(value: Json): Either<DecodingError, OffsetTime> =
    value.asJsStringOrError {
      Try { OffsetTime.parse(it.value, formatter()) }.toEither {
        DateDecodingError(value, formatter().some())
      }
    }

  companion object {
    fun withFormatter(formatter: DateTimeFormatter) =
      object : OffsetTimeDecoder {
        override fun formatter(): DateTimeFormatter = formatter
      }

    val instance by lazy { withFormatter(DateTimeFormatter.ISO_OFFSET_TIME) }
  }
}

interface MonthDayEncoder : Encoder<MonthDay> {
  override fun MonthDay.encode() = JsString(this.toString())

  companion object {
    val instance by lazy { object : MonthDayEncoder {} }
  }
}

interface MonthDayDecoder : Decoder<MonthDay> {
  override fun decode(value: Json): Either<DecodingError, MonthDay> =
    value.asJsStringOrError {
      Try { MonthDay.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    val instance by lazy { object : MonthDayDecoder {} }
  }
}

interface YearEncoder : Encoder<Year> {
  override fun Year.encode() = JsString(this.toString())

  companion object {
    val instance by lazy { object : YearEncoder {} }
  }
}

interface YearDecoder : Decoder<Year> {
  override fun decode(value: Json): Either<DecodingError, Year> =
    value.asJsStringOrError {
      Try { Year.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    val instance by lazy { object : YearDecoder {} }
  }
}

interface YearMonthEncoder : Encoder<YearMonth> {
  override fun YearMonth.encode() = JsString(this.toString())

  companion object {
    val instance by lazy { object : YearMonthEncoder {} }
  }
}

interface YearMonthDecoder : Decoder<YearMonth> {
  override fun decode(value: Json): Either<DecodingError, YearMonth> =
    value.asJsStringOrError {
      Try { YearMonth.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    val instance by lazy { object : YearMonthDecoder {} }
  }
}

interface PeriodEncoder : Encoder<Period> {
  override fun Period.encode() = JsString(this.toString())

  companion object {
    val instance by lazy { object : PeriodEncoder {} }
  }
}

interface PeriodDecoder : Decoder<Period> {
  override fun decode(value: Json): Either<DecodingError, Period> =
    value.asJsStringOrError {
      Try { Period.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    val instance by lazy { object : PeriodDecoder {} }
  }
}

interface DurationEncoder : Encoder<Duration> {
  override fun Duration.encode() = JsString(this.toString())

  companion object {
    val instance by lazy { object : DurationEncoder {} }
  }
}

interface DurationDecoder : Decoder<Duration> {
  override fun decode(value: Json): Either<DecodingError, Duration> =
    value.asJsStringOrError {
      Try { Duration.parse(it.value) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    val instance by lazy { object : DurationDecoder {} }
  }
}

interface ZoneIdEncoder : Encoder<ZoneId> {
  override fun ZoneId.encode() = JsString(this.toString())

  companion object {
    val instance by lazy { object : ZoneIdEncoder {} }
  }
}

interface ZoneIdDecoder : Decoder<ZoneId> {
  override fun decode(value: Json): Either<DecodingError, ZoneId> =
    value.asJsStringOrError {
      Try { ZoneId.of(it.value.toString()) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    val instance by lazy { object : ZoneIdDecoder {} }
  }
}

interface ZoneOffsetEncoder : Encoder<ZoneOffset> {
  override fun ZoneOffset.encode() = JsString(this.toString())

  companion object {
    val instance by lazy { object : ZoneOffsetEncoder {} }
  }
}

interface ZoneOffsetDecoder : Decoder<ZoneOffset> {
  override fun decode(value: Json): Either<DecodingError, ZoneOffset> =
    value.asJsStringOrError {
      Try { ZoneOffset.of(it.value.toString()) }.toEither {
        DateDecodingError(value, None)
      }
    }

  companion object {
    val instance by lazy { object : ZoneOffsetDecoder {} }
  }
}

