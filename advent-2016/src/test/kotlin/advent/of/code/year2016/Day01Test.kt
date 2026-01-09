package advent.of.code.year2016

import advent.of.code.common.Solution
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Test {
    private val solution = Day01()

    @Test
    fun `part 1 - example 1`() {
        assertEquals("5", solution.solvePart1("R2, L3"))
    }

    @Test
    fun `part 1 - example 2`() {
        assertEquals("2", solution.solvePart1("R2, R2, R2"))
    }

    @Test
    fun `part 1 - example 3`() {
        assertEquals("12", solution.solvePart1("R5, L5, R5, R3"))
    }

    @Test
    fun `part 2 - example 1`() {
        assertEquals("4", solution.solvePart2("R8, R4, R4, R8"))
    }
}
