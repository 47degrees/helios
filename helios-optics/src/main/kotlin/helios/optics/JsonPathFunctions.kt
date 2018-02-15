package helios.optics

import arrow.core.Option
import arrow.core.Predicate
import helios.core.JsArray
import helios.core.JsDecimal
import helios.core.JsNull
import helios.core.JsNumber
import helios.core.JsObject
import helios.core.Json
import helios.core.jsArrayIso
import helios.core.jsBooleanIso
import helios.core.jsDecimalIso
import helios.core.jsFloatIso
import helios.core.jsIntIso
import helios.core.jsLongIso
import helios.core.jsNumberJsDecimal
import helios.core.jsNumberJsFloat
import helios.core.jsNumberJsInt
import helios.core.jsNumberJsLong
import helios.core.jsObjectIso
import helios.core.jsStringIso
import helios.core.jsonJsArray
import helios.core.jsonJsBoolean
import helios.core.jsonJsNull
import helios.core.jsonJsNumber
import helios.core.jsonJsObject
import helios.core.jsonJsString
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder
import helios.typeclasses.decoder
import helios.typeclasses.encoder

interface JsonPathFunctions<F> {

    val json: Kind4<F, Json, Json, Json, Json>

    /**
     * Extract value as [Boolean] from path.
     */
    val boolean: Kind4<F, Json, Json, Boolean, Boolean>

    /**
     * Extract value as [String] from path.
     */
    val charseq: Kind4<F, Json, Json, CharSequence, CharSequence>

    /**
     * Extract value as [String] from path.
     */
    val string: Kind4<F, Json, Json, String, String>

    /**
     * Extract value as [JsNumber] from path.
     */
    val number: Kind4<F, Json, Json, JsNumber, JsNumber>

    /**
     * Extract value as [JsDecimal] from path.
     */
    val decimal: Kind4<F, Json, Json, String, String>

    /**
     * Extract value as [Long] from path.
     */
    val long: Kind4<F, Json, Json, Long, Long>
    /**
     * Extract value as [Float] from path.
     */
    val float: Kind4<F, Json, Json, Float, Float>

    /**
     * Extract value as [Int] from path.
     */
    val int: Kind4<F, Json, Json, Int, Int>

    /**
     * Extract [JsArray] as `List<Json>` from path.
     */
    val array: Kind4<F, Json, Json, List<Json>, List<Json>>

    /**
     * Extract [JsObject] as `Map<String, Json>` from path.
     */
    val `object`: Kind4<F, Json, Json, Map<String, Json>, Map<String, Json>>

    /**
     * Extract [JsNull] from path.
     */
    val `null`: Kind4<F, Json, Json, JsNull, JsNull>

    /**
     * Select field with [name] in [JsObject] from path.
     */
    fun select(name: String): JsonPathFunctions<F>

    /**
     * Extract field with [name] from [JsObject] from path.
     */
    fun at(field: String): Kind4<F, Json, Json, Option<Json>, Option<Json>>

    /**
     *  Get element at index [i] from [JsArray].
     */
    operator fun get(i: Int): JsonPathFunctions<F>

    /**
     * Extract [A] from path.
     */
    fun <A> extract(DE: Decoder<A>, EN: Encoder<A>): Kind4<F, Json, Json, A, A>

    /**
     * Select field with [name] in [JsObject] and extract as [A] from path.
     */
    fun <A> selectExtract(DE: Decoder<A>, EN: Encoder<A>, name: String): Kind4<F, Json, Json, A, A>

    /**
     * Select every entry in [JsObject] or [JsArray].
     */
    fun every(): JsonTraversalPath

    /**
     * Filter [JsArray] by indices that satisfy the predicate [p].
     */
    fun filterIndex(p: Predicate<Int>): JsonTraversalPath

    /**
     * Filter [JsObject] by keys that satisfy the predicate [p].
     */
    fun filterKeys(p: Predicate<String>): JsonTraversalPath
}