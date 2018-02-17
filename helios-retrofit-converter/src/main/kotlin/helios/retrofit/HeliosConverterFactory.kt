package helios.retrofit

import arrow.InstanceParametrizedType
import arrow.instance
import retrofit2.Converter
import okhttp3.ResponseBody
import helios.core.Json
import retrofit2.Retrofit
import java.lang.reflect.Type
import okhttp3.RequestBody
import helios.typeclasses.Encoder
import okhttp3.MediaType

object HeliosConverterFactory {

    @JvmStatic
    @JvmName("create")
    operator fun invoke() = object : Converter.Factory() {

        override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = HeliosResponseBodyConverter

        override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit): Converter<*, RequestBody>? {
            val encoder: Encoder<*> = instance(InstanceParametrizedType(Encoder::class.java, listOf(type)))
            return heliosRequesBodyConverter(encoder)
        }

    }

}

private object HeliosResponseBodyConverter : Converter<ResponseBody, Json> {
    override fun convert(value: ResponseBody): Json = value.use {
        Json.parseFromString(value.string()).fold({ throw it }, { it })
    }
}

private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")

private fun <T> heliosRequesBodyConverter(encoder: Encoder<T>) = Converter<T, RequestBody> { value ->
    RequestBody.create(MEDIA_TYPE, encoder.encode(value).toJsonString())
}
