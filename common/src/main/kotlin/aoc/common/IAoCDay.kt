package aoc.common

interface IAoCDay<out Result> {
    var rawInput: List<String>

    fun processPartOne(): Result

    fun processPartTwo(): Result
}
