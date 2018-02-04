package helios.optics

import helios.meta.json
import io.kotlintest.properties.Gen

@json
data class City(val streets: List<Street>)

@json
data class Street(val name: String)

fun genStreet(): Gen<Street> = Gen.string().let { stringGen ->
    Gen.create { Street(stringGen.generate()) }
}

fun genCity(): Gen<City> = Gen.list(genStreet()).let { gen ->
    Gen.create { City(gen.generate()) }
}