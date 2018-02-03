package helios.optics

import arrow.core.Option
import arrow.optics.*
import arrow.optics.typeclasses.At
import arrow.optics.typeclasses.Index
import arrow.optics.typeclasses.at
import arrow.optics.typeclasses.index
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

    fun select(field: String): JsonPath = JsonPath(json compose jsonJsObject() compose Index.index(field))

    fun <A> selectGet(DE: Decoder<A>, EN: Encoder<A>, field: String): Optional<Json, A> =
            json compose jsonJsObject() compose Index.index<JsObject, String, Json>(field) compose parse(DE, EN)

    fun at(field: String): Optional<Json, Option<Json>> =
            json compose jsonJsObject() compose At.at(field)

    operator fun get(i: Int): JsonPath = JsonPath(json compose jsonJsArray() compose Index.index(i))

    fun <A> get(DE: Decoder<A>, EN: Encoder<A>): Optional<Json, A> =
            json compose parse(DE, EN)

}

inline fun <reified A> JsonPath.get(EN: Encoder<A> = encoder(), DE: Decoder<A> = decoder()): Optional<Json, A> = get(DE, EN)

inline fun <reified A> JsonPath.selectGet(field: String, DE: Decoder<A> = decoder(), EN: Encoder<A> = encoder()): Optional<Json, A> = selectGet(DE, EN, field)

/**
 * Unsafe optic: needs some investigation because it is required to extract reasonable typed values from Json.
 * https://github.com/circe/circe/blob/master/modules/optics/src/main/scala/io/circe/optics/JsonPath.scala#L152
 */
@PublishedApi
internal fun <A> parse(DE: Decoder<A>, EN: Encoder<A>): Prism<Json, A> = Prism(
        getOrModify = { json -> DE.decode(json).mapLeft { _ -> json } },
        reverseGet = EN::encode
)

@PublishedApi
internal inline fun <reified A> parse(EN: Encoder<A> = encoder(), DE: Decoder<A> = decoder()): Prism<Json, A> = parse(DE, EN)