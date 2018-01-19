package helios.parser

/**
 * NullFacade discards all JSON AST information.
 *
 * This is the simplest possible facade. It could be useful for
 * checking JSON for correctness (via parsing) without worrying about
 * saving the data.
 *
 * It will always return () on any successful parse, no matter the
 * content.
 */
object NullFacade : Facade<Unit> {

    data class NullContext(val isObject: Boolean) : FContext<Unit> {
        override fun add(s: CharSequence): Unit = Unit
        override fun add(v: Unit): Unit = Unit
        override fun finish(): Unit = Unit
        override fun isObj(): Boolean = isObject
    }

    private val singleContext = NullContext(false)
    private val arrayContext = NullContext(false)
    private val objectContext = NullContext(true)

    override fun singleContext(): FContext<Unit> = singleContext
    override fun arrayContext(): FContext<Unit> = arrayContext
    override fun objectContext(): FContext<Unit> = objectContext

    override fun jnull(): Unit = Unit
    override fun jfalse(): Unit = Unit
    override fun jtrue(): Unit = Unit
    override fun jnum(s: CharSequence, decIndex: Int, expIndex: Int): Unit = Unit
    override fun jstring(s: CharSequence): Unit = Unit
}
