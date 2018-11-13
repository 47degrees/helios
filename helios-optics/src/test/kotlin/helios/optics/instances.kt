package helios.optics

import arrow.core.None
import arrow.core.Option
import arrow.core.orElse
import arrow.test.UnitSpec
import arrow.test.generators.genFunctionAToB
import arrow.test.generators.genOption
import arrow.test.laws.LensLaws
import arrow.test.laws.OptionalLaws
import arrow.test.laws.TraversalLaws
import arrow.typeclasses.Eq
import arrow.typeclasses.Monoid
import helios.core.*
import helios.optics.jsarray.each.each
import helios.optics.jsarray.index.index
import helios.optics.jsobject.at.at
import helios.optics.jsobject.each.each
import helios.optics.jsobject.index.index
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.properties.Gen
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class InstancesTest : UnitSpec() {

    init {

        testLaws(OptionalLaws.laws(
                optional = JsObject.index().index(Gen.string().generate()),
                aGen = Gen.jsObject(),
                bGen = Gen.json(),
                funcGen = genFunctionAToB(Gen.json()),
                EQA = Eq.any(),
                EQOptionB = Eq.any()
        ))

        testLaws(OptionalLaws.laws(
                optional = JsArray.index().index(1),
                aGen = Gen.jsArray(),
                bGen = Gen.json(),
                funcGen = genFunctionAToB(Gen.json()),
                EQA = Eq.any(),
                EQOptionB = Eq.any()
        ))

        testLaws(TraversalLaws.laws(
                traversal = JsObject.each().each(),
                aGen = Gen.jsObject(),
                bGen = Gen.json(),
                funcGen = genFunctionAToB(Gen.json()),
                EQA = Eq.any(),
                EQOptionB = Eq.any(),
                EQListB = Eq.any()
        ))

        testLaws(TraversalLaws.laws(
                traversal = JsArray.each().each(),
                aGen = Gen.jsArray(),
                bGen = Gen.json(),
                funcGen = genFunctionAToB(Gen.json()),
                EQA = Eq.any(),
                EQOptionB = Eq.any(),
                EQListB = Eq.any()
        ))

        testLaws(LensLaws.laws(
                lens = JsObject.at().at(Gen.string().generate()),
                aGen = Gen.jsObject(),
                bGen = genOption(Gen.json()),
                funcGen = genFunctionAToB(genOption(Gen.json())),
                EQA = Eq.any(),
                EQB = Eq.any(),
                MB = object : Monoid<Option<Json>> {
                    override fun Option<Json>.combine(b: Option<Json>) = orElse { b }
                    override fun empty(): Option<Json> = None
                }
        ))
    }
}