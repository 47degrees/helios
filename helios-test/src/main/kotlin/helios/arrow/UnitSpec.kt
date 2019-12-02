package helios.arrow

import helios.arrow.laws.Law
import io.kotest.TestCase
import io.kotest.TestType
import io.kotest.specs.StringSpec


/**
 * Base class for unit tests
 */
abstract class UnitSpec : StringSpec() {

    private val lawTestCases = mutableListOf<TestCase>()

    fun testLaws(vararg laws: List<Law>): List<TestCase> = laws
        .flatMap { list: List<Law> -> list.asIterable() }
        .distinctBy { law: Law -> law.name }
        .map { law: Law ->
            val lawTestCase = createTestCase(law.name, law.test, defaultTestCaseConfig, TestType.Test)
            lawTestCases.add(lawTestCase)
            lawTestCase
        }

    override fun testCases(): List<TestCase> = super.testCases() + lawTestCases
}
