package helios.optics

import arrow.syntax.option.none
import arrow.syntax.option.some
import arrow.test.UnitSpec
import helios.core.JsNumber
import helios.core.JsString
import helios.core.jsonJsNumber
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class JsonDSLTest : UnitSpec() {

    init {

        "number prism" {
            JsonPath.root.int.getOption(JsString("5")) shouldBe none<Int>()
            JsonPath.root.int.getOption(JsNumber(5)) shouldBe 5.some()
        }

    }
}
