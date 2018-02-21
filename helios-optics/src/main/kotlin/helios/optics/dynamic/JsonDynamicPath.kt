package helios.optics.dynamic

import arrow.core.Either
import arrow.core.identity
import arrow.optics.Optional
import arrow.optics.modify
import arrow.syntax.either.left
import arrow.syntax.either.right
import helios.core.JsArray
import helios.core.JsBoolean
import helios.core.JsDecimal
import helios.core.JsFloat
import helios.core.JsInt
import helios.core.JsLong
import helios.core.JsNull
import helios.core.JsNumber
import helios.core.JsObject
import helios.core.JsString
import helios.core.Json
import helios.optics.jsArrayPrism
import helios.optics.jsBooleanPrism
import helios.optics.jsCharSeqPrism
import helios.optics.jsDecimalPrism
import helios.optics.jsFloatPrism
import helios.optics.jsIntPrism
import helios.optics.jsLongPrism
import helios.optics.jsNullPrism
import helios.optics.jsNumberPrism
import helios.optics.jsObjectPrism
import helios.optics.jsStringPrism
import helios.typeclasses.Decoder
import helios.typeclasses.Encoder

data class JsonDynamicPath(private val json: Optional<Either<PathNotFound, Json>, Either<PathNotFound, Json>>) {

    /**
     * Extract value as [Boolean] from path.
     */
    fun extractBool(json: Json): Either<JsonPathFailure, Boolean> = extract(json, bool)

    /**
     * Modify value as [Boolean] from path.
     */
    fun modifyBool(json: Json, f: (Boolean) -> Boolean) = modify(json, bool, f)

    /**
     * Set value as [Boolean] from path.
     */
    fun setBool(json: Json, value: Boolean) = set(json, bool, value)

    /**
     * Extract value as [CharSequence] from path.
     */
    fun extractCharSeq(json: Json): Either<JsonPathFailure, CharSequence> = extract(json, charseq)

    /**
     * Modify value as [CharSequence] from path.
     */
    fun modifyCharSeq(json: Json, f: (CharSequence) -> CharSequence) = modify(json, charseq, f)

    /**
     * Set value as [CharSequence] from path.
     */
    fun setCharSeq(json: Json, value: CharSequence) = set(json, charseq, value)

    /**
     * Extract value as [String] from path.
     */
    fun extractString(json: Json): Either<JsonPathFailure, String> = extract(json, string)

    /**
     * Modify value as [String] from path.
     */
    fun modifyString(json: Json, f: (String) -> String) = modify(json, string, f)

    /**
     * Set value as [String] from path.
     */
    fun setString(json: Json, value: String) = set(json, charseq, value)

    /**
     * Extract value as [JsNumber] from path.
     */
    fun extractNumber(json: Json): Either<JsonPathFailure, JsNumber> = extract(json, number)

    /**
     * Modify value as [JsNumber] from path.
     */
    fun modifyNumber(json: Json, f: (JsNumber) -> JsNumber) = modify(json, number, f)

    /**
     * Set value as [JsNumber] from path.
     */
    fun setNumber(json: Json, value: JsNumber) = set(json, number, value)

    /**
     * Extract value as [JsDecimal] from path.
     */
    fun extractDecimal(json: Json): Either<JsonPathFailure, String> = extract(json, decimal)

    /**
     * Modify value as [JsDecimal] from path.
     */
    fun modifyDecimal(json: Json, f: (String) -> String) = modify(json, decimal, f)

    /**
     * Set value as [JsDecimal] from path.
     */
    fun setDecimal(json: Json, value: String) = set(json, decimal, value)

    /**
     * Extract value as [Long] from path.
     */
    fun extractLong(json: Json): Either<JsonPathFailure, Long> = extract(json, long)

    /**
     * Modify value as [Long] from path.
     */
    fun modifyLong(json: Json, f: (Long) -> Long) = modify(json, long, f)

    /**
     * Set value as [Long] from path.
     */
    fun setLong(json: Json, value: Long) = set(json, long, value)

    /**
     * Extract value as [Float] from path.
     */
    fun extractFloat(json: Json): Either<JsonPathFailure, Float> = extract(json, float)

    /**
     * Modify value as [Float] from path.
     */
    fun modifyFloat(json: Json, f: (Float) -> Float) = modify(json, float, f)

    /**
     * Set value as [Float] from path.
     */
    fun setFloat(json: Json, value: Float) = set(json, float, value)

    /**
     * Extract value as [Int] from path.
     */
    fun extractInt(json: Json): Either<JsonPathFailure, Int> = extract(json, int)

    /**
     * Modify value as [Int] from path.
     */
    fun modifyInt(json: Json, f: (Int) -> Int): Either<JsonPathFailure, Json> = modify(json, int, f)

    /**
     * Set value as [Int] from path.
     */
    fun setInt(json: Json, value: Int): Either<JsonPathFailure, Json> = set(json, int, value)

    /**
     * Extract value as [JsArray] from path.
     */
    fun extractArray(json: Json): Either<JsonPathFailure, List<Json>> = extract(json, array)

