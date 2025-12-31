package aoc.common.util

operator fun Array<BooleanArray>.set(
    xPos: Int,
    yPos: Int,
    value: Boolean,
) {
    this[yPos][xPos] = value
}

operator fun Array<BooleanArray>.get(
    xPos: Int,
    yPos: Int,
): Boolean = this[yPos][xPos]

operator fun Array<BooleanArray>.set(
    xRange: IntRange,
    yRange: IntRange,
    value: Boolean,
) {
    for (xPos in xRange) {
        for (yPos in yRange) {
            this[xPos, yPos] = value
        }
    }
}

fun Array<BooleanArray>.invert(
    xRange: IntRange,
    yRange: IntRange,
) {
    for (xPos in xRange) {
        for (yPos in yRange) {
            this[xPos, yPos] = !this[xPos, yPos]
        }
    }
}
