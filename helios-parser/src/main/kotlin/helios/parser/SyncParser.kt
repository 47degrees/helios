package helios.parser

/**
 * SyncParser extends Parser to do all parsing synchronously.
 *
 * Most traditional JSON parser are synchronous, and expect to receive
 * all their input before returning. SyncParser[J] still leaves
 * Parser[J]'s methods abstract, but adds a public methods for users
 * to call to actually parse JSON.
 */
interface SyncParser<J> : Parser<J> {

  /**
   * Parse the JSON document into a single JSON value.
   *
   * The parser considers documents like '333', 'true', and '"foo"' to be
   * valid, as well as more traditional documents like [1,2,3,4,5]. However,
   * multiple top-level objects are not allowed.
   */
  fun parse(facade: Facade<J>): J {
    val (value, i) = super.parse(0, facade)
    var j = i
    while (!atEof(j)) {
      when (at(j)) {
        '\n'            -> {
          newline(j); j += 1
        }
        ' ', '\t', '\r' -> j += 1
        else            -> die(j, "expected whitespace or eof")
      }
    }
    if (!atEof(j)) die(j, "expected eof")
    close()
    return value
  }
}