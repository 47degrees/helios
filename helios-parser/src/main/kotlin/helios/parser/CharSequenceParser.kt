package helios.parser

/**
 * Lazy character sequence parsing.
 *
 * This is similar to StringParser, but acts on character sequences.
 */
class CharSequenceParser<J>(val cs: CharSequence) : SyncParser<J>, CharBasedParser<J> {
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
  override fun at(i: Int): Char = cs[i]
  override fun at(i: Int, j: Int): CharSequence = cs.subSequence(i, j)
  override fun atEof(i: Int): Boolean = i == cs.length
  override fun close(): Unit = Unit
}