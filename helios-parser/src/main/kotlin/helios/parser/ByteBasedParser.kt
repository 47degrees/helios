package helios.parser

import kotlin.experimental.and

/**
 * Interface used when the data to be parsed is in UTF-8.
 *
 * This parser has to translate input bytes to Chars and Strings. It
 * provides a byte() method to access individual bytes, and also
 * parser strings from bytes.
 *
 * Its parseString() implementation has two cases. In the first case
 * (the hot path) the string has no escape sequences and we can just
 * UTF-8 decode the entire set of bytes. In the second case, it goes
 * to some trouble to be sure to de-escape correctly given that the
 * input data is UTF-8.
 */
interface ByteBasedParser<J> : Parser<J> {

  fun byte(i: Int): Byte

  /**
   * See if the string has any escape sequences. If not, return the end of the
   * string. If so, bail out and return -1.
   *
   * This method expects the data to be in UTF-8 and accesses it as bytes. Thus
   * we can just ignore any bytes with the highest bit set.
   */
  fun parseStringSimple(i: Int, ctxt: FContext<J>): Int {
    var j = i
    var c: Int = (byte(j) and 0xff.toByte()).toInt()
    while (c != 34) {
      if (c < 32) return die(j, "control char ($c) in string")
      if (c == 92) return -1
      j += 1
      c = (byte(j) and 0xff.toByte()).toInt()
    }
    return j + 1
  }

  /**
   * Parse the string according to JSON rules, and add to the given context.
   *
   * This method expects the data to be in UTF-8 and accesses it as bytes.
   */
  override fun parseString(i: Int, ctxt: FContext<J>): Int {
    val k = parseStringSimple(i + 1, ctxt)
    if (k != -1) {
      ctxt.add(at(i + 1, k - 1))
      return k
    }

    // TODO: we might be able to do better by identifying where
    // escapes occur, and then translating the intermediate strings in
    // one go.

    var j = i + 1
    val sb = CharBuilder()

    var c: Int = (byte(j).toInt() and 0xff)
    while (c != 34) { // "
      if (c == 92) { // \
        when (byte(j + 1).toInt()) {
          98  -> {
            sb.append('\b'); j += 2
          }
          102 -> {
            sb.append('\u000C'); j += 2
          }
          110 -> {
            sb.append('\n'); j += 2
          }
          114 -> {
            sb.append('\r'); j += 2
          }
          116 -> {
            sb.append('\t'); j += 2
          }

          34  -> {
            sb.append('"'); j += 2
          }
          47  -> {
            sb.append('/'); j += 2
          }
          92  -> {
            sb.append('\\'); j += 2
          }

          // if there's a problem then descape will explode
          117 -> {
            sb.append(descape(at(j + 2, j + 6))); j += 6
          }

          c   -> die(j, "invalid escape sequence (\\${c.toChar()})")
        }
      } else if (c < 32) {
        die(j, "control char ($c) in string")
      } else if (c < 128) {
        // 1-byte UTF-8 sequence
        sb.append(c.toChar())
        j += 1
      } else if ((c and 224) == 192) {
        // 2-byte UTF-8 sequence
        sb.extend(at(j, j + 2))
        j += 2
      } else if ((c and 240) == 224) {
        // 3-byte UTF-8 sequence
        sb.extend(at(j, j + 3))
        j += 3
      } else if ((c and 248) == 240) {
        // 4-byte UTF-8 sequence
        sb.extend(at(j, j + 4))
        j += 4
      } else {
        die(j, "invalid UTF-8 encoding")
      }
      c = byte(j).toInt() and 0xff
    }
    ctxt.add(sb.makeString())
    return j + 1
  }
}
