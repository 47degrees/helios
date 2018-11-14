package helios.parser

/**
 * Basic in-memory string parsing.
 *
 * This is probably the simplest Parser implementation, since there is
 * no UTF-8 decoding, and the data is already fully available.
 *
 * This parser is limited to the maximum string size (~2G). Obviously
 * for large JSON documents it's better to avoid using this parser and
 * go straight from disk, to avoid having to load the whole thing into
 * memory at once. So this limit will probably not be a problem in
 * practice.
 */
class StringParser<J>(val s: String) : SyncParser<J>, CharBasedParser<J> {
  private var line = 0
  private val charBuilder = CharBuilder()
  override fun charBuilder(): CharBuilder = charBuilder
  override fun line(): Int = line
  override fun column(i: Int): Int = i
  override fun newline(i: Int): Unit {
    line += 1
  }

  override fun reset(i: Int): Int = i
  override fun checkpoint(state: Int, i: Int, stack: List<FContext<J>>): Unit = Unit
  override fun at(i: Int): Char = s[i]
  override fun at(i: Int, j: Int): CharSequence = s.substring(i, j)
  override fun atEof(i: Int): Boolean = i == s.length
  override fun close(): Unit = Unit
}