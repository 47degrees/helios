package helios.optics

import arrow.core.Option
import arrow.core.eq
import arrow.syntax.monad.flatten
import arrow.syntax.option.*
import arrow.test.*
import arrow.test.generators.genFunctionAToB
import arrow.test.laws.*
import arrow.typeclasses.Eq
import helios.core.JsArray
import helios.core.JsDecimal
import helios.core.JsFalse
import helios.core.JsFloat
import helios.core.JsLong
import helios.core.JsNull
import helios.core.JsNumber
import helios.core.JsObject
import helios.core.JsString
import helios.optics.JsonPath.Companion.root
import helios.typeclasses.decoder
import helios.typeclasses.encoder

import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.forAll

import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class JsonDSLTest : UnitSpec() {

    init {

        testLaws(PrismLaws.laws(
                prism = parse(),
                aGen = genJson(genStreet()),
                bGen = genStreet(),
                funcGen = genFunctionAToB(genStreet()),
                EQA = Eq.any(),
                EQOptionB = Option.eq(Eq.any())
        ))

        "bool prism" {
            forAll(genJsBoolean()) { jsBool ->
                JsonPath.root.boolean.fix().getOption(jsBool) == jsBool.value.some()
            }

            JsonPath.root.boolean.fix().getOption(JsString("false")) shouldBe none<Boolean>()
        }

        "string prism" {
            forAll(genJsString()) { jsString ->
                JsonPath.root.string.fix().getOption(jsString) == jsString.value.some()
            }

            JsonPath.root.string.fix().getOption(JsFalse) shouldBe none<String>()
        }

        "number prism" {
            forAll(genJsNumber()) { jsNumber ->
                JsonPath.root.number.fix().getOption(jsNumber) == jsNumber.some()
            }

            JsonPath.root.number.fix().getOption(JsFalse) shouldBe none<JsNumber>()
        }

        "decimal prism" {
            forAll(genJsDecimal()) { jsDecimal ->
                JsonPath.root.decimal.fix().getOption(jsDecimal) == jsDecimal.value.some()
            }

            JsonPath.root.decimal.fix().getOption(JsFalse) shouldBe none<JsDecimal>()
        }

        "long prism" {
            forAll(genJsLong()) { jsLong ->
                JsonPath.root.long.fix().getOption(jsLong) == jsLong.value.some()
            }

            JsonPath.root.long.fix().getOption(JsFalse) shouldBe none<JsLong>()
        }

        "float prism" {
            forAll(genJsFloat()) { jsFloat ->
                JsonPath.root.float.fix().getOption(jsFloat) == jsFloat.value.some()
            }

            JsonPath.root.float.fix().getOption(JsFalse) shouldBe none<JsFloat>()
        }

        "int prism" {
            forAll(genJsInt()) { jsInt ->
                JsonPath.root.int.fix().getOption(jsInt) == jsInt.value.some()
            }

            JsonPath.root.int.fix().getOption(JsString("5")) shouldBe none<Int>()
        }

        "array prism" {
            forAll(genJsArray()) { jsArray ->
                JsonPath.root.array.fix().getOption(jsArray) == jsArray.value.some()
            }

            JsonPath.root.array.fix().getOption(JsString("5")) shouldBe none<JsArray>()
        }

        "object prism" {
            forAll(genJsObject()) { jsObj ->
                JsonPath.root.`object`.fix().getOption(jsObj) == jsObj.value.some()
            }

            JsonPath.root.`object`.fix().getOption(JsString("5")) shouldBe none<JsObject>()
        }

        "null prism" {
            forAll(genJsNull()) { jsNull ->
                JsonPath.root.`null`.fix().getOption(jsNull) == jsNull.some()
            }

            JsonPath.root.`null`.fix().getOption(JsString("5")) shouldBe none<JsNull>()
        }

        "at from object" {
            forAll(genJson(genCity())) { cityJson ->
                root.at("streets").fix().getOption(cityJson).flatten() == cityJson["streets"]
            }
        }

        "select from object" {
            forAll(genJson(genCity())) { cityJson ->
                root.select("streets").json.fix().getOption(cityJson) == cityJson["streets"]
            }
        }

        "extract from object" {
            forAll(genJson(genCity())) { cityJson ->
                JsonPath.root.extract<City>(decoder(), encoder()).fix().getOption(cityJson) == decoder<City>().decode(cityJson).toOption()
            }
        }

        "get from array" {
            forAll(genJson(genCity())) { cityJson ->
                JsonPath.root.select("streets")[0].extract<Street>(decoder(), encoder()).fix().getOption(cityJson) ==
                        decoder<City>().decode(cityJson).toOption().flatMap { Option.fromNullable(it.streets.getOrNull(0)) }
            }
        }

    }
}