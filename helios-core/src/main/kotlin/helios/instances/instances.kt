package helios.instances

import arrow.core.*
import arrow.instance
import arrow.instances.BooleanInstances
import arrow.instances.eq
import arrow.typeclasses.Eq
import helios.core.*
import helios.typeclasses.*

fun Int.Companion.encoder() = object : Encoder<Int> {
  override fun Int.encode(): Json = JsNumber(this)
}

fun Int.Companion.decoder() = object : Decoder<Int> {
  override fun decode(value: Json): Either<DecodingError, Int> =
    value.asJsNumber().flatMap { it.toInt() }.toEither { NumberDecodingError(value) }
}

fun BooleanInstances.encoder() = object : Encoder<Boolean> {
  override fun Boolean.encode(): Json = JsBoolean(this)
}

fun BooleanInstances.decoder() = object : Decoder<Boolean> {
  override fun decode(value: Json): Either<DecodingError, Boolean> =
    value.asJsBoolean().flatMap { it.value.some() }.toEither { BooleanDecodingError(value) }
}

fun String.Companion.encoder() = object : Encoder<String> {
  override fun String.encode(): Json = JsString(this)
}

fun String.Companion.decoder() = object : Decoder<String> {
  override fun decode(value: Json): Either<DecodingError, String> =
    value.asJsString().flatMap {
      it.value.toString().some()
    }.toEither { StringDecodingError(value) }
}

@instance(Option::class)
interface OptionEncoderInstance<in A> : Encoder<Option<A>> {

  fun encoderA(): Encoder<A>

  override fun Option<A>.encode(): Json =
    fold({ JsNull }, { encoderA().run { it.encode() } })

}

@instance(Tuple2::class)
interface Tuple2EncoderInstance<A, B> : Encoder<Tuple2<A, B>> {

  fun encoderA(): Encoder<A>

  fun encoderB(): Encoder<B>

  override fun Tuple2<A, B>.encode(): Json = JsArray(
    listOf(
      encoderA().run { a.encode() },
      encoderB().run { b.encode() }
    )
  )

}

@instance(Tuple3::class)
interface Tuple3EncoderInstance<A, B, C> : Encoder<Tuple3<A, B, C>> {

  fun encoderA(): Encoder<A>

  fun encoderB(): Encoder<B>

  fun encoderC(): Encoder<C>

  override fun Tuple3<A, B, C>.encode(): Json = JsArray(
    listOf(
      encoderA().run { a.encode() },
      encoderB().run { b.encode() },
      encoderC().run { c.encode() }
    )
  )

}

private inline val Json.isNull inline get() = this === JsNull

@instance(JsObject::class)
interface JsObjectEqInstance : Eq<JsObject> {
  override fun JsObject.eqv(b: JsObject): Boolean = with(Json.eq()) {
    this@eqv.value.entries.zip(b.value.entries) { aa, bb ->
      aa.key == bb.key && aa.value.eqv(bb.value)
    }.fold(true) { b1, b2 -> b1 && b2 }
  }
}

@instance(JsArray::class)
interface JsArrayEqInstance : Eq<JsArray> {
  override fun JsArray.eqv(b: JsArray): Boolean = with(Json.eq()) {
    this@eqv.value.zip(b.value) { a, b -> a.eqv(b) }
      .fold(true) { b1, b2 -> b1 && b2 }
  }
}

@instance(Json::class)
interface JsonEqInstance : Eq<Json> {
  override fun Json.eqv(b: Json): Boolean = when {
    this is JsObject && b is JsObject -> JsObject.eq().run { this@eqv.eqv(b) }
    this is JsString && b is JsString -> String.eq().run {
      this@eqv.value.toString().eqv(b.value.toString())
    }
    this is JsNumber && b is JsNumber -> JsNumber.eq().run { this@eqv.eqv(b) }
    this is JsBoolean && b is JsBoolean -> BooleanInstances.eq().run { this@eqv.value.eqv(b.value) }
    this is JsArray && b is JsArray -> JsArray.eq().run { this@eqv.eqv(b) }
    else -> this.isNull && b.isNull
  }

}

@instance(JsNumber::class)
interface JsNumberEqInstance : Eq<JsNumber> {
  override fun JsNumber.eqv(b: JsNumber): Boolean = when (this) {
    is JsDecimal -> when (b) {
      is JsDecimal -> String.eq().run { this@eqv.value.eqv(b.value) }
      is JsLong    -> String.eq().run { this@eqv.value.eqv(b.value.toString()) }
      is JsDouble  -> String.eq().run { this@eqv.value.eqv(b.value.toString()) }
      is JsFloat   -> String.eq().run { this@eqv.value.eqv(b.value.toString()) }
      is JsInt     -> String.eq().run { this@eqv.value.eqv(b.value.toString()) }
    }
    is JsLong    -> when (b) {
      is JsDecimal -> String.eq().run { this@eqv.value.toString().eqv(b.value) }
      is JsLong    -> Long.eq().run { this@eqv.value.eqv(b.value) }
      is JsDouble  -> Double.eq().run { this@eqv.value.toDouble().eqv(b.value) }
      is JsFloat   -> Float.eq().run { this@eqv.value.toFloat().eqv(b.value) }
      is JsInt     -> Long.eq().run { this@eqv.value.eqv(b.value.toLong()) }
    }
    is JsDouble  -> when (b) {
      is JsDecimal -> String.eq().run { this@eqv.value.toString().eqv(b.value) }
      is JsLong    -> Double.eq().run { this@eqv.value.eqv(b.value.toDouble()) }
      is JsDouble  -> Double.eq().run { this@eqv.value.eqv(b.value) }
      is JsFloat   -> Double.eq().run { this@eqv.value.eqv(b.value.toDouble()) }
      is JsInt     -> Double.eq().run { this@eqv.value.eqv(b.value.toDouble()) }
    }
    is JsFloat   -> when (b) {
      is JsDecimal -> String.eq().run { this@eqv.value.toString().eqv(b.value) }
      is JsLong    -> Float.eq().run { this@eqv.value.eqv(b.value.toFloat()) }
      is JsDouble  -> Double.eq().run { this@eqv.value.toDouble().eqv(b.value) }
      is JsFloat   -> Float.eq().run { this@eqv.value.eqv(b.value) }
      is JsInt     -> Float.eq().run { this@eqv.value.eqv(b.value.toFloat()) }
    }
    is JsInt     -> when (b) {
      is JsDecimal -> String.eq().run { this@eqv.value.toString().eqv(b.value) }
      is JsLong    -> Long.eq().run { this@eqv.value.toLong().eqv(b.value) }
      is JsDouble  -> Double.eq().run { this@eqv.value.toDouble().eqv(b.value) }
      is JsFloat   -> Float.eq().run { this@eqv.value.toFloat().eqv(b.value) }
      is JsInt     -> Int.eq().run { this@eqv.value.eqv(b.value) }
    }
  }
}