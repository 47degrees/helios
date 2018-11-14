package helios.specs.model

import arrow.*
import arrow.core.Either
import arrow.deriving
import arrow.higherkind
import arrow.optics.optics
import arrow.typeclasses.Applicative
import arrow.typeclasses.Functor
import arrow.typeclasses.Monad
import helios.meta.json
import io.kotlintest.properties.Gen
import io.kotlintest.properties.map

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

@higherkind
@deriving(
  Functor::class,
  Applicative::class,
  Monad::class
)
class GenA<A>(val value: Gen<A>) : GenAOf<A>, Gen<A> by value {

  fun <B> map(f: (A) -> B): GenA<B> =
    GenA(value.map(f))

  fun <B> flatMap(f: (A) -> GenAOf<B>): GenA<B> =
    map(f).value.generate().fix()

  fun <B> ap(ff: GenAOf<(A) -> B>): GenA<B> =
    ff.fix().flatMap { this.fix().map(it) }

  companion object {
    fun <A> just(a: A): GenA<A> =
      GenA(Gen.create { a })

    tailrec fun <A, B> tailRecM(a: A, f: (A) -> GenAOf<Either<A, B>>): GenA<B> {
      val r = f(a).fix()
      val genValue = r.value.generate()
      return when (genValue) {
        is Either.Left<A>  -> tailRecM(genValue.a, f)
        is Either.Right<B> -> just(genValue.b)
      }
    }
  }
}

fun <A> Gen<A>.k(): GenA<A> = GenA(this)

object GenFriend : Gen<Friend> {

  override fun generate(): Friend =
    GenA.applicative().map(
      Gen.string().k(),
      Gen.string().k(),
      Gen.string().k(),
      Gen.list(Gen.string()).k(),
      Gen.list(Gen.int()).k(),
      Gen.string().k(),
      Gen.string().k()
    ) { (_id, latitude, longitude, tags, range, greeting, favoriteFruit) ->
      Friend(_id, latitude, longitude, tags, range, greeting, favoriteFruit)
    }.fix().generate()

}

val sampleJson: String = """
        {
  "friends": [
    {
      "_id": "5a53e9ff2d2c7723422549f8",
      "latitude": "52.657215",
      "longitude": "141.189848",
      "tags": [
        "aliquip",
        "in",
        "magna",
        "consectetur",
        "pariatur"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Jordan Cooper"
        },
        {
          "id": 1,
          "name": "Reid Underwood"
        },
        {
          "id": 2,
          "name": "Leta Henson"
        }
      ],
      "greeting": "Hello, Dodson! You have 6 unread messages.",
      "favoriteFruit": "banana"
    },
    {
      "_id": "5a53ea009f8757ef348f92fc",
      "latitude": "37.34675",
      "longitude": "111.050794",
      "tags": [
        "et",
        "aliquip",
        "tempor",
        "labore",
        "ipsum"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Florence Burke"
        },
        {
          "id": 1,
          "name": "Carly Evans"
        },
        {
          "id": 2,
          "name": "Serena Johnson"
        }
      ],
      "greeting": "Hello, Glass! You have 8 unread messages.",
      "favoriteFruit": "banana"
    },
    {
      "_id": "5a53ea00903b68fa05f1ab5b",
      "latitude": "-33.581901",
      "longitude": "-33.32772",
      "tags": [
        "nisi",
        "eu",
        "exercitation",
        "id",
        "aliquip"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Hendricks Randall"
        },
        {
          "id": 1,
          "name": "Randall Pace"
        },
        {
          "id": 2,
          "name": "Roberts Becker"
        }
      ],
      "greeting": "Hello, Koch! You have 9 unread messages.",
      "favoriteFruit": "banana"
    },
    {
      "_id": "5a53ea002e85517642f70ca4",
      "latitude": "49.730088",
      "longitude": "-158.479322",
      "tags": [
        "esse",
        "exercitation",
        "nisi",
        "elit",
        "laboris"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Ana Moran"
        },
        {
          "id": 1,
          "name": "James Fulton"
        },
        {
          "id": 2,
          "name": "Mitchell Bean"
        }
      ],
      "greeting": "Hello, Bridgett! You have 5 unread messages.",
      "favoriteFruit": "apple"
    },
    {
      "_id": "5a53ea001002edabbe604572",
      "latitude": "53.919839",
      "longitude": "-71.03511",
      "tags": [
        "veniam",
        "elit",
        "consequat",
        "quis",
        "reprehenderit"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Beulah Ortiz"
        },
        {
          "id": 1,
          "name": "Martinez Kidd"
        },
        {
          "id": 2,
          "name": "Jenna Holloway"
        }
      ],
      "greeting": "Hello, Luella! You have 5 unread messages.",
      "favoriteFruit": "strawberry"
    },
    {
      "_id": "5a53ea00955ec935ecec05f3",
      "latitude": "-45.486921",
      "longitude": "63.211776",
      "tags": [
        "voluptate",
        "ad",
        "voluptate",
        "minim",
        "sint"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Hull Berg"
        },
        {
          "id": 1,
          "name": "Audrey Guerra"
        },
        {
          "id": 2,
          "name": "Tammi Horne"
        }
      ],
      "greeting": "Hello, Love! You have 10 unread messages.",
      "favoriteFruit": "strawberry"
    },
    {
      "_id": "5a53ea0091955b3c822c9521",
      "latitude": "69.080384",
      "longitude": "-83.466304",
      "tags": [
        "adipisicing",
        "incididunt",
        "duis",
        "sint",
        "amet"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Elma Barton"
        },
        {
          "id": 1,
          "name": "Ila Hatfield"
        },
        {
          "id": 2,
          "name": "Angelina Pruitt"
        }
      ],
      "greeting": "Hello, Samantha! You have 5 unread messages.",
      "favoriteFruit": "strawberry"
    },
    {
      "_id": "5a53ea00ab92f2f9f55d08b6",
      "latitude": "68.937566",
      "longitude": "120.353298",
      "tags": [
        "id",
        "minim",
        "culpa",
        "duis",
        "quis"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Stokes Merrill"
        },
        {
          "id": 1,
          "name": "Geraldine Beard"
        },
        {
          "id": 2,
          "name": "Lynn Mcdowell"
        }
      ],
      "greeting": "Hello, Walton! You have 7 unread messages.",
      "favoriteFruit": "banana"
    },
    {
      "_id": "5a53ea007e27a651d41fab70",
      "latitude": "20.262147",
      "longitude": "48.63372",
      "tags": [
        "aute",
        "fugiat",
        "dolor",
        "eiusmod",
        "amet"
      ],
      "range": [
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9
      ],
      "friends": [
        {
          "id": 0,
          "name": "Jean Cruz"
        },
        {
          "id": 1,
          "name": "Lauri Mills"
        },
        {
          "id": 2,
          "name": "Perez Whitfield"
        }
      ],
      "greeting": "Hello, Meagan! You have 10 unread messages.",
      "favoriteFruit": "strawberry"
    }
  ]
}
        """