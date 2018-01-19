package helios.parser

import java.lang.Integer.*
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.ReadableByteChannel

/**
 * Basic file parser.
 *
 * Given a file name this parser opens it, chunks the data, and parses
 * it.
 */
class ChannelParser<J>(val ch: ReadableByteChannel, bufferSize: Int) : SyncParser<J>, ByteBasedParser<J> {

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

        fun <J> fromChannel(ch: ReadableByteChannel, bufferSize: Int = DefaultBufferSize): ChannelParser<J> =
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

    var Bufsize: Int = ChannelParser.computeBufferSize(bufferSize)
    var Mask: Int = Bufsize - 1
    var Allsize: Int = Bufsize * 2

    // these are the actual byte arrays we'll use
    private var curr = ByteArray(Bufsize)
    private var next = ByteArray(Bufsize)

    // these are the bytecounts for each array
    private var ncurr = ch.read(ByteBuffer.wrap(curr))
    private var nnext = ch.read(ByteBuffer.wrap(next))

    var line = 0

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
        var tmp = curr; curr = next; next = tmp
        var ntmp = ncurr; ncurr = nnext; nnext = ntmp
    }

    fun grow(): Unit {
        val cc = ByteArray(Allsize)
        System.arraycopy(curr, 0, cc, 0, Bufsize)
        System.arraycopy(next, 0, cc, Bufsize, Bufsize)

        curr = cc
        ncurr = ncurr + nnext
        next = ByteArray(Allsize)
        nnext = ch.read(ByteBuffer.wrap(next))

        Bufsize = Allsize
        Mask = Allsize - 1
        Allsize *= 2
    }

    /**
     * If the cursor 'i' is past the 'curr' buffer, we want to clear the
     * current byte buffer, do a swap, load some more data, and
     * continue.
     */
    override fun reset(i: Int): Int =
            if (i >= Bufsize) {
                swap()
                nnext = ch.read(ByteBuffer.wrap(next))
                pos -= Bufsize
                i - Bufsize
            } else {
                i
            }

    override fun checkpoint(state: Int, i: Int, stack: List<FContext<J>>): Unit = Unit

    /**
     * This is a specialized accessor for the case where our underlying
     * data are bytes not chars.
     */
    override fun byte(i: Int): Byte =
            if (i < Bufsize) curr[i]
            else if (i < Allsize) next[i and Mask]
            else {
                grow(); byte(i)
            }

    /**
     * Reads a byte as a single Char. The byte must be valid ASCII (this
     * method is used to parse JSON values like numbers, constants, or
     * delimiters, which are known to be within ASCII).
     */
    tailrec override fun at(i: Int): Char =
            if (i < Bufsize) curr[i].toChar()
            else if (i < Allsize) next[i and Mask].toChar()
            else {
                grow(); at(i)
            }

    /**
     * Access a byte range as a string.
     *
     * Since the underlying data are UTF-8 encoded, i and k must occur
     * on unicode boundaries. Also, the resulting String is not
     * guaranteed to have length (k - i).
     */
    tailrec override fun at(i: Int, k: Int): CharSequence {
        val len = k - i
        return if (k > Allsize) {
            grow()
            at(i, k)
        } else if (k <= Bufsize) {
            String(curr, i, len, utf8)
        } else if (i >= Bufsize) {
            String(next, i - Bufsize, len, utf8)
        } else {
            val arr = ByteArray(len)
            val mid = Bufsize - i
            System.arraycopy(curr, i, arr, 0, mid)
            System.arraycopy(next, 0, arr, mid, k - Bufsize)
            String(arr, utf8)
        }
    }

    override fun atEof(i: Int): Boolean =
            if (i < Bufsize) i >= ncurr
            else i >= (nnext + Bufsize)
}
