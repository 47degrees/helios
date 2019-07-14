package helios.retrofit

import arrow.core.flatMap
import helios.core.Json
import helios.typeclasses.Decoder
import okhttp3.ResponseBody
import retrofit2.Converter
import java.nio.ByteBuffer

class HeliosResponseBodyConverter<T>(private val decoder: Decoder<T>) : Converter<ResponseBody, T> {

  // TODO: Refactor this block when Arrow's Resource data type is available
  override fun convert(value: ResponseBody): T = value.use { body ->
    Json
      .parseFromByteBuffer(ByteBuffer.wrap(body.byteStream().readBytes()))
      .flatMap { it.decode(decoder) }
      .fold({ throw it }, { it })
  }
}
