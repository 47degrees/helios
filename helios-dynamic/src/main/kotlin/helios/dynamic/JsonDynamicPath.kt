package helios.dynamic

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

/**
 * JsonDynamicPath offers a DSL for Json which supports `.` notation.
 *
 * example:
 * {
 *  "person": {
 *    "id": 12345,
 *    "name": "John Doe",
 *    "phones": {
 *      "home": "800-123-4567",
 *      "mobile": "877-123-1234"
 *    }
 *  }
 * }
 *
 * To select home phone: JsonDynamicPath().dynamic("person.phones.home")
 */
data class JsonDynamicPath(private val json: Optional<Either<PathNotFound, Json>, Either<PathNotFound, Json>>) {

    companion object {
        operator fun invoke(): JsonDynamicPath = JsonDynamicPath(dynamicJson.choice(Optional.id()) compose rightJson)
    }

    /**
     * Extract value as [Boolean] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [Boolean] value.
     */
    fun extractBool(json: Json): Either<JsonPathFailure, Boolean> = extract(json, bool)

    /**
     * Modify value as [Boolean] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [Boolean] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyBool(json: Json, f: (Boolean) -> Boolean): Either<JsonPathFailure, Json> = modify(json, bool, f)

    /**
     * Set value as [Boolean] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [Boolean] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setBool(json: Json, value: Boolean): Either<JsonPathFailure, Json> = set(json, bool, value)

    /**
     * Extract value as [CharSequence] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [CharSequence] value.
     */
    fun extractCharSeq(json: Json): Either<JsonPathFailure, CharSequence> = extract(json, charseq)

    /**
     * Modify value as [CharSequence] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [CharSequence] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyCharSeq(json: Json, f: (CharSequence) -> CharSequence): Either<JsonPathFailure, Json> = modify(json, charseq, f)

    /**
     * Set value as [CharSequence] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [CharSequence] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setCharSeq(json: Json, value: CharSequence): Either<JsonPathFailure, Json> = set(json, charseq, value)

    /**
     * Extract value as [String] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [String] value.
     */
    fun extractString(json: Json): Either<JsonPathFailure, String> = extract(json, string)

    /**
     * Modify value as [String] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [String] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyString(json: Json, f: (String) -> String): Either<JsonPathFailure, Json> = modify(json, string, f)

    /**
     * Set value as [String] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [String] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setString(json: Json, value: String): Either<JsonPathFailure, Json> = set(json, charseq, value)

    /**
     * Extract value as [JsNumber] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [JsNumber] value.
     */
    fun extractNumber(json: Json): Either<JsonPathFailure, JsNumber> = extract(json, number)

    /**
     * Modify value as [JsNumber] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [JsNumber] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyNumber(json: Json, f: (JsNumber) -> JsNumber): Either<JsonPathFailure, Json> = modify(json, number, f)

    /**
     * Set value as [JsNumber] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [JsNumber] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setNumber(json: Json, value: JsNumber): Either<JsonPathFailure, Json> = set(json, number, value)

    /**
     * Extract value as [JsDecimal] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [JsDecimal] value.
     */
    fun extractDecimal(json: Json): Either<JsonPathFailure, String> = extract(json, decimal)

    /**
     * Modify value as [JsDecimal] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [JsDecimal] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyDecimal(json: Json, f: (String) -> String): Either<JsonPathFailure, Json> = modify(json, decimal, f)

    /**
     * Set value as [JsDecimal] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [JsDecimal] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setDecimal(json: Json, value: String): Either<JsonPathFailure, Json> = set(json, decimal, value)

    /**
     * Extract value as [Long] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [Long] value.
     */
    fun extractLong(json: Json): Either<JsonPathFailure, Long> = extract(json, long)

    /**
     * Modify value as [Long] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [Long] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyLong(json: Json, f: (Long) -> Long): Either<JsonPathFailure, Json> = modify(json, long, f)

    /**
     * Set value as [Long] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [Long] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setLong(json: Json, value: Long): Either<JsonPathFailure, Json> = set(json, long, value)

    /**
     * Extract value as [Float] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [Float] value.
     */
    fun extractFloat(json: Json): Either<JsonPathFailure, Float> = extract(json, float)

