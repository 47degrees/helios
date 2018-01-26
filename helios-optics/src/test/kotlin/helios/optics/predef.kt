package helios.optics

import helios.core.JsObject
import helios.core.Json
import helios.typeclasses.Encoder
import helios.typeclasses.encoder
import io.kotlintest.properties.Gen

inline fun <reified T> jsonGen(valid: Gen<T>, EN: Encoder<T> = encoder()): Gen<Json> = Gen.oneOf(
        randomJsonGen(Gen.int()),
        randomJsonGen(Gen.string()),
        randomJsonGen(valid, EN)
)

inline fun <reified T> randomJsonGen(values: Gen<T>, EN: Encoder<T> = encoder()): Gen<Json> = Gen.create {
    Gen.map(
            Gen.string(),
            Gen.create { values.generate().let(EN::encode) }
    ).generate().let(::JsObject)
}