    /**
     * Modify value as [JsArray] from path.
     */
    fun modifyArray(json: Json, f: (List<Json>) -> List<Json>): Either<JsonPathFailure, Json> = modify(json, array, f)

    /**
     * Set value as [JsArray] from path.
     */
    fun setArray(json: Json, value: List<Json>): Either<JsonPathFailure, Json> = set(json, array, value)

    /**
     * Extract value as [JsObject] from path.
     */
    fun extractObject(json: Json): Either<JsonPathFailure, Map<String, Json>> = extract(json, `object`)

    /**
     * Modify value as [JsObject] from path.
     */
    fun modifyObject(json: Json, f: (Map<String, Json>) -> Map<String, Json>): Either<JsonPathFailure, Json> = modify(json, `object`, f)

    /**
     * Set value as [JsObject] from path.
     */
    fun setObject(json: Json, value: Map<String, Json>): Either<JsonPathFailure, Json> = set(json, `object`, value)

    /**
     * Extract value as [JsNull] from path.
     */
    fun extractNull(json: Json): Either<JsonPathFailure, JsNull> = extract(json, `null`)

    /**
     * Modify value as [JsNull] from path.
     */
    fun modifyNull(json: Json, f: (JsNull) -> JsNull): Either<JsonPathFailure, Json> = modify(json, `null`, f)

    /**
     * Set value as [JsNull] from path.
     */
    fun setNull(json: Json, value: JsNull): Either<JsonPathFailure, Json> = set(json, `null`, value)

    /**
     * Select field with [name] in [JsObject] from path.
     */
    fun select(name: String): JsonDynamicPath = JsonDynamicPath(json compose dynamicIndex.index(name))

    /**
     * Extract value as [A] from path.
     */
    fun <A> extract(jsonValue: Json, EN: Encoder<A>, DE: Decoder<A>): Either<JsonPathFailure, A> =
            extract(jsonValue, rightJson compose json compose jsonAs({ DE.decode(it).toOption() }, EN::encode))

    /**
     * Modify value as [A] from path.
     */
    fun <A> modify(jsonValue: Json, EN: Encoder<A>, DE: Decoder<A>, f: (A) -> A): Either<JsonPathFailure, Json> =
            modify(jsonValue, rightJson compose json compose jsonAs({ DE.decode(it).toOption() }, EN::encode), f)

    /**
     * Set value as [A] from path.
     */
    fun <A> set(jsonValue: Json, a: A, EN: Encoder<A>, DE: Decoder<A>) =
            set(jsonValue, rightJson compose json compose jsonAs({ DE.decode(it).toOption() }, EN::encode), a)

    private val bool: Optional<Json, Either<PathNotFound, Boolean>> = rightJson compose json compose jsonAs(jsBooleanPrism::getOption, ::JsBoolean)
    private val charseq: Optional<Json, Either<PathNotFound, CharSequence>> = rightJson compose json compose jsonAs(jsCharSeqPrism::getOption, ::JsString)
    private val string: Optional<Json, Either<PathNotFound, String>> = rightJson compose json compose jsonAs(jsStringPrism::getOption, ::JsString)
    private val number: Optional<Json, Either<PathNotFound, JsNumber>> = rightJson compose json compose jsonAs(jsNumberPrism::getOption, ::identity)
    private val decimal: Optional<Json, Either<PathNotFound, String>> = rightJson compose json compose jsonAs(jsDecimalPrism::getOption, ::JsDecimal)
    private val long: Optional<Json, Either<PathNotFound, Long>> = rightJson compose json compose jsonAs(jsLongPrism::getOption, ::JsLong)
    private val float: Optional<Json, Either<PathNotFound, Float>> = rightJson compose json compose jsonAs(jsFloatPrism::getOption, ::JsFloat)
    private val int: Optional<Json, Either<PathNotFound, Int>> = rightJson compose json compose jsonAs(jsIntPrism::getOption, ::JsInt)
    private val array: Optional<Json, Either<PathNotFound, List<Json>>> = rightJson compose json compose jsonAs(jsArrayPrism::getOption, ::JsArray)
    private val `object`: Optional<Json, Either<PathNotFound, Map<String, Json>>> = rightJson compose json compose jsonAs(jsObjectPrism::getOption, ::JsObject)
    private val `null`: Optional<Json, Either<PathNotFound, JsNull>> = rightJson compose json compose jsonAs(jsNullPrism::getOption, ::identity)

    private fun <A> extract(json: Json, optional: Optional<Json, Either<PathNotFound, A>>): Either<JsonPathFailure, A> =
            optional.getOption(json).fold({ TypeExtractionFailed.left() }, ::identity)

    private fun <A> modify(json: Json, optional: Optional<Json, Either<PathNotFound, A>>, f: (A) -> A): Either<JsonPathFailure, Json> =
            optional.getOption(json).fold(
                    { TypeExtractionFailed.left() },
                    { it.fold({ it.left() }, { value -> optional.modify(json, { f(value).right() }).right() }) }
            )

    private fun <A> set(json: Json, optional: Optional<Json, Either<PathNotFound, A>>, a: A): Either<JsonPathFailure, Json> =
            modify(json, optional) { _ -> a }

}
