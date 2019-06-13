package helios.retrofit

import arrow.core.Option
import arrow.core.Tuple2
import arrow.core.getOrElse
import arrow.data.extensions.list.monad.map
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class HeliosConverterFactory private constructor(
  private val decoderInstances: Map<String, Decoder<*>>,
  private val encoderInstances: Map<String, Encoder<*>>
) : Converter.Factory() {

  companion object {
    fun create(jsonables: List<Triple<Class<*>, Encoder<*>, Decoder<*>>>): HeliosConverterFactory {
      val encoderInstances = jsonables.map { (clazz, encoder, _) -> Pair(clazz.canonicalName, encoder) }.toMap()
      val decoderInstances = jsonables.map { (clazz, _, decoder) -> Pair(clazz.canonicalName, decoder) }.toMap()

      return HeliosConverterFactory(decoderInstances, encoderInstances)
    }
  }

  private fun retrieveDecoderForType(type: String): Option<Decoder<*>> =
    Option.fromNullable(decoderInstances[type])

  private fun retrieveEncoderForType(type: String): Option<Encoder<*>> =
    Option.fromNullable(encoderInstances[type])

  override fun responseBodyConverter(
    type: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit
  ): Converter<ResponseBody, *>? =
    retrieveDecoderForType(type.typeName).map { HeliosResponseBodyConverter(it) }.getOrElse { null }

  override fun requestBodyConverter(
    type: Type,
    parameterAnnotations: Array<Annotation>,
    methodAnnotations: Array<Annotation>,
    retrofit: Retrofit
  ): Converter<*, RequestBody>? =
    retrieveEncoderForType(type.typeName).map { HeliosRequestBodyConverter(it) }.getOrElse { null }
}