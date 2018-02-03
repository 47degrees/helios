package helios.optics

import arrow.core.Option
import arrow.core.eq
import arrow.syntax.option.*
import arrow.test.*
import arrow.test.generators.genFunctionAToB
import arrow.test.laws.*
import arrow.typeclasses.Eq
import helios.core.JsNumber
import helios.core.JsString
import helios.core.Json
import helios.typeclasses.decoder

import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe

import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class JsonDSLTest : UnitSpec() {

    init {

        val parseUnsafe: Json = Json.parseUnsafe(json)

        testLaws(PrismLaws.laws(
                prism = parse(),
                aGen = jsonGen(streetGen()),
                bGen = streetGen(),
                funcGen = genFunctionAToB(streetGen()),
                EQA = Eq.any(),
                EQOptionB = Option.eq(Eq.any())
        ))

        "number prism" {
            JsonPath.root.int.getOption(JsString("5")) shouldBe none<Int>()
            JsonPath.root.int.getOption(JsNumber(5)) shouldBe 5.some()
        }

        "get from array" {
            JsonPath.root.select("street")[0].extract<Street>().getOption(parseUnsafe) shouldBe Street("East Main Street").some()
        }

    }
}