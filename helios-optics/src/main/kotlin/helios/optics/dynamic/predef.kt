package helios.optics.dynamic

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.getOrElse
import arrow.core.identity
import arrow.data.k
import arrow.data.updated
import arrow.optics.Optional
import arrow.optics.POptional
import arrow.optics.typeclasses.Index
import arrow.syntax.either.left
import arrow.syntax.either.right
import arrow.syntax.option.toEither
import arrow.syntax.option.toOption
import helios.core.JsInt
import helios.core.JsObject
import helios.core.Json

sealed class JsonPathFailure
data class PathNotFound(val value: String): JsonPathFailure()
object TypeExtractionFailed: JsonPathFailure()

internal val dynamicIndex = object : Index<Either<PathNotFound, Json>, String, Either<PathNotFound, Json>> {
    override fun index(i: String): Optional<Either<PathNotFound, Json>, Either<PathNotFound, Json>> = Optional(
            getOrModify = {
                it.fold({ Right(it.left()) }, {
                    Right(it.asJsObject().flatMap {
                        it.value[i].toOption()
                    }.toEither { PathNotFound(i) })
                })
            },
            set = { newPathOrJson ->
                { oldPathOrJson ->
                    oldPathOrJson.fold({ it.left() }, { jsObj ->
                        newPathOrJson.fold({ it.left() }, { json ->
                            jsObj.asJsObject().map {
                                JsObject(it.value.k().updated(i, json))
                            }.toEither { PathNotFound(i) }
                        })
                    })
                }
            }
    )
}

val rightJson: Optional<Json, Either<PathNotFound, Json>> = POptional(
        getOrModify = { Right(it.right()) },
        set = { pathOrJson -> { json -> pathOrJson.fold({ json }, ::identity) } }
)

val dynamicJson: Optional<PathNotFound, Json> = POptional(
        getOrModify = { it.left() },
        set = { _ -> { it } }
)

val jsonAsString: Optional<Either<PathNotFound, Json>, Either<PathNotFound, CharSequence>> = POptional(
        getOrModify = { pathOrJson -> pathOrJson.fold({ Right(it.left()) }, { it.asJsString().map { Right(it.value.right()) as Either<Either<PathNotFound, Json>, Either<PathNotFound, CharSequence>> }.getOrElse { Left(pathOrJson) } }) },
        set = { pathOrSequence ->
            {
                it.fold({ it.left() }, {
                    pathOrSequence.fold({ it.left() }, { chars ->
                        it.asJsString().map {
                            it.copy(chars)
                        }.toEither { PathNotFound("Conversion failed") }
                    })
                })
            }
        }
)

val jsonAsInt: Optional<Either<PathNotFound, Json>, Either<PathNotFound, Int>> = POptional(
        getOrModify = { pathOrJson -> pathOrJson.fold({ Right(it.left()) }, { it.asJsNumber().flatMap { (it as? JsInt).toOption() }.map { Right(it.value.right()) as Either<Either<PathNotFound, Json>, Either<PathNotFound, Int>> }.getOrElse { Left(pathOrJson) } }) },
        set = { pathOrSequence ->
            {
                it.fold({ it.left() }, {
                    pathOrSequence.fold({ it.left() }, { int ->
                        it.asJsNumber().flatMap {
                            (it as? JsInt).toOption().map {
                                it.copy(int)
                            }
                        }.toEither { PathNotFound("Conversion failed") }
                    })
                })
            }
        }
)