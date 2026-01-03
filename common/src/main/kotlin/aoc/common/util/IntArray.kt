package aoc.common.util

operator fun Array<IntArray>.set(
    row: Int,
    col: Int,
    value: Int,
) {
    this[row][col] = value
}

operator fun Array<IntArray>.get(
    row: Int,
    col: Int,
): Int = this[row][col]

operator fun Array<IntArray>.set(
    rowRange: IntRange,
    colRange: IntRange,
    value: Int,
) {
    for (row in rowRange) {
        for (col in colRange) {
            this[row, col] = value
        }
    }
}

fun Array<IntArray>.increase(
    rowRange: IntRange,
    colRange: IntRange,
) {
    for (row in rowRange) {
        for (col in colRange) {
            this[row, col]++
        }
    }
}

fun Array<IntArray>.increaseBy(
    rowRange: IntRange,
    colRange: IntRange,
    value: Int,
) {
    for (row in rowRange) {
        for (col in colRange) {
            this[row, col] += value
        }
    }
}

fun Array<IntArray>.decrease(
    rowRange: IntRange,
    colRange: IntRange,
) {
    for (row in rowRange) {
        for (col in colRange) {
            this[row, col]--
        }
    }
}

fun Array<IntArray>.decreaseToZero(
    rowRange: IntRange,
    colRange: IntRange,
) {
    for (row in rowRange) {
        for (col in colRange) {
            if (this[row, col] > 0) {
                this[row, col]--
            }
        }
    }
}
