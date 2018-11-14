package helios.parser

import java.nio.ByteBuffer

/**
 * Basic ByteBuffer parser.
 *
 * This assumes that the provided ByteBuffer is ready to be read. The
 * user is responsible for any necessary flipping/resetting of the
 * ByteBuffer before parsing.
 *
 * The parser makes absolute calls to the ByteBuffer, which will not
 * update its own mutable position fields.
 */
class ByteBufferParser<J>(val src: ByteBuffer) : SyncParser<J>, ByteBasedParser<J> {
  private val start = src.position()
  private val limit = src.limit() - start

  private var lineState = 0
  override fun line(): Int = lineState

  override fun newline(i: Int) {
    lineState += 1
  }

  override fun column(i: Int) = i

  override fun close() {
    src.position(src.limit())
  }

  override fun reset(i: Int): Int = i
  override fun checkpoint(state: Int, i: Int, stack: List<FContext<J>>) {}
  override fun byte(i: Int): Byte = src.get(i + start)
  override fun at(i: Int): Char = src.get(i + start).toChar()

  override fun at(i: Int, j: Int): CharSequence {
    val len = j - i
    val arr = ByteArray(len)
    src.position(i + start)
    src.get(arr, 0, len)
    src.position(start)
    return String(arr, utf8)
  }

  override fun atEof(i: Int) = i >= limit
}