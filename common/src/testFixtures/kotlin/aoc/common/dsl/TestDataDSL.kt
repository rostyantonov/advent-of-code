package aoc.common.dsl

/**
 * Test case builder for single result tests.
 */
class TestCaseBuilder<T> {
    var input: List<String> = emptyList()
    var expected: T? = null

    /**
     * Set input as a multiline string.
     */
    fun input(content: String) {
        input = content.trimIndent().lines()
    }

    /**
     * Set input as a list of strings.
     */
    fun input(lines: List<String>) {
        input = lines
    }

    /**
     * Set expected result.
     */
    fun expected(value: T) {
        expected = value
    }

    fun build(): TestCase<T> {
        require(expected != null) { "Expected value must be set" }
        return TestCase(input, expected!!)
    }
}

/**
 * Test case with input and expected result.
 */
data class TestCase<T>(
    val input: List<String>,
    val expected: T,
)

/**
 * DSL function to create a test case.
 */
fun <T> testCase(init: TestCaseBuilder<T>.() -> Unit): TestCase<T> {
    val builder = TestCaseBuilder<T>()
    builder.init()
    return builder.build()
}

/**
 * Test suite builder for multiple test cases.
 */
class TestSuiteBuilder<T> {
    private val cases = mutableListOf<TestCase<T>>()

    /**
     * Add a test case to the suite.
     */
    fun case(init: TestCaseBuilder<T>.() -> Unit) {
        cases.add(testCase(init))
    }

    /**
     * Add a test case with inline parameters.
     */
    fun case(
        input: String,
        expected: T,
    ) {
        cases.add(
            TestCase(
                input.trimIndent().lines(),
                expected,
            ),
        )
    }

    /**
     * Add a test case with list input.
     */
    fun case(
        input: List<String>,
        expected: T,
    ) {
        cases.add(TestCase(input, expected))
    }

    fun build(): List<TestCase<T>> = cases.toList()
}

/**
 * DSL function to create a test suite.
 * Example usage:
 * ```
 * val tests = testSuite<Int> {
 *     case {
 *         input("""
 *             line1
 *             line2
 *         """)
 *         expected(42)
 *     }
 *     case("simple input", 10)
 *     case(listOf("a", "b"), 20)
 * }
 * ```
 */
fun <T> testSuite(init: TestSuiteBuilder<T>.() -> Unit): List<TestCase<T>> {
    val builder = TestSuiteBuilder<T>()
    builder.init()
    return builder.build()
}
