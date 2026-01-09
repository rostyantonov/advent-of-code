package advent.of.code.year2015

import advent.of.code.common.Solution
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {
    private val solution = Day01()

    @Test
    fun `part 1 - example 1`() {
        assertEquals("0", solution.solvePart1("(())"))
    }

    @Test
    fun `part 1 - example 2`() {
        assertEquals("0", solution.solvePart1("()()"))
    }

    @Test
    fun `part 1 - example 3`() {
        assertEquals("3", solution.solvePart1("((("))
    }

    @Test
    fun `part 1 - example 4`() {
        assertEquals("3", solution.solvePart1("(()(()("))
    }

    @Test
    fun `part 1 - example 5`() {
        assertEquals("-1", solution.solvePart1("))("))
    }

    @Test
    fun `part 2 - example 1`() {
        assertEquals("1", solution.solvePart2(")"))
    }

    @Test
    fun `part 2 - example 2`() {
        assertEquals("5", solution.solvePart2("()())"))
    }
}
