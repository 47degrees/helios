package helios.parser

import arrow.core.Try
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset

data class ParseException(val msg: String, val index: Int, val line: Int, val col: Int) :
  Exception(msg)

data class IncompleteParseException(val msg: String) : Exception(msg)

val utf8 = Charset.forName("UTF-8")

/**
 * Valid parser states.
 */
internal val ARRBEG = 6
internal val OBJBEG = 7
internal val DATA = 1
internal val KEY = 2
internal val SEP = 3
internal val ARREND = 4
internal val OBJEND = 5

val HexChars: IntArray = {
  val arr = IntArray(128)
  var i = 0
  while (i < 10) {
    arr[i + '0'.toInt()] = i; i += 1
  }
  i = 0
  while (i < 16) {
    arr[i + 'a'.toInt()] = 10 + i; arr[i + 'A'.toInt()] = 10 + i; i += 1
  }
  arr
}()

/**
 * A Kotlin port of the Scala jawn parser https://github.com/non/jawn
 */
interface Parser<J> {

  /**
   * Read the byte/char at 'i' as a Char.
   *
   * Note that this should not be used on potential multi-byte
   * sequences.
   */
  fun at(i: Int): Char

  /**
   * Read the bytes/chars from 'i' until 'j' as a String.
   */
  fun at(i: Int, j: Int): CharSequence

  /**
   * Return true iff 'i' is at or beyond the end of the input (EOF).
   */
  fun atEof(i: Int): Boolean

  /**
   * The reset() method is used to signal that we're working from the
   * given position, and any previous data can be released. Some
   * parsers (e.g.  StringParser) will ignore release, while others
   * (e.g. PathParser) will need to use this information to release
   * and allocate different areas.
   */
  fun reset(i: Int): Int

  /**
   * The checkpoint() method is used to allow some parsers to store
   * their progress.
   */
  fun checkpoint(state: Int, i: Int, stack: List<FContext<J>>): Unit

  /**
   * Should be called when parsing is finished.
   */
  fun close(): Unit

  fun newline(i: Int): Unit
  fun line(): Int
  fun column(i: Int): Int

  /**
   * Used to generate error messages with character info and offsets.
   */
  fun die(i: Int, msg: String): Nothing {
    val y = line() + 1
    val x = column(i) + 1
    val s = "%s got %s (line %d, column %d)".format(msg, at(i), y, x)
    throw ParseException(s, i, y, x)
  }

  /**
   * Used to generate messages for internal errors.
   *
   * This should only be used in situations where a possible bug in
   * the parser was detected. For errors in user-provided JSON, use
   * die().
   */
  fun error(msg: String): Nothing =
    throw RuntimeException(msg)

  /**
   * Parse the given number, and add it to the given context.
   *
   * We don't actually instantiate a number here, but rather pass the
   * string of for future use. Facades can choose to be lazy and just
   * store the string. This ends up being way faster and has the nice
   * side-effect that we know exactly how the user represented the
   * number.
   */
  fun parseNum(i: Int, ctxt: FContext<J>, facade: Facade<J>): Int {
    var k = i
    var c = at(k)
    var decIndex = -1
    var expIndex = -1

    if (c == '-') {
      k += 1
      c = at(k)
    }
    if (c == '0') {
      k += 1
      c = at(k)
    } else if ('1' <= c && c <= '9') {
      while ('0' <= c && c <= '9') {
        k += 1; c = at(k)
      }
    } else {
      die(i, "expected digit")
    }

    if (c == '.') {
      decIndex = k - i
      k += 1
      c = at(k)
      if ('0' <= c && c <= '9') {
        while ('0' <= c && c <= '9') {
          k += 1; c = at(k)
        }
      } else {
        die(i, "expected digit")
      }
    }

    if (c == 'e' || c == 'E') {
      expIndex = k - i
      k += 1
      c = at(k)
      if (c == '+' || c == '-') {
        k += 1
        c = at(k)
      }
      if ('0' <= c && c <= '9') {
        while ('0' <= c && c <= '9') {
          k += 1; c = at(k)
        }
      } else {
        die(i, "expected digit")
      }
    }

    ctxt.add(facade.jnum(at(i, k), decIndex, expIndex))
    return k
  }

