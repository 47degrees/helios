package helios.optics

import arrow.core.*
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

import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.forAll

import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class JsonDSLTest : UnitSpec() {

    init {

        testLaws(PrismLaws.laws(
                prism = parse(Street.decoder(), Street.encoder()),
                aGen = genJson(Street.encoder(), genStreet()),
                bGen = genStreet(),
                funcGen = genFunctionAToB(genStreet()),
                EQA = Eq.any(),
                EQOptionB = Option.eq(Eq.any())
        ))

        "bool prism" {
            forAll(genJsBoolean()) { jsBool ->
                JsonPath.root.boolean.getOption(jsBool) == jsBool.value.some()
            }

            JsonPath.root.boolean.getOption(JsString("false")) shouldBe none<Boolean>()
        }

        "string prism" {
            forAll(genJsString()) { jsString ->
                JsonPath.root.string.getOption(jsString) == jsString.value.some()
            }

            JsonPath.root.string.getOption(JsFalse) shouldBe none<String>()
        }

        "number prism" {
            forAll(genJsNumber()) { jsNumber ->
                JsonPath.root.jsnumber.getOption(jsNumber) == jsNumber.some()
            }

            JsonPath.root.jsnumber.getOption(JsFalse) shouldBe none<JsNumber>()
        }

        "decimal prism" {
            forAll(genJsDecimal()) { jsDecimal ->
                JsonPath.root.decimal.getOption(jsDecimal) == jsDecimal.value.some()
            }

            JsonPath.root.decimal.getOption(JsFalse) shouldBe none<JsDecimal>()
        }

        "long prism" {
            forAll(genJsLong()) { jsLong ->
                JsonPath.root.long.getOption(jsLong) == jsLong.value.some()
            }

            JsonPath.root.long.getOption(JsFalse) shouldBe none<JsLong>()
        }

        "float prism" {
            forAll(genJsFloat()) { jsFloat ->
                JsonPath.root.float.getOption(jsFloat) == jsFloat.value.some()
            }

            JsonPath.root.float.getOption(JsFalse) shouldBe none<JsFloat>()
        }

        "int prism" {
            forAll(genJsInt()) { jsInt ->
                JsonPath.root.int.getOption(jsInt) == jsInt.value.some()
            }

            JsonPath.root.int.getOption(JsString("5")) shouldBe none<Int>()
        }

        "array prism" {
            forAll(genJsArray()) { jsArray ->
                JsonPath.root.array.getOption(jsArray) == jsArray.value.some()
            }

            JsonPath.root.array.getOption(JsString("5")) shouldBe none<JsArray>()
        }

        "object prism" {
            forAll(genJsObject()) { jsObj ->
                JsonPath.root.`object`.getOption(jsObj) == jsObj.value.some()
            }

            JsonPath.root.`object`.getOption(JsString("5")) shouldBe none<JsObject>()
        }

        "null prism" {
            forAll(genJsNull()) { jsNull ->
                JsonPath.root.`null`.getOption(jsNull) == jsNull.some()
            }

            JsonPath.root.`null`.getOption(JsString("5")) shouldBe none<JsNull>()
        }

        "at from object" {
            forAll(genJson(City.encoder(), genCity())) { cityJson ->
                root.at("streets").getOption(cityJson).flatMap(::identity) == cityJson["streets"]
            }
        }

        "select from object" {
            forAll(genJson(City.encoder(), genCity())) { cityJson ->
                root.select("streets").json.getOption(cityJson) == cityJson["streets"]
            }
        }

        "extract from object" {
            forAll(genJson(City.encoder(), genCity())) { cityJson ->
                root.extract(City.decoder(), City.encoder()).getOption(cityJson) == City.decoder().decode(cityJson).toOption()
            }
        }

        "get from array" {
            forAll(genJson(City.encoder(), genCity())) { cityJson ->
                JsonPath.root.select("streets")[0].extract(Street.decoder(), Street.encoder()).getOption(cityJson) ==
                        City.decoder().decode(cityJson).toOption().flatMap { Option.fromNullable(it.streets.getOrNull(0)) }
            }
        }

    }
}