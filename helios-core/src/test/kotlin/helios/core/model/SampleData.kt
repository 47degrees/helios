package helios.core.model

import arrow.optics.optics
import helios.json
import helios.test.generators.alphaStr
import io.kotlintest.properties.Gen

@json
@optics
data class Friend(
  val _id: String,
  val latitude: String,
  val longitude: String,
  val tags: List<String>,
  val range: List<Int>,
  val greeting: String,
  val favoriteFruit: String
) {
  companion object
}

val genFriend: Gen<Friend> =
  Gen.bind(
    Gen.alphaStr(),
    Gen.alphaStr(),
    Gen.alphaStr(),
    Gen.list(Gen.alphaStr()),
    Gen.list(Gen.int()),
    Gen.alphaStr(),
    Gen.alphaStr()
  ) { id, latitude, longitude, tags, range, greeting, favoriteFruit ->
    Friend(id, latitude, longitude, tags, range, greeting, favoriteFruit)
  }