  /**
   * Parse the given number, and add it to the given context.
   *
   * This method is a bit slower than parseNum() because it has to be
   * sure it doesn't run off the end of the input.
   *
   * Normally (when operating in rparse in the context of an outer
   * array or object) we don't need to worry about this and can just
   * grab characters, because if we run out of characters that would
   * indicate bad input. This is for cases where the number could
   * possibly be followed by a valid EOF.
   *
   * This method has all the same caveats as the previous method.
   */
  fun parseNumSlow(i: Int, ctxt: FContext<J>, facade: Facade<J>): Int {
    var j = i
    var c = at(j)
    var decIndex = -1
    var expIndex = -1

    if (c == '-') {
      // any valid input will require at least one digit after -
      j += 1
      c = at(j)
    }
    if (c == '0') {
      j += 1
      if (atEof(j)) {
        ctxt.add(facade.jnum(at(i, j), decIndex, expIndex))
        return j
      }
      c = at(j)
    } else if ('1' <= c && c <= '9') {
      while ('0' <= c && c <= '9') {
        j += 1
        if (atEof(j)) {
          ctxt.add(facade.jnum(at(i, j), decIndex, expIndex))
          return j
        }
        c = at(j)
      }
    } else {
      die(i, "expected digit")
    }

    if (c == '.') {
      // any valid input will require at least one digit after .
      decIndex = j - i
      j += 1
      c = at(j)
      if ('0' <= c && c <= '9') {
        while ('0' <= c && c <= '9') {
          j += 1
          if (atEof(j)) {
            ctxt.add(facade.jnum(at(i, j), decIndex, expIndex))
            return j
          }
          c = at(j)
        }
      } else {
        die(i, "expected digit")
      }
    }

    if (c == 'e' || c == 'E') {
      // any valid input will require at least one digit after e, e+, etc
      expIndex = j - i
      j += 1
      c = at(j)
      if (c == '+' || c == '-') {
        j += 1
        c = at(j)
      }
      if ('0' <= c && c <= '9') {
        while ('0' <= c && c <= '9') {
          j += 1
          if (atEof(j)) {
            ctxt.add(facade.jnum(at(i, j), decIndex, expIndex))
            return j
          }
          c = at(j)
        }
      } else {
        die(i, "expected digit")
      }
    }

    ctxt.add(facade.jnum(at(i, j), decIndex, expIndex))
    return j
  }

  /**
   * Generate a Char from the hex digits of "\u1234" (i.e. "1234").
   *
   * NOTE: This is only capable of generating characters from the basic plane.
   * This is why it can only return Char instead of Int.
   */
  fun descape(s: CharSequence): Char {
    val hc = HexChars
    var i = 0
    var x = 0
    while (i < 4) {
      x = (x shl 4) or hc[s[i].toInt()]
      i += 1
    }
    return x.toChar()
  }

  /**
   * Parse the JSON string starting at 'i' and save it into 'ctxt'.
   */
  fun parseString(i: Int, ctxt: FContext<J>): Int

  /**
   * Parse the JSON constant "true".
   *
   * Note that this method assumes that the first character has already been checked.
   */
  fun parseTrue(i: Int, facade: Facade<J>): J =
    if (at(i + 1) == 'r' && at(i + 2) == 'u' && at(i + 3) == 'e') {
      facade.jtrue()
    } else {
      die(i, "expected true")
    }

  /**
   * Parse the JSON constant "false".
   *
   * Note that this method assumes that the first character has already been checked.
   */
  fun parseFalse(i: Int, facade: Facade<J>): J =
    if (at(i + 1) == 'a' && at(i + 2) == 'l' && at(i + 3) == 's' && at(i + 4) == 'e') {
      facade.jfalse()
    } else {
      die(i, "expected false")
    }

  /**
   * Parse the JSON constant "null".
   *
   * Note that this method assumes that the first character has already been checked.
   */
  fun parseNull(i: Int, facade: Facade<J>): J =
    if (at(i + 1) == 'u' && at(i + 2) == 'l' && at(i + 3) == 'l') {
      facade.jnull()
    } else {
      die(i, "expected null")
    }

  /**
   * Parse and return the next JSON value and the position beyond it.
   */
  tailrec fun parse(i: Int, facade: Facade<J>): Pair<J, Int> =
    when (at(i)) {
      // ignore whitespace
      ' '                                                   -> parse(i + 1, facade)
      '\t'                                                  -> parse(i + 1, facade)
      '\r'                                                  -> parse(i + 1, facade)
      '\n'                                                  -> {
        newline(i); parse(i + 1, facade)
      }

      // if we have a recursive top-level structure, we'll delegate the parsing
      // duties to our good friend rparse().
      '['                                                   -> rparse(
        ARRBEG,
        i + 1,
        listOf(facade.arrayContext()),
        facade
      )
      '{'                                                   -> rparse(
        OBJBEG,
        i + 1,
        listOf(facade.objectContext()),
        facade
      )

      // we have a single top-level number
      '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
        val ctxt = facade.singleContext()
        val j = parseNumSlow(i, ctxt, facade)
        ctxt.finish() to j
      }

      // we have a single top-level string
      '"'                                                   -> {
        val ctxt = facade.singleContext()
        val j = parseString(i, ctxt)
        ctxt.finish() to j
      }

      // we have a single top-level constant
      't'                                                   -> (parseTrue(i, facade) to i + 4)
      'f'                                                   -> (parseFalse(i, facade) to i + 5)
      'n'                                                   -> (parseNull(i, facade) to i + 4)

