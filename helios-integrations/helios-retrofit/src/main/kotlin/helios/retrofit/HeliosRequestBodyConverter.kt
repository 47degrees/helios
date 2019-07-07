package helios.retrofit

import helios.typeclasses.Encoder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

class HeliosRequestBodyConverter<T>(private val encoder: Encoder<T>) : Converter<T, RequestBody> {
  companion object {
    private val MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8")
  }

  override fun convert(value: T): RequestBody? {
    val json = encoder.run { value.encode() }
    return RequestBody.create(MEDIA_TYPE, json.toJsonString())
  }
}