package helios.optics

import arrow.core.None
import arrow.core.Option
import arrow.core.orElse
import arrow.optics.typeclasses.index
import arrow.test.UnitSpec
import arrow.test.generators.genFunctionAToB
import arrow.test.generators.genOption
import arrow.test.laws.LensLaws
import arrow.test.laws.OptionalLaws
import arrow.test.laws.TraversalLaws
import arrow.typeclasses.Eq
import arrow.typeclasses.Monoid
import helios.core.JsArray
import helios.core.JsObject
import helios.core.Json
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.properties.Gen
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class InstancesTest : UnitSpec() {

    init {

        "instances can be resolved implicitly" {
            index<JsObject, String, Json>().index("one") shouldNotBe null
            index<JsArray, Int, Json>().index(1) shouldNotBe null
            each<JsObject, Json>().each() shouldNotBe null
            each<JsArray, Json>().each() shouldNotBe null
            at<JsObject, String, Option<Json>>().at("one") shouldNotBe null
        }

        testLaws(OptionalLaws.laws(
                optional = index<JsObject, String, Json>().index(Gen.string().generate()),
                aGen = genJsObject(),
                bGen = genJson(),
                funcGen = genFunctionAToB(genJson()),
                EQA = Eq.any(),
                EQOptionB = Eq.any()
        ))

        testLaws(OptionalLaws.laws(
                optional = index<JsArray, Int, Json>().index(1),
                aGen = genJsArray(),
                bGen = genJson(),
                funcGen = genFunctionAToB(genJson()),
                EQA = Eq.any(),
                EQOptionB = Eq.any()
        ))

        testLaws(TraversalLaws.laws(
                traversal = each<JsObject, Json>().each(),
                aGen = genJsObject(),
                bGen = genJson(),
                funcGen = genFunctionAToB(genJson()),
                EQA = Eq.any(),
                EQOptionB = Eq.any(),
                EQListB = Eq.any()
        ))

        testLaws(TraversalLaws.laws(
                traversal = each<JsArray, Json>().each(),
                aGen = genJsArray(),
                bGen = genJson(),
                funcGen = genFunctionAToB(genJson()),
                EQA = Eq.any(),
                EQOptionB = Eq.any(),
                EQListB = Eq.any()
        ))

        testLaws(LensLaws.laws(
                lens = at<JsObject, String, Option<Json>>().at(Gen.string().generate()),
                aGen = genJsObject(),
                bGen = genOption(genJson()),
                funcGen = genFunctionAToB(genOption(genJson())),
                EQA = Eq.any(),
                EQB = Eq.any(),
                MB = object : Monoid<Option<Json>> {
                    override fun combine(a: Option<Json>, b: Option<Json>) = a.orElse { b }
                    override fun empty(): Option<Json> = None
                }
        ))
    }
}