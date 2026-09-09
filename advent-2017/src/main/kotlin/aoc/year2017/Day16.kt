package aoc.year2017

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredMultiInput
import aoc.year2017.entity.DanceMove
import aoc.year2017.entity.DanceMoveCompanion

class Day16 : AoCFileInput<List<DanceMove>, String>() {
    override val inputFunction
        get() = StructuredMultiInput(
            regexArray =
                arrayOf(
                    Regex("(?<type>s)(?<steps>\\d+)"),
                    Regex("(?<type>x)(?<from>\\d+)/(?<to>\\d+)"),
                    Regex("(?<type>p)(?<from>\\w)/(?<to>\\w)"),
                ),
            builder = DanceMoveCompanion::fromLine,
            splitBy = ",",
        )::getFirstLineStructInput

    /**
     * You come upon a very unusual sight; a group of programs here appear to be dancing.
     *
     * There are sixteen programs in total, named a through p. They start by standing in a line: a stands in
     * position 0, b stands in position 1, and so on until p, which stands in position 15.
     *
     * The programs' dance consists of a sequence of dance moves:
     *
     *     Spin, written sX, makes X programs move from the end to the front, but maintain their order otherwise.
     *              (For example, s3 on abcde produces cdeab).
     *     Exchange, written xA/B, makes the programs at positions A and B swap places.
     *     Partner, written pA/B, makes the programs named A and B swap places.
     *
     * For example, with only five programs standing in a line (abcde), they could do the following dance:
     *
     *     s1, a spin of size 1: eabcd.
     *     x3/4, swapping the last two programs: eabdc.
     *     pe/b, swapping programs e and b: baedc.
     *
     * After finishing their dance, the programs end up in order baedc.
     *
     * You watch the dance for a while and record their dance moves (your puzzle input). In what order are the
     * programs standing after their dance?
     */
    override fun processPartOne(): String = makeADance(16)
    // result pkgnhomelfdibjac for part 1

    internal fun makeADance(size: Int): String {
        val charArray = CharArray(size) { i -> ('a' + i) }
        input.forEach { move -> move.execute(charArray) }
        return String(charArray)
    }

    /**
     * Now that you're starting to get a feel for the dance moves, you turn your attention to the dance as a whole.
     *
     * Keeping the positions they ended up in from their previous dance, the programs perform it again and again:
     * including the first dance, a total of one billion (1000000000) times.
     *
     * In the example above, their second dance would begin with the order baedc, and use the same dance moves:
     *
     *     s1, a spin of size 1: cbaed.
     *     x3/4, swapping the last two programs: cbade.
     *     pe/b, swapping programs e and b: ceadb.
     *
     * In what order are the programs standing after their billion dances?
     */
    override fun processPartTwo(): String {
        val charArray = CharArray(16) { i -> ('a' + i) }
        val visited = mutableSetOf<String>()
        repeat(1000000000) {
            if (!visited.add(String(charArray))) {
                val remainingIterations = 1000000000 % visited.size
                repeat(remainingIterations) { _ ->
                    input.forEach { move -> move.execute(charArray) }
                }
                return String(charArray)
            }
            input.forEach { move -> move.execute(charArray) }
        }
        return String(charArray)
    }
    // result pogbjfihclkemadn for part 2
}
