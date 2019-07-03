package helios.retrofit

import arrow.core.Try
import arrow.core.extensions.either.functor.`as`
import arrow.core.flatMap
import arrow.core.getOrElse
import helios.core.Json
import helios.typeclasses.Decoder
import okhttp3.ResponseBody
import retrofit2.Converter
import java.nio.ByteBuffer

class HeliosResponseBodyConverter<T>(private val decoder: Decoder<T>) : Converter<ResponseBody, T> {
  override fun convert(value: ResponseBody): T? = try {
    Json
      .parseFromByteBuffer(ByteBuffer.wrap(value.byteStream().readBytes()))
      .flatMap { it.decode(decoder) }
      .flatMap { Try { value.close() }.toEither().`as`(it) }.getOrElse { null }
  } catch (ex: Exception) {
    null
  }
}
