package helios.parser

/**
 * Facade is a type class that describes how Jawn should construct
 * JSON AST elements of type J.
 *
 * Facade[J] also uses FContext[J] instances, so implementors will
 * usually want to define both.
 */
interface SimpleFacade<J : Any> : Facade<J> {
  fun jarray(vs: List<J>): J
  fun jobject(vs: Map<String, J>): J

  override fun singleContext(): FContext<J> = object : FContext<J> {
    lateinit var value: J
    override fun add(s: CharSequence): Unit {
      value = jstring(s)
    }

    override fun add(v: J): Unit {
      value = v
    }

    override fun finish(): J = value
    override fun isObj(): Boolean = false
  }

  override fun arrayContext(): FContext<J> = object : FContext<J> {
    val vs = mutableListOf<J>()
    override fun add(s: CharSequence): Unit {
      vs += jstring(s)
    }

    override fun add(v: J): Unit {
      vs += v
    }

    override fun finish(): J = jarray(vs.toList())
    override fun isObj(): Boolean = false
  }

  override fun objectContext(): FContext<J> = object : FContext<J> {
    var key: String? = null
    var vs = mutableMapOf<String, J>()
    override fun add(s: CharSequence): Unit =
      if (key == null) {
        key = s.toString()
      } else {
        val k = key
        if (k != null) vs[k] = jstring(s)
        key = null
      }

    override fun add(v: J): Unit {
      val k = key
      if (k != null) vs[k] = v
      key = null
    }

    override fun finish(): J = jobject(vs)
    override fun isObj(): Boolean = true
  }
}