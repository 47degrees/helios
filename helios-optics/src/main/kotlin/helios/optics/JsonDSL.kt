package helios.optics

import arrow.optics.*
import arrow.optics.function.index
import helios.core.*
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import helios.typeclasses.decoder
import helios.typeclasses.encoder

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

    operator fun get(i: Int): JsonPath =
            JsonPath(json compose jsonJsArray() compose index(i))

    fun <A> to(DE: Decoder<A>, EN: Encoder<A>): Optional<Json, A> =
            json compose parse(DE, EN)

}

inline fun <reified A> JsonPath.to(EN: Encoder<A> = encoder(), DE: Decoder<A> = decoder()): Optional<Json, A> =
        json compose parse(DE, EN)

/**
 * Unsafe optic: needs some investigation because it is required to extract reasonable typed values from Json.
 * https://github.com/circe/circe/blob/master/modules/optics/src/main/scala/io/circe/optics/JsonPath.scala#L152
 */
@PublishedApi internal fun <A> parse(DE: Decoder<A>, EN: Encoder<A>): Prism<Json, A> = Prism(
        getOrModify = { json -> DE.decode(json).mapLeft { _ -> json } },
        reverseGet = EN::encode
)

@PublishedApi internal inline fun <reified A> parse(EN: Encoder<A> = encoder(), DE: Decoder<A> = decoder()): Prism<Json, A> = parse(DE, EN)