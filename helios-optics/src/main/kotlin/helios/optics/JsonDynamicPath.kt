package helios.optics

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.getOrElse
import arrow.core.identity
import arrow.data.k
import arrow.data.updated
import arrow.optics.Optional
import arrow.optics.Prism
import arrow.optics.typeclasses.Index
import arrow.syntax.either.left
import arrow.syntax.either.right
import arrow.syntax.option.toEither
import arrow.syntax.option.toOption
import helios.core.JsInt
import helios.core.JsObject
import helios.core.Json
import helios.core.jsStringIso
import helios.core.jsonJsObject
import helios.core.jsonJsString

data class JsonDynamicPathPath(val json: Optional<Either<PathNotFound, Json>, Either<PathNotFound, Json>>) {

    /**
     * Extract value as [CharSequence] from path.
     */
    val charseq: Optional<Json, Either<PathNotFound, CharSequence>> = rightJson() compose json compose jsonAsString()

    /**
     * Extract value as [Int] from path.
     */
    val int: Optional<Json, Either<PathNotFound, Int>> = rightJson() compose json compose jsonAsInt()

    /**
     * Select field with [name] in [JsObject] from path.
     */
    fun select(name: String): JsonDynamicPathPath = JsonDynamicPathPath(json compose dynamicIndex().index(name))

}


data class PathNotFound(val value: String)

fun dynamicIndex() = object : Index<Either<PathNotFound, Json>, String, Either<PathNotFound, Json>> {
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

fun <L, R> rightPrism(): Prism<Either<L, R>, R> = Prism(
        getOrModify = { eith: Either<L, R> -> eith.fold({ Left(eith) }, { it.right() }) },
        reverseGet = { it -> it.right() }
)

fun rightJson(): Optional<Json, Either<PathNotFound, Json>> = Optional(
        getOrModify = { Right(it.right()) },
        set = { pathOrJson -> { json -> pathOrJson.fold({ json }, ::identity) } }
)

fun dynamicJson(): Optional<PathNotFound, Json> = Optional(
        getOrModify = { it.left() },
        set = { _ -> { it } }
)

fun jsonAsString(): Optional<Either<PathNotFound, Json>, Either<PathNotFound, CharSequence>> = Optional(
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

fun jsonAsInt(): Optional<Either<PathNotFound, Json>, Either<PathNotFound, Int>> = Optional(
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