      // invalid
      else                                                  -> die(i, "expected json value")
    }

  /**
   * Tail-recursive parsing method to do the bulk of JSON parsing.
   *
   * This single method manages parser states, data, etc. Except for
   * parsing non-recursive values (like strings, numbers, and
   * constants) all important work happens in this loop (or in methods
   * it calls, like reset()).
   *
   * Currently the code is optimized to make use of switch
   * statements. Future work should consider whether this is better or
   * worse than manually constructed if/else statements or something
   * else. Also, it may be possible to reorder some cases for speed
   * improvements.
   */
  tailrec fun rparse(
    state: Int,
    j: Int,
    stack: List<FContext<J>>,
    facade: Facade<J>
  ): Pair<J, Int> {
    val i = reset(j)
    checkpoint(state, i, stack)

    val c = at(i)

    return if (c == '\n') {
      newline(i)
      rparse(state, i + 1, stack, facade)
    } else if (c == ' ' || c == '\t' || c == '\r') {
      rparse(state, i + 1, stack, facade)
    } else if (state == DATA) {
      // we are inside an object or array expecting to see data
      if (c == '[') {
        rparse(ARRBEG, i + 1, arrayListOf(facade.arrayContext()) + stack, facade)
      } else if (c == '{') {
        rparse(OBJBEG, i + 1, arrayListOf(facade.objectContext()) + stack, facade)
      } else {
        val ctxt = stack[0]

        if ((c in '0'..'9') || c == '-') {
          val jc = parseNum(i, ctxt, facade)
          rparse(if (ctxt.isObj()) OBJEND else ARREND, jc, stack, facade)
        } else if (c == '"') {
          val jc = parseString(i, ctxt)
          rparse(if (ctxt.isObj()) OBJEND else ARREND, jc, stack, facade)
        } else if (c == 't') {
          ctxt.add(parseTrue(i, facade))
          rparse(if (ctxt.isObj()) OBJEND else ARREND, i + 4, stack, facade)
        } else if (c == 'f') {
          ctxt.add(parseFalse(i, facade))
          rparse(if (ctxt.isObj()) OBJEND else ARREND, i + 5, stack, facade)
        } else if (c == 'n') {
          ctxt.add(parseNull(i, facade))
          rparse(if (ctxt.isObj()) OBJEND else ARREND, i + 4, stack, facade)
        } else {
          die(i, "expected json value")
        }
      }
    } else if ((c == ']' && (state == ARREND || state == ARRBEG)) ||
      (c == '}' && (state == OBJEND || state == OBJBEG))
    ) {
      // we are inside an array or object and have seen a key or a closing
      // brace, respectively.
      if (stack.isEmpty()) {
        error("invalid stack")
      } else {
        val ctxt1 = stack[0]
        val tail = stack.drop(1)

        if (tail.isEmpty()) {
          (ctxt1.finish() to i + 1)
        } else {
          val ctxt2 = tail[0]
          ctxt2.add(ctxt1.finish())
          rparse(if (ctxt2.isObj()) OBJEND else ARREND, i + 1, tail, facade)
        }
      }
    } else if (state == KEY) {
      // we are in an object expecting to see a key.
      if (c == '"') {
        rparse(SEP, parseString(i, stack[0]), stack, facade)
      } else {
        die(i, "expected \"")
      }
    } else if (state == SEP) {
      // we are in an object just after a key, expecting to see a colon.
      if (c == ':') {
        rparse(DATA, i + 1, stack, facade)
      } else {
        die(i, "expected :")
      }
    } else if (state == ARREND) {
      // we are in an array, expecting to see a comma (before more data).
      if (c == ',') {
        rparse(DATA, i + 1, stack, facade)
      } else {
        die(i, "expected ] or ,")
      }
    } else if (state == OBJEND) {
      // we are in an object, expecting to see a comma (before more data).
      if (c == ',') {
        rparse(KEY, i + 1, stack, facade)
      } else {
        die(i, "expected } or ,")
      }
    } else if (state == ARRBEG) {
      // we are starting an array, expecting to see data or a closing bracket.
      rparse(DATA, i, stack, facade)
    } else {
      // we are starting an object, expecting to see a key or a closing brace.
      rparse(KEY, i, stack, facade)
    }
  }

  companion object {
    fun <J> parseUnsafe(s: String, facade: Facade<J>): J =
      StringParser<J>(s).parse(facade)

    fun <J> parseFromString(s: String, facade: Facade<J>): Try<J> =
      Try { StringParser<J>(s).parse(facade) }

    fun <J> parseFromCharSequence(cs: CharSequence, facade: Facade<J>): Try<J> =
      Try { CharSequenceParser<J>(cs).parse(facade) }

    fun <J> parseFromPath(path: String, facade: Facade<J>): Try<J> =
      Try { ChannelParser.fromFile<J>(File(path)).parse(facade) }

    fun <J> parseFromFile(file: File, facade: Facade<J>): Try<J> =
      Try { ChannelParser.fromFile<J>(file).parse(facade) }

    fun <J> parseFromChannel(ch: ReadableByteChannel, facade: Facade<J>): Try<J> =
      Try { ChannelParser.fromChannel<J>(ch).parse(facade) }

    fun <J> parseFromByteBuffer(buf: ByteBuffer, facade: Facade<J>): Try<J> =
      Try { ByteBufferParser<J>(buf).parse(facade) }

    fun <J> async(mode: Mode): AsyncParser<J> =
      AsyncParser(mode)
  }

}