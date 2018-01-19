package helios.specs

import arrow.core.Right
import arrow.test.UnitSpec
import helios.specs.model.Friend
import helios.specs.model.GenFriend
import helios.specs.model.toJson
import helios.typeclasses.decoder
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.properties.forAll
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class HeliosTest : UnitSpec() {

    init {
        "helios serialization works both ways" {
            forAll(GenFriend) {
                decoder<Friend>().decode(it.toJson()) == Right(it)
            }
        }
    }
}
