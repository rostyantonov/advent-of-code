package aoc.common.util

fun Number.formatted(): String =
    toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
