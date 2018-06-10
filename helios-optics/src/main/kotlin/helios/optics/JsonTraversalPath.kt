package helios.optics

import arrow.core.*
import arrow.optics.*
import arrow.optics.typeclasses.*
import helios.core.*
import helios.instances.decoder
import helios.instances.encoder
import helios.typeclasses.*

data class JsonTraversalPath(val json: Traversal<Json, Json>) {

    /**
     * Extract value as [Boolean] from path.
     */
    val boolean: Traversal<Json, Boolean> = json compose Json.jsBoolean compose JsBoolean.iso

    /**
     * Extract value as [CharSequence] from path.
     */
    val charseq: Traversal<Json, CharSequence> = json compose Json.jsString compose JsString.iso

    /**
     * Extract value as [String] from path.
     */
    val string: Traversal<Json, String> = extract(String.decoder(), String.encoder())

    /**
     * Extract value as [JsNumber] from path.
     */
    val jsnumber: Traversal<Json, JsNumber> = json compose Json.jsNumber

    /**
     * Extract value as [JsDecimal] from path.
     */
    val decimal: Traversal<Json, String> = jsnumber compose JsNumber.jsDecimal compose JsDecimal.iso

    /**
     * Extract value as [Long] from path.
     */
    val long: Traversal<Json, Long> = jsnumber compose JsNumber.jsLong compose JsLong.iso

    /**
     * Extract value as [Float] from path.
     */
    val float: Traversal<Json, Float> = jsnumber compose JsNumber.jsFloat compose JsFloat.iso

    /**
     * Extract value as [Int] from path.
     */
    val int: Traversal<Json, Int> = jsnumber compose JsNumber.jsInt compose JsInt.iso

    /**
     * Extract [JsArray] as `List<Json>` from path.
     */
    val array: Traversal<Json, List<Json>> = json compose Json.jsArray compose JsArray.iso

    /**
     * Extract [JsObject] as `Map<String, Json>` from path.
     */
    val `object`: Traversal<Json, Map<String, Json>> = json compose Json.jsObject compose JsObject.iso

    /**
     * Extract [JsNull] from path.
     */
    val `null`: Traversal<Json, JsNull> = json compose Json.jsNull

    /**
     * Select field with [name] in [JsObject] from path.
     */
    fun select(name: String) = JsonTraversalPath(json compose Json.jsObject compose JsObject.index().index(name))

    /**
     * Extract field with [name] from [JsObject] from path.
     */
    fun at(field: String): Traversal<Json, Option<Json>> = json compose Json.jsObject compose JsObject.at().at(field)

    /**
     *  Get element at index [i] from [JsArray].
     */
    operator fun get(i: Int) = JsonTraversalPath(json compose Json.jsArray compose JsArray.index().index(i))

    /**
     * Extract [A] from path.
     */
    fun <A> extract(DE: Decoder<A>, EN: Encoder<A>): Traversal<Json, A> =
            json compose parse(DE, EN)

    /**
     * Select field with [name] in [JsObject] and extract as [A] from path.
     */
    fun <A> selectExtract(DE: Decoder<A>, EN: Encoder<A>, name: String): Traversal<Json, A> =
            select(name).extract(DE, EN)

    /**
     * Select every entry in [JsObject] or [JsArray].
     */
    fun every() = JsonTraversalPath(json compose jsonTraversal)

    /**
     * Filter [JsArray] by indices that satisfy the predicate [p].
     */
    fun filterIndex(p: Predicate<Int>) = JsonTraversalPath(array compose ListFilterIndexInstance<Json>().filter(p))

    /**
     * Filter [JsObject] by keys that satisfy the predicate [p].
     */
    fun filterKeys(p: Predicate<String>) = JsonTraversalPath(`object` compose MapFilterIndexInstance<String, Json>().filter(p))

}
