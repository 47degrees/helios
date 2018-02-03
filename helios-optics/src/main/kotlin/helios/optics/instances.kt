package helios.optics

import arrow.core.Option
import arrow.data.getOption
import arrow.data.k
import arrow.data.updated
import arrow.instance
import arrow.optics.Lens
import arrow.optics.Optional
import arrow.optics.typeclasses.At
import arrow.optics.typeclasses.Index
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

@instance(JsObject::class)
interface JsObjectAtInstance : At<JsObject, String, Option<Json>> {
    override fun at(i: String): Lens<JsObject, Option<Json>> = Lens(
            get = { it.value.getOption(i) },
            set = { optJs ->
                { js ->
                    optJs.fold({
                        js.copy(value = js.value - i)
                    }, {
                        js.copy(value = js.value + (i to it))
                    })
                }
            }
    )

}