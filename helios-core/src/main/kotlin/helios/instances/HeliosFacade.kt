package helios.instances

import helios.core.*
import helios.parser.FContext
import helios.parser.Facade

object HeliosFacade : Facade<Json> {
  override fun singleContext(): FContext<Json> = object : FContext<Json> {
    lateinit var value: Json
    override fun add(s: CharSequence): Unit {
      value = jstring(s.toString())
    }

    override fun add(v: Json): Unit {
      value = v
    }

    override fun finish(): Json = value
    override fun isObj(): Boolean = false
  }

  override fun arrayContext(): FContext<Json> = object : FContext<Json> {
    private val vs = arrayListOf<Json>()
    override fun add(s: CharSequence): Unit {
      vs += jstring(s.toString())
    }

    override fun add(v: Json): Unit {
      vs += v
    }

    override fun finish(): Json = Json.fromValues(vs)
    override fun isObj(): Boolean = false
  }

  override fun objectContext(): FContext<Json> = object : FContext<Json> {
    var key: String? = null
    private val m = LinkedHashMap<String, Json>()

    override fun add(s: CharSequence): Unit =
      if (key == null) {
        key = s.toString()
      } else {
        val k = key
        if (k != null) m.put(k, jstring(s))
        key = null
      }

    override fun add(v: Json): Unit {
      val k = key
      if (k != null) m.put(k, v)
      key = null
    }

    override fun finish(): Json = JsObject(m)
    override fun isObj(): Boolean = true
  }

  override fun jnull(): Json = JsNull

  override fun jfalse(): Json = JsFalse

  override fun jtrue(): Json = JsTrue

  override fun jnum(s: CharSequence, decIndex: Int, expIndex: Int): Json =
    if (decIndex < 0 && expIndex < 0) {
      JsNumber.fromIntegralStringUnsafe(s.toString())
    } else {
      JsNumber.fromDecimalStringUnsafe(s.toString())
    }

  override fun jstring(s: CharSequence): Json = JsString(s)

}