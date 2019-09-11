package helios.sample.retrofit

import arrow.fx.IO
import helios.retrofit.HeliosConverterFactory
import helios.retrofit.JsonableEvidence
import retrofit2.Retrofit

object Sample {

  @JvmStatic
  fun main(args: Array<String>) {

    val retrofit = Retrofit.Builder()
      .addConverterFactory(
        HeliosConverterFactory.create(
          JsonableEvidence(Response::class, Response.encoder(), Response.decoder())
        )
      )
      .baseUrl("https://httpbin.org/")
      .build()

    val api = retrofit.create(HttpBinApi::class.java)

    IO {
      val response = api.getJson().execute()
      println(response.body())
    }.unsafeRunSync()
  }

}