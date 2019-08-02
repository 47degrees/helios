package helios.test.generators

import io.kotlintest.properties.Gen
import java.math.BigDecimal

fun Gen.Companion.alphaStr() =
  Gen.string().map { str -> str.filter(Char::isLetterOrDigit) }.filter(String::isNotBlank)

inline fun <reified A> Gen.Companion.array(genA: Gen<A>): Gen<Array<A>> = Gen.list(genA).map { it.toTypedArray() }

fun Gen.Companion.short(): Gen<Short> = choose(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).map { it.toShort() }

fun Gen.Companion.byte(): Gen<Byte> = choose(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).map { it.toByte() }

fun Gen.Companion.bigDecimal(): Gen<BigDecimal> =
  Gen.double().filterNot { it.isInfinite() || it.isNaN() }.map { it.toBigDecimal() }

fun <A, B, C> Gen.Companion.triple(genA: Gen<A>, genB: Gen<B>, genC: Gen<C>): Gen<Triple<A, B, C>> =
  bind(genA, genB, genC) { a, b, c -> Triple(a, b, c) }
