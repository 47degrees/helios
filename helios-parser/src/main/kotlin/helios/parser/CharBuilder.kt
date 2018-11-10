package helios.parser

/**
 * CharBuilder is a specialized way to build Strings.
 *
 * It wraps a (growable) array of characters, and can accept
 * additional String or Char data to be added to its buffer.
 */
class CharBuilder {
  val initialSize = 32

  private var cs = CharArray(initialSize)
  private var capacity = initialSize
  private var len = 0

  fun reset(): CharBuilder {
    len = 0
    return this
  }

  fun makeString(): String = String(cs, 0, len)

  fun resizeIfNecessary(goal: Int) {
    if (goal <= capacity) return
    var cap = capacity
    while (cap in 1..(goal - 1)) cap *= 2
    if (cap > capacity) {
      val ncs = CharArray(cap)
      System.arraycopy(cs, 0, ncs, 0, capacity)
      cs = ncs
      capacity = cap
    } else if (cap < capacity) {
      throw RuntimeException("maximum string size exceeded")
    }
  }

  fun extend(s: CharSequence) {
    val tlen = len + s.length
    resizeIfNecessary(tlen)
    var i = 0
    var j = len
    len = tlen
    while (i < s.length) {
      cs[j] = s[i]
      i += 1
      j += 1
    }
  }

  fun append(c: Char) {
    val tlen = len + 1
    resizeIfNecessary(tlen)
    cs[len] = c
    len = tlen
  }
}
