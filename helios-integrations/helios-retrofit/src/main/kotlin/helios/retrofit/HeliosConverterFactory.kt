package helios.retrofit

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.data.extensions.list.monad.map
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class HeliosConverterFactory private constructor(
  private val decoderInstances: Map<Class<*>, Decoder<*>>,
  private val encoderInstances: Map<Class<*>, Encoder<*>>
) : Converter.Factory() {

  companion object {

    fun create(vararg jsonables: JsonableEvidence<*>): HeliosConverterFactory =
      create(jsonables.toList())

    fun create(jsonables: List<JsonableEvidence<*>>): HeliosConverterFactory {
      val encoderInstances = jsonables.map { (clazz, encoder, _) -> Pair(clazz.java, encoder) }.toMap()
      val decoderInstances = jsonables.map { (clazz, _, decoder) -> Pair(clazz.java, decoder) }.toMap()

      return HeliosConverterFactory(decoderInstances, encoderInstances)
    }
  }

  private fun retrieveDecoderForType(type: Type): Option<Decoder<*>> =
    decoderInstances[type].toOption()

  private fun retrieveEncoderForType(type: Type): Option<Encoder<*>> =
    encoderInstances[type].toOption()

  override fun responseBodyConverter(
    type: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit
  ): Converter<ResponseBody, *>? =
    retrieveDecoderForType(type).map { HeliosResponseBodyConverter(it) }.getOrElse { null }

  override fun requestBodyConverter(
    type: Type,
    parameterAnnotations: Array<Annotation>,
    methodAnnotations: Array<Annotation>,
    retrofit: Retrofit
  ): Converter<*, RequestBody>? =
    retrieveEncoderForType(type).map { HeliosRequestBodyConverter(it) }.getOrElse { null }
}