package aoc.year2017

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.year2017.entity.Instruction
import aoc.year2017.entity.InstructionCompanion

class Day08 : AoCFileInput<List<Instruction>, Int>() {
    override val inputFunction
        get() =
            StructuredInput(
                regex =
                    Regex(
                        "(?<register>\\w+) (?<operation>inc|dec) (?<amount>-?\\d+) if (?<condRegister>\\w+) (?<condOperator>[><=!]+) (?<condAmount>-?\\d+)",
                    ),
                builder = InstructionCompanion::fromLine,
            )::getStructInput

    private val registers = mutableMapOf<String, Int>()
    private var highestValueEver = 0
    private var calculated = false

    /**
     * You receive a signal directly from the CPU. Because of your recent assistance with jump instructions,
     * it would like you to compute the result of a series of unusual register instructions.
     *
     * Each instruction consists of several parts: the register to modify, whether to increase or decrease
     * that register's value, the amount by which to increase or decrease it, and a condition. If the condition
     * fails, skip the instruction without modifying the register. The registers all start at 0. The
     * instructions look like this:
     *
     *      b inc 5 if a > 1
     *      a inc 1 if b < 5
     *      c dec -10 if a >= 1
     *      c inc -20 if c == 10
     *
     * These instructions would be processed as follows:
     *
     *      Because a starts at 0, it is not greater than 1, and so b is not modified.
     *      a is increased by 1 (to 1) because b is less than 5 (it is 0).
     *      c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
     *      c is increased by -20 (to -10) because c is equal to 10.
     *
     * After this process, the largest value in any register is 1.
     *
     * You might also encounter <= (less than or equal to) or != (not equal to). However, the CPU doesn't have
     * the bandwidth to tell you what all the registers are named, and leaves that to you to determine.
     *
     * What is the largest value in any register after completing the instructions in your puzzle input?
     */
    override fun processPartOne(): Int = solveTheTask(true)
    // result 3 089 for part 1

    /**
     * To be safe, the CPU also needs to know the highest value held in any register during this process so that
     * it can decide how much memory to allocate to these operations. For example, in the above instructions,
     * the highest value ever held was 10 (in register c after the third instruction was evaluated).
     */
    override fun processPartTwo(): Int = solveTheTask(false)
    // result 5 391 for part 2

    private fun solveTheTask(partOne: Boolean): Int {
        if (!calculated) {
            input.forEach { instruction ->
                val value = registers.getOrPut(instruction.condRegister) { 0 }
                val conditionResult =
                    when (instruction.condOperator) {
                        ">" -> value > instruction.condAmount
                        "<" -> value < instruction.condAmount
                        ">=" -> value >= instruction.condAmount
                        "<=" -> value <= instruction.condAmount
                        "==" -> value == instruction.condAmount
                        "!=" -> value != instruction.condAmount
                        else -> throw IllegalArgumentException("Unknown operator: ${instruction.condOperator}")
                    }
                if (conditionResult) {
                    val registerValue = registers.get(instruction.register) ?: 0
                    registers[instruction.register] = registerValue +
                        if (instruction.operation == "inc") instruction.amount else -instruction.amount
                    highestValueEver = maxOf(highestValueEver, registers[instruction.register]!!)
                }
            }
            calculated = true
        }
        return if (partOne) registers.maxBy { it.value }.value else highestValueEver
    }
}
