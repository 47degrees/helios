package helios.parser

import java.io.File
import java.io.FileInputStream
import java.lang.Integer.bitCount
import java.lang.Integer.highestOneBit
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel

/**
 * Basic file parser.
 *
 * Given a file name this parser opens it, chunks the data, and parses
 * it.
 */
class ChannelParser<J>(val ch: ReadableByteChannel, bufferSize: Int) : SyncParser<J>,
  ByteBasedParser<J> {

  companion object {
    const val DefaultBufferSize = 1048576

    const val ParseAsStringThreshold = 20 * 1048576

    fun <J> fromFile(f: File, bufferSize: Int = DefaultBufferSize): SyncParser<J> =
      if (f.length() < ParseAsStringThreshold) {
        val bytes = ByteArray(f.length().toInt())
        val fis = FileInputStream(f)
        fis.read(bytes)
        StringParser(String(bytes, utf8))
      } else {
        ChannelParser(FileInputStream(f).channel, bufferSize)
      }

    fun <J> fromChannel(
      ch: ReadableByteChannel,
      bufferSize: Int = DefaultBufferSize
    ): ChannelParser<J> =
      ChannelParser(ch, bufferSize)

    /**
     * Given a desired buffer size, find the closest positive
     * power-of-two larger than that size.
     *
     * This method throws an exception if the given values are negative
     * or too large to have a valid power of two.
     */
    fun computeBufferSize(x: Int): Int =
      if (x < 0) {
        throw IllegalArgumentException("negative bufferSize ($x)")
      } else if (x > 0x40000000) {
        throw IllegalArgumentException("bufferSize too large ($x)")
      } else if (bitCount(x) == 1) {
        x
      } else {
        highestOneBit(x) shl 1
      }
  }

  var bufSize: Int = computeBufferSize(bufferSize)
  var mask: Int = bufSize - 1
  var allSize: Int = bufSize * 2

  // these are the actual byte arrays we'll use
  private var curr = ByteArray(bufSize)
  private var next = ByteArray(bufSize)

  // these are the bytecounts for each array
  private var ncurr = ch.read(ByteBuffer.wrap(curr))
  private var nnext = ch.read(ByteBuffer.wrap(next))

  private var line = 0

  override fun line(): Int = line

  private var pos = 0
  override fun newline(i: Int): Unit {
    line += 1; pos = i
  }

  override fun column(i: Int): Int = i - pos

  override fun close(): Unit = ch.close()

  /**
   * Swap the curr and next arrays/buffers/counts.
   *
   * We'll call this in response to certain reset() calls. Specifically, when
   * the index provided to reset is no longer in the 'curr' buffer, we want to
   * clear that data and swap the buffers.
   */
  fun swap(): Unit {
    val tmp = curr; curr = next; next = tmp
    val ntmp = ncurr; ncurr = nnext; nnext = ntmp
  }

  fun grow(): Unit {
    val cc = ByteArray(allSize)
    System.arraycopy(curr, 0, cc, 0, bufSize)
    System.arraycopy(next, 0, cc, bufSize, bufSize)

    curr = cc
    ncurr += nnext
    next = ByteArray(allSize)
    nnext = ch.read(ByteBuffer.wrap(next))

    bufSize = allSize
    mask = allSize - 1
    allSize *= 2
  }

  /**
   * If the cursor 'i' is past the 'curr' buffer, we want to clear the
   * current byte buffer, do a swap, load some more data, and
   * continue.
   */
  override fun reset(i: Int): Int =
    if (i >= bufSize) {
      swap()
      nnext = ch.read(ByteBuffer.wrap(next))
      pos -= bufSize
      i - bufSize
    } else {
      i
    }

  override fun checkpoint(state: Int, i: Int, stack: List<FContext<J>>): Unit = Unit

  /**
   * This is a specialized accessor for the case where our underlying
   * data are bytes not chars.
   */
  override fun byte(i: Int): Byte =
    if (i < bufSize) curr[i]
    else if (i < allSize) next[i and mask]
    else {
      grow(); byte(i)
    }

  /**
   * Reads a byte as a single Char. The byte must be valid ASCII (this
   * method is used to parse JSON values like numbers, constants, or
   * delimiters, which are known to be within ASCII).
   */
  override tailrec fun at(i: Int): Char =
    if (i < bufSize) curr[i].toChar()
    else if (i < allSize) next[i and mask].toChar()
    else {
      grow(); at(i)
    }

  /**
   * Access a byte range as a string.
   *
   * Since the underlying data are UTF-8 encoded, i and j must occur
   * on unicode boundaries. Also, the resulting String is not
   * guaranteed to have length (j - i).
   */
  override tailrec fun at(i: Int, j: Int): CharSequence {
    val len = j - i
    return if (j > allSize) {
      grow()
      at(i, j)
    } else if (j <= bufSize) {
      String(curr, i, len, utf8)
    } else if (i >= bufSize) {
      String(next, i - bufSize, len, utf8)
    } else {
      val arr = ByteArray(len)
      val mid = bufSize - i
      System.arraycopy(curr, i, arr, 0, mid)
      System.arraycopy(next, 0, arr, mid, j - bufSize)
      String(arr, utf8)
    }
  }

  override fun atEof(i: Int): Boolean =
    if (i < bufSize) i >= ncurr
    else i >= (nnext + bufSize)
}
