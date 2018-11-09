@file:JvmName("JsonPath")

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
 * JsonPath is a Json DSL based on Arrow-Optics (http://arrow-kt.io/docs/optics/iso/).
 *
 * With JsonPath you can represent paths/relations within your [Json] and it allows for working with [Json] in a more elegant way.
 */
inline val Json.Companion.path: Optional<Json, Json> inline get() = Optional.id()

/**
 * Extract value as [Boolean] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.boolean: Optional<Json, Boolean> inline get() = this compose Json.jsBoolean compose JsBoolean.value

/**
 * Extract value as [CharSequence] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.charseq: Optional<Json, CharSequence> inline get() = this compose Json.jsString compose JsString.value

/**
 * Extract value as [String] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.string: Optional<Json, String> inline get() = extract(String.decoder(), String.encoder())

/**
 * Extract value as [JsNumber] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.jsnumber: Optional<Json, JsNumber> inline get() = this compose Json.jsNumber

/**
 * Extract value as [JsDecimal] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.decimal: Optional<Json, String> inline get() = jsnumber compose JsNumber.jsDecimal compose JsDecimal.value

/**
 * Extract value as [Long] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.long: Optional<Json, Long> inline get() = jsnumber compose JsNumber.jsLong compose JsLong.value

/**
 * Extract value as [Double] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.double: Optional<Json, Double> inline get() = jsnumber compose JsNumber.jsDouble compose JsDouble.value

/**
 * Extract value as [Float] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.float: Optional<Json, Float> inline get() = jsnumber compose JsNumber.jsFloat compose JsFloat.value

/**
 * Extract value as [Int] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.int: Optional<Json, Int> inline get() = jsnumber compose JsNumber.jsInt compose JsInt.value

/**
 * Extract [JsArray] as `List<Json>` from [Json.Companion.path].
 */
inline val Optional<Json, Json>.array: Optional<Json, List<Json>> inline get() = this compose Json.jsArray compose JsArray.value

/**
 * Extract [JsObject] as `Map<String, Json>` from [Json.Companion.path].
 */
inline val Optional<Json, Json>.`object`: Optional<Json, Map<String, Json>> inline get() = this compose Json.jsObject compose JsObject.value

/**
 * Extract [JsNull] from [Json.Companion.path].
 */
inline val Optional<Json, Json>.`null`: Optional<Json, JsNull> inline get() = this compose Json.jsNull

/**
 * Select field with [name] in [JsObject] from [Json.Companion.path].
 */
fun Optional<Json, Json>.select(name: String): Optional<Json, Json> = this compose Json.jsObject compose JsObject.index().index(name)

/**
 * Select field with [name] in [JsObject] from [Json.Companion.path].
 */
operator fun Optional<Json, Json>.get(name: String): Optional<Json, Json> = this compose Json.jsObject compose JsObject.index().index(name)

/**
 * Extract field with [field] from [JsObject] from [Json.Companion.path].
 */
fun Optional<Json, Json>.at(field: String): Optional<Json, Option<Json>> = (this compose Json.jsObject).at(JsObject.at(), field)

/**
 *  Get element at index [i] from [JsArray].
 */
operator fun Optional<Json, Json>.get(i: Int): Optional<Json, Json> = this compose Json.jsArray compose JsArray.index().index(i)

/**
 * Extract [A] from [Json.Companion.path].
 */
fun <A> Optional<Json, Json>.extract(DE: Decoder<A>, EN: Encoder<A>): Optional<Json, A> =
        this compose parse(DE, EN)

/**
 * Select field with [name] in [JsObject] and extract as [A] from path.
 */
fun <A> Optional<Json, Json>.selectExtract(DE: Decoder<A>, EN: Encoder<A>, name: String): Optional<Json, A> =
        select(name).extract(DE, EN)

/**
 * Select every entry in [JsObject] or [JsArray].
 */
inline val Optional<Json, Json>.every inline get() = this compose Json.traversal()

/**
 * Filter [JsArray] by indices that satisfy the predicate [p].
 */
fun Optional<Json, Json>.filterIndex(p: Predicate<Int>) = array compose ListFilterIndexInstance<Json>().filter(p)

/**
 * Filter [JsObject] by keys that satisfy the predicate [p].
 */
fun Optional<Json, Json>.filterKeys(p: Predicate<String>) = `object` compose MapFilterIndexInstance<String, Json>().filter(p)

/**
 * Unsafe optic: needs some investigation because it is required to extract reasonable typed values from Json.
 * https://github.com/circe/circe/blob/master/modules/optics/src/main/scala/io/circe/optics/JsonPath.scala#L152
 */
@PublishedApi
internal fun <A> parse(DE: Decoder<A>, EN: Encoder<A>): Prism<Json, A> = Prism(
        getOrModify = { json -> DE.decode(json).mapLeft { _ -> json } },
        reverseGet = { EN.run { it.encode() } }
)
