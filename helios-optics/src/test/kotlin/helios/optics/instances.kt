package helios.optics

import arrow.core.None
import arrow.core.Option
import arrow.core.extensions.option.eq.eq
import helios.arrow.UnitSpec
import helios.arrow.generators.functionAToB
import helios.arrow.generators.option
import helios.arrow.laws.LensLaws
import helios.arrow.laws.OptionalLaws
import helios.arrow.laws.TraversalLaws
import arrow.typeclasses.Eq
import arrow.typeclasses.Monoid
import helios.core.JsArray
import helios.core.JsObject
import helios.core.Json
import helios.instances.jsobject.eq.eq
import helios.instances.json.eq.eq
import helios.optics.jsarray.each.each
import helios.optics.jsarray.index.index
import helios.optics.jsobject.at.at
import helios.optics.jsobject.each.each
import helios.optics.jsobject.index.index
import helios.test.generators.alphaStr
import helios.test.generators.jsArray
import helios.test.generators.jsObject
import helios.test.generators.json
import io.kotest.properties.Gen

class InstancesTest : UnitSpec() {

  init {

    testLaws(
      OptionalLaws.laws(
        optionalGen = Gen.alphaStr().map { JsObject.index().index(it) },
        aGen = Gen.jsObject(),
        bGen = Gen.json(),
        funcGen = Gen.functionAToB(Gen.json()),
        EQA = Eq.any(),
        EQOptionB = Eq.any()
      )
    )

    testLaws(
      OptionalLaws.laws(
        optional = JsArray.index().index(1),
        aGen = Gen.jsArray(),
        bGen = Gen.json(),
        funcGen = Gen.functionAToB(Gen.json()),
        EQA = Eq.any(),
        EQOptionB = Eq.any()
      )
    )

    testLaws(
      TraversalLaws.laws(
        traversal = JsObject.each().each(),
        aGen = Gen.jsObject(),
        bGen = Gen.json(),
        funcGen = Gen.functionAToB(Gen.json()),
        EQA = Eq.any(),
        EQOptionB = Eq.any(),
        EQListB = Eq.any()
      )
    )

    testLaws(
      TraversalLaws.laws(
        traversal = JsArray.each().each(),
        aGen = Gen.jsArray(),
        bGen = Gen.json(),
        funcGen = Gen.functionAToB(Gen.json()),
        EQA = Eq.any(),
        EQOptionB = Eq.any(),
        EQListB = Eq.any()
      )
    )

    testLaws(LensLaws.laws(
      lensGen = Gen.alphaStr().map { JsObject.at().at(it) },
      aGen = Gen.jsObject(),
      bGen = Gen.option(Gen.json()),
      funcGen = Gen.functionAToB(Gen.option(Gen.json())),
      EQA = JsObject.eq(),
      EQB = Option.eq(Json.eq()),
      MB = object : Monoid<Option<Json>> {
        override fun Option<Json>.combine(b: Option<Json>) = flatMap { a -> b.map { a.merge(it) } }
        override fun empty(): Option<Json> = None
      }
    ))

  }
}