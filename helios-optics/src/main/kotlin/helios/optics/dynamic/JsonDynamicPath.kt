package helios.optics.dynamic

import arrow.core.Either
import arrow.core.identity
import arrow.optics.Optional
import arrow.syntax.either.left
import arrow.syntax.either.right
import arrow.syntax.foldable.fold
import helios.core.JsObject
import helios.core.Json

data class JsonDynamicPath(val json: Optional<Either<PathNotFound, Json>, Either<PathNotFound, Json>>) {

    /**
     * Extract value as [CharSequence] from path.
     */
    val charseq: Optional<Json, Either<PathNotFound, CharSequence>> = rightJson compose json compose jsonAsString

    /**
     * Extract value as [Int] from path.
     */
    val int: Optional<Json, Either<PathNotFound, Int>> = rightJson compose json compose jsonAsInt

    /**
     * Select field with [name] in [JsObject] from path.
     */
    fun select(name: String): JsonDynamicPath = JsonDynamicPath(json compose dynamicIndex.index(name))

}

/**
 * Alternative API where we don't expose the optics.
 */
fun JsonDynamicPath.getCharSeq(json: Json): Either<JsonPathFailure, CharSequence> =
        charseq.getOption(json).fold({ TypeExtractionFailed.left() }, ::identity)

fun JsonDynamicPath.getInt(json: Json): Either<JsonPathFailure, Int> =
        int.getOption(json).fold({ TypeExtractionFailed.left() }, ::identity)

fun JsonDynamicPath.modifyInt(json: Json, value: Int): Either<JsonPathFailure, Json> = int.getOption(json).fold(
        { TypeExtractionFailed.left() },
        { it.fold({ it.left() }, { this.int.set(json, value.right()).right() }) }
)
