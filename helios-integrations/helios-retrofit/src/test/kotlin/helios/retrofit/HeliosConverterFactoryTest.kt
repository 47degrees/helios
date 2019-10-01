package helios.retrofit

import arrow.core.Try
import arrow.core.Tuple3
import io.kotlintest.Description
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

class HeliosConverterFactoryTest : StringSpec() {

  interface Service {
    @POST("/")
    fun getSomething(@Body something: Something): Call<Something>
  }

  @Rule
  val server = MockWebServer()

  private lateinit var service: Service

  private val jsonables: List<JsonableEvidence<*>> = listOf(
    Tuple3(Something::class, Something.encoder(), Something.decoder())
  )

  override fun beforeTest(description: Description) {
    super.beforeTest(description)

    val retrofit = Retrofit.Builder()
      .baseUrl(server.url("/"))
      .addConverterFactory(HeliosConverterFactory.create(jsonables))
      .build()

    service = retrofit.create(Service::class.java)
  }

  init {
    "Converter can encode body of request if encoder instance is provided to factory" {
      server.enqueue(MockResponse().setBody("""{"name":"Test","quantity":1}"""))

      val call = service.getSomething(Something("value", 100))
      call.execute()

      val request = server.takeRequest()
      request.body.readUtf8() shouldBe """{"name":"value","quantity":100}"""
      request.getHeader("Content-Type") shouldBe "application/json; charset=UTF-8"
    }

    "Converter can decode body of response if decoder instance is provided to factory" {
      server.enqueue(MockResponse().setBody("""{"name":"Test","quantity":1}"""))

      val call = service.getSomething(Something("value", 100)).execute()
      val result = call.body()

      result shouldNotBe null
      result!!.name shouldBe "Test"
      result.quantity shouldBe 1
    }

    "Converter should throw an exception if serialization fails" {
      server.enqueue(MockResponse().setBody("{}"))

      Try {
        service.getSomething(Something("value", 100)).execute().body()
      }.isFailure() shouldBe true
    }

  }

}