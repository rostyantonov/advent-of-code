package aoc.common

import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

interface IAoCDoubleTest : IAoCSingleTest {
    fun partTwoInput(): Stream<Arguments>
}
