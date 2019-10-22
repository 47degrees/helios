package helios.core

import arrow.test.UnitSpec
import helios.instances.json.eq.eq
import io.kotlintest.shouldBe

class JsonOperationTest : UnitSpec() {
    init {
        "add operation adds JsString field" {
            Json.eq().run {
                val actual = JsObject(
                    "latitude" to JsString("123")
                ).add("longitude", JsString("124"))

                val expected = JsObject(
                    "latitude" to JsString("123"),
                    "longitude" to JsString("124")
                )

                actual.eqv(expected) shouldBe true
            }
        }

        "add operation updates existing JsString field" {
            Json.eq().run {
                val actual = JsObject(
                    "latitude" to JsString("123"),
                    "longitude" to JsString("124")
                ).add("longitude", JsString("125"))

                val expected = JsObject(
                    "latitude" to JsString("123"),
                    "longitude" to JsString("125")
                )

                actual.eqv(expected) shouldBe true
            }
        }

        "add operation adds JsObject field" {
            Json.eq().run {
                val actual = JsObject(
                    "latitude" to JsString("123")
                ).add(
                    "longitude", JsObject(
                        "type" to JsString("WGS84"),
                        "value" to JsString("124")
                    )
                )

                val expected = JsObject(
                    mapOf(
                        "latitude" to JsString("123"),
                        "longitude" to JsObject(
                            "type" to JsString("WGS84"),
                            "value" to JsString("124")
                        )
                    )
                )

                actual.eqv(expected) shouldBe true
            }
        }

        "add operation updates existing JsObject field" {
            Json.eq().run {
                val actual = JsObject(
                    "latitude" to JsString("123"),
                    "longitude" to JsObject(
                        "type" to JsString("WGS84"),
                        "value" to JsString("124")
                    )
                ).add(
                    "longitude", JsObject(
                        "type" to JsString("WGS84"),
                        "value" to JsString("125")
                    )
                )

                val expected = JsObject(
                    mapOf(
                        "latitude" to JsString("123"),
                        "longitude" to JsObject(
                            "type" to JsString("WGS84"),
                            "value" to JsString("125")
                        )
                    )
                )

                actual.eqv(expected) shouldBe true
            }
        }

        "merge two objects together" {
            Json.eq().run {
                val a = JsObject(
                    "latitude" to JsString("123"),
                    "longitude" to JsObject(
                        "type" to JsString("WGS84"),
                        "value" to JsString("125")
                    )
                )

                val b = JsObject(
                    "location" to JsString("nyc")
                )

                val actual = a.merge(b)

                val expected = JsObject(
                    mapOf(
                        "latitude" to JsString("123"),
                        "longitude" to JsObject(
                            "type" to JsString("WGS84"),
                            "value" to JsString("125")
                        ),
                        "location" to JsString("nyc")
                    )
                )

                actual.eqv(expected) shouldBe true
            }
        }
    }
}
