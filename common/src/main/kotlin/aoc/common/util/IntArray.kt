package aoc.common.util

operator fun Array<IntArray>.set(
    xPos: Int,
    yPos: Int,
    value: Int,
) {
    this[yPos][xPos] = value
}

operator fun Array<IntArray>.get(
    xPos: Int,
    yPos: Int,
): Int = this[yPos][xPos]

operator fun Array<IntArray>.set(
    xRange: IntRange,
    yRange: IntRange,
    value: Int,
) {
    for (xPos in xRange) {
        for (yPos in yRange) {
            this[xPos, yPos] = value
        }
    }
}

fun Array<IntArray>.increase(
    xRange: IntRange,
    yRange: IntRange,
) {
    for (xPos in xRange) {
        for (yPos in yRange) {
            this[xPos, yPos]++
        }
    }
}

fun Array<IntArray>.increaseBy(
    xRange: IntRange,
    yRange: IntRange,
    value: Int,
) {
    for (xPos in xRange) {
        for (yPos in yRange) {
            this[xPos, yPos] += value
        }
    }
}

fun Array<IntArray>.decrease(
    xRange: IntRange,
    yRange: IntRange,
) {
    for (xPos in xRange) {
        for (yPos in yRange) {
            this[xPos, yPos]--
        }
    }
}

fun Array<IntArray>.decreaseToZero(
    xRange: IntRange,
    yRange: IntRange,
) {
    for (xPos in xRange) {
        for (yPos in yRange) {
            if (this[xPos, yPos] > 0) {
                this[xPos, yPos]--
            }
        }
    }
}
