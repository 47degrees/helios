package helios.optics

import arrow.data.k
import arrow.data.updated
import arrow.instance
import arrow.optics.Optional
import arrow.optics.function.Index
import arrow.syntax.either.left
import arrow.syntax.either.right
import helios.core.JsObject
import helios.core.Json

@instance(JsObject::class)
interface JsObjectIndexInstance : Index<JsObject, String, Json> {
    override fun index(i: String): Optional<JsObject, Json> = Optional(
            getOrModify = { it.value[i]?.right() ?: it.left() },
            set = { js -> { jsObj -> jsObj.copy(jsObj.value.k().updated(i, js)) } }
    )
}