package helios.optics

import arrow.core.*
import arrow.optics.*
import arrow.optics.typeclasses.*
import helios.core.*
import helios.typeclasses.*

data class JsonTraversalPath(val json: Traversal<Json, Json>) {

    /**
     * Extract value as [Boolean] from path.
     */
    val boolean: Traversal<Json, Boolean> = json compose jsonJsBoolean() compose jsBooleanIso()

    /**
     * Extract value as [String] from path.
     */
    val string: Traversal<Json, CharSequence> = json compose jsonJsString() compose jsStringIso()

    /**
     * Extract value as [JsNumber] from path.
     */
    val number: Traversal<Json, JsNumber> = json compose jsonJsNumber()

    /**
     * Extract value as [JsDecimal] from path.
     */
    val decimal: Traversal<Json, String> = number compose jsNumberJsDecimal() compose jsDecimalIso()

    /**
     * Extract value as [Long] from path.
     */
    val long: Traversal<Json, Long> = number compose jsNumberJsLong() compose jsLongIso()

    /**
     * Extract value as [Float] from path.
     */
    val float: Traversal<Json, Float> = number compose jsNumberJsFloat() compose jsFloatIso()

    /**
     * Extract value as [Int] from path.
     */
    val int: Traversal<Json, Int> = number compose jsNumberJsInt() compose jsIntIso()

    /**
     * Extract [JsArray] as `List<Json>` from path.
     */
    val array: Traversal<Json, List<Json>> = json compose jsonJsArray() compose jsArrayIso()

    /**
     * Extract [JsObject] as `Map<String, Json>` from path.
     */
    val `object`: Traversal<Json, Map<String, Json>> = json compose jsonJsObject() compose jsObjectIso()

    /**
     * Extract [JsNull] from path.
     */
    val `null`: Traversal<Json, JsNull> = json compose jsonJsNull()

    /**
     * Select field with [name] in [JsObject] from path.
     */
    fun select(name: String) = JsonTraversalPath(json compose jsonJsObject() compose Index.index(name))

    /**
     * Extract field with [name] from [JsObject] from path.
     */
    fun at(field: String): Traversal<Json, Option<Json>> = json compose jsonJsObject() compose At.at(field)

    /**
     *  Get element at index [i] from [JsArray].
     */
    operator fun get(i: Int) = JsonTraversalPath(json compose jsonJsArray() compose Index.index(i))

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
    fun every() = JsonTraversalPath(json compose jsonDescendants)

    /**
     * Filter [JsArray] by indices that satisfy the predicate [p].
     */
    fun filterIndex(p: Predicate<Int>) = JsonTraversalPath(array compose FilterIndex.filterIndex(p = p))

    /**
     * Filter [JsObject] by keys that satisfy the predicate [p].
     */
    fun filterKeys(p: Predicate<String>) = JsonTraversalPath(`object` compose FilterIndex.filterIndex(p = p))

}

/**
 * Extract [A] from path.
 */
inline fun <reified A> JsonTraversalPath.extract(EN: Encoder<A> = encoder(), DE: Decoder<A> = decoder()): Traversal<Json, A> = extract(DE, EN)

/**
 * Select field with [name] in [JsObject] and extract as [A] from path.
 */
inline fun <reified A> JsonTraversalPath.selectExtract(name: String, DE: Decoder<A> = decoder(), EN: Encoder<A> = encoder()): Traversal<Json, A> =
        selectExtract(DE, EN, name)