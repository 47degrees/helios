package helios.specs

import arrow.core.some
import arrow.test.UnitSpec
import helios.core.JsArray
import helios.core.JsLong
import helios.core.Json
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.properties.map
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class JsNumberTest : UnitSpec() {

  init {

    fun jsLongs(): Gen<JsArray> =
      Gen.list(
        Gen.long().map(::JsLong)
      ).map(::JsArray)

    "helios serialization works both ways" {
      forAll(jsLongs()) { longs ->
        val string = longs.toJsonString()
        Json.parseUnsafe(string).asJsArray() == longs.some()
      }
    }

  }

}