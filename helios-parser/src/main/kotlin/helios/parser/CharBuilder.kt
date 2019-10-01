package helios.parser

/**
 * CharBuilder is a specialized way to build Strings.
 *
 * It wraps a (growable) array of characters, and can accept
 * additional String or Char data to be added to its buffer.
 */
class CharBuilder {
  private val initialSize = 32

  private var cs = CharArray(initialSize)
  private var capacity = initialSize
  private var length = 0

  fun reset(): CharBuilder {
    length = 0
    return this
  }

  fun makeString(): String = String(cs, 0, length)

  private fun resizeIfNecessary(goal: Int): Unit {
    if (goal <= capacity) return
    var cap = capacity
    while (cap in 1 until goal) cap *= 2
    if (cap > capacity) {
      val ncs = CharArray(cap)
      System.arraycopy(cs, 0, ncs, 0, capacity)
      cs = ncs
      capacity = cap
    } else if (cap < capacity) {
      throw RuntimeException("maximum string size exceeded")
    }
  }

  fun extend(s: CharSequence): Unit {
    val tLength = length + s.length
    resizeIfNecessary(tLength)
    var i = 0
    var j = length
    length = tLength
    while (i < s.length) {
      cs[j] = s[i]
      i += 1
      j += 1
    }
  }

  fun append(c: Char): Unit {
    val tLength = length + 1
    resizeIfNecessary(tLength)
    cs[length] = c
    length = tLength
  }
}
