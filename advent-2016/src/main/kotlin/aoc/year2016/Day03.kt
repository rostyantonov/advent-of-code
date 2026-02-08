package aoc.year2016

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.year2016.entity.Triangle
import aoc.year2016.entity.TriangleCompanion

class Day03 : AoCFileInput<List<Triangle>, Int>() {
    override val inputFunction: (List<String>) -> List<Triangle>
        get() =
            StructuredInput(
                regex =
                    Regex(
                        " *(?<aSide>\\d+)" +
                            " +(?<bSide>\\d+)" +
                            " +(?<cSide>\\d+)",
                    ),
                builder = TriangleCompanion::fromLine,
            )::getStructInput

    /**
     * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that
     * makes up this part of Easter Bunny HQ. This must be a graphic design department;
     * the walls are covered in specifications for triangles.
     *
     * Or are they?
     *
     * The design document gives the side lengths of each triangle it describes, but... 5 10 25?
     * Some of these aren't triangles. You can't help but mark the impossible ones.
     *
     * In a valid triangle, the sum of any two sides must be larger than the remaining side.
     * For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
     *
     * In your puzzle input, how many of the listed triangles are possible?
     */
    override fun processPartOne(): Int = input.count(Triangle::isPossible)
    // result 917 for part 1

    /**
     * Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified
     * in groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.
     *
     * For example, given the following specification, numbers with the same hundreds digit would be part
     * of the same triangle:
     *
     *      101 301 501
     *      102 302 502
     *      103 303 503
     *      201 401 601
     *      202 402 602
     *      203 403 603
     *
     * In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
     *
     * Your puzzle answer was 1649.
     */
    override fun processPartTwo(): Int =
        input
            .chunked(3)
            .flatMap { (row0, row1, row2) ->
                listOf(
                    Triangle(row0.aSide, row1.aSide, row2.aSide),
                    Triangle(row0.bSide, row1.bSide, row2.bSide),
                    Triangle(row0.cSide, row1.cSide, row2.cSide),
                )
            }.count(Triangle::isPossible)
    // result 1649 for part 2
}
