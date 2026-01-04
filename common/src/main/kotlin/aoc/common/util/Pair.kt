package aoc.common.util

fun <First, Second> Pair<First, Second>.reverse(): Pair<Second, First> = Pair(this.second, this.first)
