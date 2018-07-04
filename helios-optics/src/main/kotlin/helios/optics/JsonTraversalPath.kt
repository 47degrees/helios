@file:JvmName("JsonTraversalPath")

package helios.optics

import arrow.core.*

import arrow.optics.*
import arrow.optics.dsl.at
import arrow.optics.instances.*
import helios.core.*
import helios.instances.decoder
import helios.instances.encoder
import helios.typeclasses.*

/**
 * Extract value as [Boolean] from path.
 */
inline val Traversal<Json, Json>.boolean: Traversal<Json, Boolean> inline get() = this compose Json.jsBoolean compose JsBoolean.value

/**
 * Extract value as [CharSequence] from path.
 */
inline val Traversal<Json, Json>.charseq: Traversal<Json, CharSequence> inline get() = this compose Json.jsString compose JsString.value

/**
 * Extract value as [String] from path.
 */
inline val Traversal<Json, Json>.string: Traversal<Json, String> inline get() = extract(String.decoder(), String.encoder())

/**
 * Extract value as [JsNumber] from path.
 */
inline val Traversal<Json, Json>.jsnumber: Traversal<Json, JsNumber> inline get() = this compose Json.jsNumber

/**
 * Extract value as [JsDecimal] from path.
 */
inline val Traversal<Json, Json>.decimal: Traversal<Json, String> inline get() = jsnumber compose JsNumber.jsDecimal compose JsDecimal.value

/**
 * Extract value as [Long] from path.
 */
inline val Traversal<Json, Json>.long: Traversal<Json, Long> inline get() = jsnumber compose JsNumber.jsLong compose JsLong.value

/**
 * Extract value as [Float] from path.
 */
inline val Traversal<Json, Json>.float: Traversal<Json, Float> inline get() = jsnumber compose JsNumber.jsFloat compose JsFloat.value

/**
 * Extract value as [Int] from path.
 */
inline val Traversal<Json, Json>.int: Traversal<Json, Int> inline get() = jsnumber compose JsNumber.jsInt compose JsInt.value

/**
 * Extract [JsArray] as `List<Json>` from path.
 */
inline val Traversal<Json, Json>.array: Traversal<Json, List<Json>> inline get() = this compose Json.jsArray compose JsArray.value

/**
 * Extract [JsObject] as `Map<String, Json>` from path.
 */
inline val Traversal<Json, Json>.`object`: Traversal<Json, Map<String, Json>> inline get() = this compose Json.jsObject compose JsObject.value

/**
 * Extract [JsNull] from path.
 */
inline val Traversal<Json, Json>.`null`: Traversal<Json, JsNull> inline get() = this compose Json.jsNull

/**
 * Select field with [name] in [JsObject] from path.
 */
fun Traversal<Json, Json>.select(name: String) = this compose Json.jsObject compose JsObject.index().index(name)

/**
 * Extract field with [name] from [JsObject] from path.
 */
fun Traversal<Json, Json>.at(field: String): Traversal<Json, Option<Json>> = (this compose Json.jsObject).at(JsObject.at(), field)

/**
 *  Get element at index [i] from [JsArray].
 */
operator fun Traversal<Json, Json>.get(i: Int) = this compose Json.jsArray compose JsArray.index().index(i)

/**
 * Extract [A] from path.
 */
fun <A> Traversal<Json, Json>.extract(DE: Decoder<A>, EN: Encoder<A>): Traversal<Json, A> =
        this compose parse(DE, EN)

/**
 * Select field with [name] in [JsObject] and extract as [A] from path.
 */
fun <A> Traversal<Json, Json>.selectExtract(DE: Decoder<A>, EN: Encoder<A>, name: String): Traversal<Json, A> =
        select(name).extract(DE, EN)

/**
 * Select every entry in [JsObject] or [JsArray].
 */
inline val Traversal<Json, Json>.every inline get() = this compose Json.traversal()

/**
 * Filter [JsArray] by indices that satisfy the predicate [p].
 */
fun Traversal<Json, Json>.filterIndex(p: Predicate<Int>) = array compose ListFilterIndexInstance<Json>().filter(p)

/**
 * Filter [JsObject] by keys that satisfy the predicate [p].
 */
fun Traversal<Json, Json>.filterKeys(p: Predicate<String>) = `object` compose MapFilterIndexInstance<String, Json>().filter(p)
