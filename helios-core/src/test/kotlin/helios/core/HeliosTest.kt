package helios.core

import arrow.core.Right
import arrow.test.UnitSpec
import helios.core.model.Friend
import helios.core.model.decoder
import helios.core.model.genFriend
import helios.core.model.toJson
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
