package helios.optics

import arrow.optics.Prism
import helios.core.JsNull
import helios.core.JsNumber
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
import helios.instances.StringDecoderInstance
import helios.instances.StringEncoderInstance

val jsBooleanPrism: Prism<Json, Boolean> = jsonJsBoolean() compose jsBooleanIso()
val jsCharSeqPrism: Prism<Json, CharSequence> = jsonJsString() compose jsStringIso()
val jsStringPrism: Prism<Json, String> = parse(StringEncoderInstance, StringDecoderInstance)
val jsNumberPrism: Prism<Json, JsNumber> = jsonJsNumber()
val jsDecimalPrism: Prism<Json, String> = jsNumberPrism compose jsNumberJsDecimal() compose jsDecimalIso()
val jsLongPrism: Prism<Json, Long> = jsNumberPrism compose jsNumberJsLong() compose jsLongIso()
val jsFloatPrism: Prism<Json, Float> = jsNumberPrism compose jsNumberJsFloat() compose jsFloatIso()
val jsIntPrism: Prism<Json, Int> = jsNumberPrism compose jsNumberJsInt() compose jsIntIso()
val jsArrayPrism: Prism<Json, List<Json>> = jsonJsArray() compose jsArrayIso()
val jsObjectPrism: Prism<Json, Map<String, Json>> = jsonJsObject() compose jsObjectIso()
val jsNullPrism: Prism<Json, JsNull> = jsonJsNull()