    /**
     * Modify value as [Float] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [Float] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyFloat(json: Json, f: (Float) -> Float): Either<JsonPathFailure, Json> = modify(json, float, f)

    /**
     * Set value as [Float] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [Float] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setFloat(json: Json, value: Float): Either<JsonPathFailure, Json> = set(json, float, value)

    /**
     * Extract value as [Int] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [Int] value.
     */
    fun extractInt(json: Json): Either<JsonPathFailure, Int> = extract(json, int)

    /**
     * Modify value as [Int] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [Int] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyInt(json: Json, f: (Int) -> Int): Either<JsonPathFailure, Json> = modify(json, int, f)

    /**
     * Set value as [Int] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [Int] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setInt(json: Json, value: Int): Either<JsonPathFailure, Json> = set(json, int, value)

    /**
     * Extract value as [JsArray] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [JsArray] value.
     */
    fun extractArray(json: Json): Either<JsonPathFailure, List<Json>> = extract(json, array)

    /**
     * Modify value as [JsArray] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [JsArray] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyArray(json: Json, f: (List<Json>) -> List<Json>): Either<JsonPathFailure, Json> = modify(json, array, f)

    /**
     * Set value as [JsArray] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [JsArray] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setArray(json: Json, value: List<Json>): Either<JsonPathFailure, Json> = set(json, array, value)

    /**
     * Extract value as [JsObject] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [JsObject] value.
     */
    fun extractObject(json: Json): Either<JsonPathFailure, Map<String, Json>> = extract(json, `object`)

    /**
     * Modify value as [JsObject] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [JsObject] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyObject(json: Json, f: (Map<String, Json>) -> Map<String, Json>): Either<JsonPathFailure, Json> = modify(json, `object`, f)

    /**
     * Set value as [JsObject] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [JsObject] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setObject(json: Json, value: Map<String, Json>): Either<JsonPathFailure, Json> = set(json, `object`, value)

    /**
     * Extract value as [JsNull] from path.
     *
     * @param json value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [JsNull] value.
     */
    fun extractNull(json: Json): Either<JsonPathFailure, JsNull> = extract(json, `null`)

    /**
     * Modify value as [JsNull] from path.
     *
     * @param json value to apply path on.
     * @param f function to apply to [JsNull] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun modifyNull(json: Json, f: (JsNull) -> JsNull): Either<JsonPathFailure, Json> = modify(json, `null`, f)

    /**
     * Set value as [JsNull] from path.
     *
     * @param json value to apply path on.
     * @param value to set to [JsNull] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun setNull(json: Json, value: JsNull): Either<JsonPathFailure, Json> = set(json, `null`, value)

    /**
     * Extract value as [A] from path.
     *
     * @param jsonValue value to apply path on.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the [A] value.
     */
    fun <A> extract(jsonValue: Json, EN: Encoder<A>, DE: Decoder<A>): Either<JsonPathFailure, A> =
            extract(jsonValue, rightJson compose json compose jsonAs({ DE.decode(it).toOption() }, EN::encode))

    /**
     * Modify value as [A] from path.
     *
     * @param jsonValue value to apply path on.
     * @param f function to apply to [A] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun <A> modify(jsonValue: Json, EN: Encoder<A>, DE: Decoder<A>, f: (A) -> A): Either<JsonPathFailure, Json> =
            modify(jsonValue, rightJson compose json compose jsonAs({ DE.decode(it).toOption() }, EN::encode), f)

    /**
     * Set value as [A] from path.
     *
     * @param jsonValue value to apply path on.
     * @param value to set to [A] in path.
     * @return either a [JsonPathFailure] that occurred somewhere in the path or the modified [Json] value.
     */
    fun <A> set(jsonValue: Json, value: A, EN: Encoder<A>, DE: Decoder<A>): Either<JsonPathFailure, Json> =
            set(jsonValue, rightJson compose json compose jsonAs({ DE.decode(it).toOption() }, EN::encode), value)

    /**
     * Select field with [name] in [JsObject] from path.
     *
     * @param key to select in path.
     * @return [JsonDynamicPath].
     */
    fun select(key: String): JsonDynamicPath = JsonDynamicPath(json compose dynamicIndex.index(key))

    /**
     * Select a path in the Json using `.` notation.
     *
     * @param path in `.` notation.
     * @return [JsonDynamicPath]
     */
    fun dynamic(path: String): JsonDynamicPath = path.split(".")
            .fold(this) { jsPath, str -> jsPath.select(str) }

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
