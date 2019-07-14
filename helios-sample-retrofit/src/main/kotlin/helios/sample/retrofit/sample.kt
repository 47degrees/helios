package helios.sample.retrofit

import helios.retrofit.HeliosConverterFactory
import helios.retrofit.JsonableEvidence
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

interface HttpBinApi {
  @GET("json")
  fun getJson(): Call<Response>
}

fun main() {

  val retrofit = Retrofit.Builder()
    .addConverterFactory(HeliosConverterFactory.create(
      JsonableEvidence(Response::class, Response.encoder(), Response.decoder())
    ))
    .baseUrl("https://httpbin.org/")
    .build()

  val api = retrofit.create(HttpBinApi::class.java)

  val response = api.getJson().execute()

  print(response.body())

}