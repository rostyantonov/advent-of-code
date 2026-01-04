package aoc.common

import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

interface IAoCSingleTest {
    fun partOneInput(): Stream<Arguments>
}
