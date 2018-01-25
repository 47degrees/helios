package helios.optics

import arrow.optics.*
import arrow.optics.function.index
import helios.core.*


data class JsonPath(val json: Optional<Json, Json>) {

    companion object {
        val root = JsonPath(Optional.id())
        operator fun invoke() = root
    }

    val boolean: Optional<Json, Boolean> = json compose jsonJsBoolean() compose jsBooleanIso()
    val string: Optional<Json, CharSequence> = json compose jsonJsString() compose jsStringIso()
    val number: Optional<Json, JsNumber> = json compose jsonJsNumber()
    val decimal: Optional<Json, JsDecimal> = number compose jsNumberJsDecimal()
    val long: Optional<Json, JsLong> = number compose jsNumberJsLong()
    val float: Optional<Json, Float> = number compose jsNumberJsFloat() compose jsFloatIso()
    val int: Optional<Json, Int> = number compose jsNumberJsInt() compose jsIntIso()
    val array: Optional<Json, List<Json>> = json compose jsonJsArray() compose jsArrayIso()
    val `object`: Optional<Json, JsObject> = json compose jsonJsObject()
    val `null`: Optional<Json, JsNull> = json compose jsonJsNull()

    fun select(field: String): JsonPath =
        JsonPath(json compose jsonJsObject() compose index(field))

}