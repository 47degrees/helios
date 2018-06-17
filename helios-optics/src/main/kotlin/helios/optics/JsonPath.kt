package helios.optics

import arrow.core.*
import arrow.optics.*
import arrow.optics.dsl.at
import arrow.optics.instances.ListFilterIndexInstance
import arrow.optics.instances.MapFilterIndexInstance
import helios.core.*
import helios.instances.decoder
import helios.instances.encoder
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder

/**
 * [JsonPath] which is the start of any path.
 */
inline val Json.Companion.path: JsonPath get() = JsonPath(Optional.id())

/**
 * [JsonPath] is a Json DSL based on Arrow-Optics (http://arrow-kt.io/docs/optics/iso/).
 *
 * With [JsonPath] you can represent paths/relations within your [Json] and allows for working with [Json] in an elegant way.
 */
data class JsonPath(val json: Optional<Json, Json>) {

    /**
     * Extract value as [Boolean] from path.
     */
    val boolean: Optional<Json, Boolean> = json compose Json.jsBoolean compose JsBoolean.value

    /**
     * Extract value as [CharSequence] from path.
     */
    val charseq: Optional<Json, CharSequence> = json compose Json.jsString compose JsString.value

    /**
     * Extract value as [String] from path.
     */
    val string: Optional<Json, String> = extract(String.decoder(), String.encoder())

    /**
     * Extract value as [JsNumber] from path.
     */
    val jsnumber: Optional<Json, JsNumber> = json compose Json.jsNumber

    /**
     * Extract value as [JsDecimal] from path.
     */
    val decimal: Optional<Json, String> = jsnumber compose JsNumber.jsDecimal compose JsDecimal.value

    /**
     * Extract value as [Long] from path.
     */
    val long: Optional<Json, Long> = jsnumber compose JsNumber.jsLong compose JsLong.value

    /**
     * Extract value as [Float] from path.
     */
    val float: Optional<Json, Float> = jsnumber compose JsNumber.jsFloat compose JsFloat.value

    /**
     * Extract value as [Int] from path.
     */
    val int: Optional<Json, Int> = jsnumber compose JsNumber.jsInt compose JsInt.value

    /**
     * Extract [JsArray] as `List<Json>` from path.
     */
    val array: Optional<Json, List<Json>> = json compose Json.jsArray compose JsArray.value

    /**
     * Extract [JsObject] as `Map<String, Json>` from path.
     */
    val `object`: Optional<Json, Map<String, Json>> = json compose Json.jsObject compose JsObject.value

    /**
     * Extract [JsNull] from path.
     */
    val `null`: Optional<Json, JsNull> = json compose Json.jsNull

    /**
     * Select field with [name] in [JsObject] from path.
     */
    fun select(name: String): JsonPath = JsonPath(json compose Json.jsObject compose JsObject.index().index(name))

    /**
     * Extract field with [name] from [JsObject] from path.
     */
    fun at(field: String): Optional<Json, Option<Json>> = (json compose Json.jsObject).at(JsObject.at(), field)

    /**
     *  Get element at index [i] from [JsArray].
     */
    operator fun get(i: Int): JsonPath = JsonPath(json compose Json.jsArray compose JsArray.index().index(i))

    /**
     * Extract [A] from path.
     */
    fun <A> extract(DE: Decoder<A>, EN: Encoder<A>): Optional<Json, A> =
            json compose parse(DE, EN)

    /**
     * Select field with [name] in [JsObject] and extract as [A] from path.
     */
    fun <A> selectExtract(DE: Decoder<A>, EN: Encoder<A>, name: String): Optional<Json, A> =
            select(name).extract(DE, EN)

    /**
     * Select every entry in [JsObject] or [JsArray].
     */
    fun every() = JsonTraversalPath(json compose Json.traversal())

    /**
     * Filter [JsArray] by indices that satisfy the predicate [p].
     */
    fun filterIndex(p: Predicate<Int>) = JsonTraversalPath(array compose ListFilterIndexInstance<Json>().filter(p))

    /**
     * Filter [JsObject] by keys that satisfy the predicate [p].
     */
    fun filterKeys(p: Predicate<String>) = JsonTraversalPath(`object` compose MapFilterIndexInstance<String, Json>().filter(p))

}

/**
 * Unsafe optic: needs some investigation because it is required to extract reasonable typed values from Json.
 * https://github.com/circe/circe/blob/master/modules/optics/src/main/scala/io/circe/optics/JsonPath.scala#L152
 */
@PublishedApi
internal fun <A> parse(DE: Decoder<A>, EN: Encoder<A>): Prism<Json, A> = Prism(
        getOrModify = { json -> DE.decode(json).mapLeft { _ -> json } },
        reverseGet = { EN.run { it.encode() } }
)
