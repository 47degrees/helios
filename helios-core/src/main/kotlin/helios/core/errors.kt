package helios.core

import arrow.core.Option
import java.time.format.DateTimeFormatter

sealed class DecodingError {
  data class JsNullDecodingError(val value: Json) : DecodingError()
  data class JsStringDecodingError(val value: Json) : DecodingError()
  data class JsBooleanDecodingError(val value: Json) : DecodingError()
  data class JsNumberDecodingError(val value: Json) : DecodingError()
  data class JsArrayDecodingError(val value: Json) : DecodingError()
  data class JsObjectDecodingError(val value: Json) : DecodingError()
  data class KeyNotFound(val name: String) : DecodingError()
  data class EnumValueNotFound(val value: Json) : DecodingError()
  data class DateDecodingError(val value: Json, val formatter: Option<DateTimeFormatter>) : DecodingError()
  data class ExceptionOnDecoding(
    val value: Json,
    val msg: String,
    val exception: Throwable? = null
  ) : DecodingError()
}