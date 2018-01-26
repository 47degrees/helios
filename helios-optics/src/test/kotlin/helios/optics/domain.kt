package helios.optics

import helios.meta.json
import io.kotlintest.properties.Gen

@json data class Street(val number: Int, val name: String)

fun streetGen(): Gen<Street> = Gen.create { Street(Gen.int().generate(), Gen.string().generate()) }
