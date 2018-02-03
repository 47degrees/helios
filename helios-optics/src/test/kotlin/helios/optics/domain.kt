package helios.optics

import helios.meta.json
import io.kotlintest.properties.Gen

@json
data class City(val streets: List<Street>)

@json
data class Street(val name: String)

fun streetGen(): Gen<Street> = Gen.create { Street(Gen.string().generate()) }

val json = """
{
  "street": [
    {
      "name": "East Main Street"
    },
    {
      "name": "West Birch Lane"
    }
  ]
}
        """.trimMargin()