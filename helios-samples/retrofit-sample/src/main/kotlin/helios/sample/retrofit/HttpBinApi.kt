package helios.sample.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface HttpBinApi {
  @GET("json")
  fun getJson(): Call<Response>
}