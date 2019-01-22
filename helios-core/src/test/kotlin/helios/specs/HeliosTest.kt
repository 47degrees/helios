package helios.specs

import arrow.core.Right
import arrow.test.UnitSpec
import helios.specs.model.Friend
import helios.specs.model.decoder
import helios.specs.model.genFriend
import helios.specs.model.toJson
import io.kotlintest.properties.forAll

class HeliosTest : UnitSpec() {

  init {
    "helios serialization works both ways" {
      forAll(genFriend) {
        Friend.decoder().decode(it.toJson()) == Right(it)
      }
    }
  }
}
