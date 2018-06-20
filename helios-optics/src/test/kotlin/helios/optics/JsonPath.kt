package helios.optics

import arrow.core.None
import arrow.core.Some
import arrow.optics.optics
import helios.core.*
import helios.meta.json
import io.kotlintest.matchers.shouldBe
import kotlin.test.*

internal typealias Sample = org.junit.Test

class JsonPathExample {

    @Sample
    fun select() {
        val json = JsObject(
                "age" to JsInt(25),
                "first_name" to JsString("Simon"),
                "last_name" to JsString("Vergauwen")
        )
        val age = Json.path.select("age").int

        age.getOption(json) shouldBe Some(25)
        age.getOption(JsObject("age" to JsString("fifteen"))) shouldBe None
        age.getOption(JsNull) shouldBe None
    }

    @Sample
    fun boolean() {
        val json = JsObject("enabled" to JsTrue)
        val b1 = Json.path.select("enabled").boolean.getOption(json)

        b1 shouldBe Some(true)

        val b2 = Json.path.boolean.getOption(JsFalse)

        b2 shouldBe Some(false)
    }

    @Sample
    fun charseq() {
        val json = JsObject("chars" to JsString("some-content"))
        val s1 = Json.path.select("chars").charseq.getOption(json)

        s1 shouldBe Some("some-content")

        val s2 = Json.path.charseq.getOption(JsFalse)

        s2 shouldBe None
    }

}