package helios.retrofit

import arrow.core.flatMap
import helios.core.Json
import helios.typeclasses.Decoder
import okhttp3.ResponseBody
import retrofit2.Converter
import java.nio.ByteBuffer
import java.nio.CharBuffer

class HeliosResponseBodyConverter<T>(private val decoder: Decoder<T>) : Converter<ResponseBody, T> {
  override fun convert(value: ResponseBody): T? = Json
    .parseFromByteBuffer(ByteBuffer.wrap(value.byteStream().readBytes()))
    .flatMap { it.decode(decoder) }
    .fold({ null }, { it })
    .also { value.close() }
}
