package helios.typeclasses

interface KeyEncoder<in A> {
  fun A.keyEncode(): String
}