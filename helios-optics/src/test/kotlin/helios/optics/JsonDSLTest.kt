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

import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe

import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class JsonDSLTest : UnitSpec() {

    init {

        testLaws(PrismLaws.laws(
                prism = parse(),
                aGen = jsonGen(streetGen()),
                bGen = streetGen(),
                funcGen = genFunctionAToB(streetGen()),
                EQA = Eq.any(),
                EQB = Eq.any(),
                EQOptionB = Option.eq(Eq.any())
        ))

        "number prism" {
            JsonPath.root.int.getOption(JsString("5")) shouldBe none<Int>()
            JsonPath.root.int.getOption(JsNumber(5)) shouldBe 5.some()
        }

    }
}