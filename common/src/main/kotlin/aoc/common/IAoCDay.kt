package aoc.common

interface IAoCDay<out Result : Any> {
    var rawInput: List<String>

    fun processPartOne(): Result

    fun processPartTwo(): Result
}
