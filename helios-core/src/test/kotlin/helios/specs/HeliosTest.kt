package helios.specs

import arrow.core.None
import arrow.syntax.option.some
import arrow.test.UnitSpec
import helios.core.*
import helios.specs.model.Person
import helios.syntax.json.toJson
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class HeliosTest : UnitSpec() {

    val expected = jsObject(sortedMapOf(
            "age" to jsNumber(37),
            "name" to jsString("Raul Raja"),
            "partner" to jsObject(sortedMapOf(
                    "age" to jsNumber(36),
                    "name" to jsString("Tawny"),
                    "partner" to JsNull,
                    "preferences" to jsArray(listOf(jsNumber(3), jsNumber(4), jsNumber(5))),
                    "properties" to jsObject(sortedMapOf("a" to jsString("b")
                    ))
            )),
            "preferences" to jsArray(listOf(jsNumber(1), jsNumber(2), jsNumber(3))),
            "properties" to jsObject(sortedMapOf("a" to jsString("b")))
    ))

    init {
        "helios test" {
            Person(
                    "Raul Raja",
                    37,
                    Person("Tawny", 36, None, listOf(3, 4, 5)).some(),
                    listOf(1, 2, 3)
            ).toJson().toJsonString() shouldBe expected.toJsonString()

            println(expected.toJsonString())
        }
    }
}

