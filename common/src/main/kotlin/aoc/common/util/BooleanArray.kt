package aoc.common.util

operator fun Array<BooleanArray>.set(
    row: Int,
    col: Int,
    value: Boolean,
) {
    this[row][col] = value
}

operator fun Array<BooleanArray>.get(
    row: Int,
    col: Int,
): Boolean = this[row][col]

operator fun Array<BooleanArray>.set(
    rowRange: IntRange,
    colRange: IntRange,
    value: Boolean,
) {
    for (row in rowRange) {
        for (col in colRange) {
            this[row, col] = value
        }
    }
}

fun Array<BooleanArray>.invert(
    rowRange: IntRange,
    colRange: IntRange,
) {
    for (row in rowRange) {
        for (col in colRange) {
            this[row, col] = !this[row, col]
        }
    }
}
