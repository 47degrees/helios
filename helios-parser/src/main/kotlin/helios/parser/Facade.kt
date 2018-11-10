package helios.parser

/**
 * Facade is a type class that describes how Jawn should construct
 * JSON AST elements of type J.
 *
 * Facade[J] also uses FContext[J] instances, so implementors will
 * usually want to define both.
 */
interface Facade<J> {
  fun singleContext(): FContext<J>
  fun arrayContext(): FContext<J>
  fun objectContext(): FContext<J>

  fun jnull(): J
  fun jfalse(): J
  fun jtrue(): J
  fun jnum(s: CharSequence, decIndex: Int, expIndex: Int): J
  fun jstring(s: CharSequence): J
}

/**
 * FContext is used to construct nested JSON values.
 *
 * The most common cases are to build objects and arrays. However,
 * this type is also used to build a single top-level JSON element, in
 * cases where the entire JSON document consists of "333.33".
 */
interface FContext<J> {
  fun add(s: CharSequence)
  fun add(v: J)
  fun finish(): J
  fun isObj(): Boolean
}
