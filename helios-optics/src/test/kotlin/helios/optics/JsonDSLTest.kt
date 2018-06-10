package helios.optics

import arrow.core.*
import arrow.test.*
import arrow.test.generators.genFunctionAToB
import arrow.test.laws.*
import arrow.typeclasses.Eq
import helios.core.*

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
                Json.path.boolean.getOption(jsBool) == jsBool.value.some()
            }

            Json.path.boolean.getOption(JsString("false")) shouldBe none<Boolean>()
        }

        "string prism" {
            forAll(genJsString()) { jsString ->
                Json.path.string.getOption(jsString) == jsString.value.some()
            }

            Json.path.string.getOption(JsFalse) shouldBe none<String>()
        }

        "number prism" {
            forAll(genJsNumber()) { jsNumber ->
                Json.path.jsnumber.getOption(jsNumber) == jsNumber.some()
            }

            Json.path.jsnumber.getOption(JsFalse) shouldBe none<JsNumber>()
        }

        "decimal prism" {
            forAll(genJsDecimal()) { jsDecimal ->
                Json.path.decimal.getOption(jsDecimal) == jsDecimal.value.some()
            }

            Json.path.decimal.getOption(JsFalse) shouldBe none<JsDecimal>()
        }

        "long prism" {
            forAll(genJsLong()) { jsLong ->
                Json.path.long.getOption(jsLong) == jsLong.value.some()
            }

            Json.path.long.getOption(JsFalse) shouldBe none<JsLong>()
        }

        "float prism" {
            forAll(genJsFloat()) { jsFloat ->
                Json.path.float.getOption(jsFloat) == jsFloat.value.some()
            }

            Json.path.float.getOption(JsFalse) shouldBe none<JsFloat>()
        }

        "int prism" {
            forAll(genJsInt()) { jsInt ->
                Json.path.int.getOption(jsInt) == jsInt.value.some()
            }

            Json.path.int.getOption(JsString("5")) shouldBe none<Int>()
        }

        "array prism" {
            forAll(genJsArray()) { jsArray ->
                Json.path.array.getOption(jsArray) == jsArray.value.some()
            }

            Json.path.array.getOption(JsString("5")) shouldBe none<JsArray>()
        }

        "object prism" {
            forAll(genJsObject()) { jsObj ->
                Json.path.`object`.getOption(jsObj) == jsObj.value.some()
            }

            Json.path.`object`.getOption(JsString("5")) shouldBe none<JsObject>()
        }

        "null prism" {
            forAll(genJsNull()) { jsNull ->
                Json.path.`null`.getOption(jsNull) == jsNull.some()
            }

            Json.path.`null`.getOption(JsString("5")) shouldBe none<JsNull>()
        }

        "at from object" {
            forAll(genJson(City.encoder(), genCity())) { cityJson ->
                Json.path.at("streets").getOption(cityJson).flatMap(::identity) == cityJson["streets"]
            }
        }

        "select from object" {
            forAll(genJson(City.encoder(), genCity())) { cityJson ->
                Json.path.select("streets").json.getOption(cityJson) == cityJson["streets"]
            }
        }

        "extract from object" {
            forAll(genJson(City.encoder(), genCity())) { cityJson ->
                Json.path.extract(City.decoder(), City.encoder()).getOption(cityJson) == City.decoder().decode(cityJson).toOption()
            }
        }

        "get from array" {
            forAll(genJson(City.encoder(), genCity())) { cityJson ->
                Json.path.select("streets")[0].extract(Street.decoder(), Street.encoder()).getOption(cityJson) ==
                        City.decoder().decode(cityJson).toOption().flatMap { Option.fromNullable(it.streets.getOrNull(0)) }
            }
        }

    }
}