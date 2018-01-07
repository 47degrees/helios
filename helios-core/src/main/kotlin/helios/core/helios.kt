package helios.core

import arrow.*
import arrow.core.*
import arrow.syntax.applicative.map
import arrow.syntax.option.*

@prisms sealed class Json {

    fun <B> fold(ifJsString: (JsString) -> B,
                 ifJsNumber: (JsNumber) -> B,
                 ifJsArray: (JsArray) -> B,
                 ifJsObject: (JsObject) -> B,
                 ifJsNull: () -> B): B =
            when (this) {
                is JsString -> ifJsString(this)
                is JsNumber -> ifJsNumber(this)
                is JsArray -> ifJsArray(this)
                is JsObject -> ifJsObject(this)
                JsNull -> ifJsNull()
            }

    fun add(key: String, value: Json): JsObject =
            JsObject(hashMapOf(key to value))

    fun asJsString(): Option<JsString> =
            (this as? JsString)?.some() ?: none()

    fun asJsNumber(): Option<JsNumber> =
            (this as? JsNumber)?.some() ?: none()

    fun asJsArray(): Option<JsArray> =
            (this as? JsArray)?.some() ?: none()

    fun asJsObject(): Option<JsObject> =
            (this as? JsObject)?.some() ?: none()

    fun asJsNull(): Option<JsNull> =
            (this as? JsNull)?.some() ?: none()

    fun merge(that: Json): Json =
            Option.applicative().map(asJsObject(), that.asJsObject(), { (lhs, rhs) ->
                lhs.toList().fold(rhs) { acc, (key, value) ->
                    rhs(key).fold({ acc.add(key, value) }, { r -> acc.add(key, value.merge(r)) })
                }
            }).ev().getOrElse { that }

    abstract fun toJsonString(): String

}

@lenses data class JsString(val value: String) : Json() {
    override fun toJsonString(): String = """"$value""""
}
@lenses data class JsNumber(val value: Int) : Json() {
    override fun toJsonString(): String = "$value"
}
@lenses data class JsArray(val value: List<Json>) : Json() {
    override fun toJsonString(): String =
            value.map { it.toJsonString() }.joinToString(prefix = "[", separator = ",", postfix = "]")
}
@lenses data class JsObject(val value: Map<String, Json>) : Json() {

    fun keys(): Iterable<String> = value.keys

    fun values(): Iterable<Json> = value.values

    fun toList(): List<Tuple2<String, Json>> = value.toList().map { it.first toT it.second }

    operator fun invoke(key: String): Option<Json> = Option.fromNullable(value[key])

    override fun toJsonString(): String =
            value.map { (k, v) -> """"$k":${v.toJsonString()}""" }.joinToString(prefix = "{", separator = ",", postfix = "}")
}

object JsNull : Json() {
    override fun toJsonString(): String = "null"
}

fun jsString(value: String): JsString = JsString(value)
fun jsNumber(value: Int): JsNumber = JsNumber(value)
fun jsArray(value: List<Json>): JsArray = JsArray(value)
fun jsObject(value: Map<String, Json>): JsObject = JsObject(value)
fun jsNull(): JsNull = JsNull