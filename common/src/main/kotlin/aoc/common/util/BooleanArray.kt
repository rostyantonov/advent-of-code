package aoc.common.util

import aoc.common.exception.UnsupportedTypeException

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

fun Array<BooleanArray>.rotateBottom(
    column: Int,
    amount: Int,
) {
    val realAmount = amount % this.size
    var columnList = this.map { it[column] }
    columnList = columnList.takeLast(realAmount) + columnList.dropLast(realAmount)
    this.forEachIndexed { index, row ->
        row[column] = columnList[index]
    }
}

fun Array<BooleanArray>.rotateRight(
    row: Int,
    amount: Int,
) {
    val realAmount = amount % this[row].size
    this[row] = (this[row].takeLast(realAmount) + this[row].dropLast(realAmount)).toBooleanArray()
}

fun Array<BooleanArray>.toDisplayChars(
    charWidth: Int,
    charHeight: Int,
): String {
    val result = StringBuilder()
    (first().indices step charWidth).forEach { charPosition ->
        val charDisplay =
            mapIndexed { index, row ->
                row
                    .sliceArray(charPosition until charPosition + charWidth)
                    .toIntValue() shl (index * charWidth)
            }.sum()
        if (charWidth == 5 && charHeight == 6) {
            result.append(getChar5on6(charDisplay))
        } else {
            throw UnsupportedTypeException(
                "Char display size: width=$charWidth and height=$charHeight is not supported.",
            )
        }
    }
    return result.toString()
}

private fun BooleanArray.toIntValue(): Int =
    mapIndexed { index, value ->
        (if (value) 1 else 0) shl index
    }.sum()
