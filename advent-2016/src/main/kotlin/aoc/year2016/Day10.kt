package aoc.year2016

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredMultiInput
import aoc.year2016.entity.FactoryLine
import aoc.year2016.entity.FactoryLine.Bot
import aoc.year2016.entity.FactoryLine.Value
import aoc.year2016.entity.FactoryLineCompanion

class Day10 : AoCFileInput<List<FactoryLine>, Int>() {
    override val inputFunction
        get() =
            StructuredMultiInput(
                regexArray =
                    arrayOf(
                        Regex("(?<cmd>value) (?<value>\\d+) goes to (?<typeOut>bot) (?<outId>\\d+)"),
                        Regex(
                            "(?<cmd>bot) (?<botId>\\d+) gives low to (?<lowOut>bot|output) (?<lowId>\\d+)" +
                                " and high to (?<highOut>bot|output) (?<highId>\\d+)",
                        ),
                    ),
                builder = FactoryLineCompanion::fromLine,
            )::getStructInput

    private val initialValues by lazy { input.filterIsInstance<Value>().toMutableList() }
    private val bots by lazy { input.filterIsInstance<Bot>().associateBy { it.botId } }
    private val outputs = mutableMapOf<Int, MutableList<Int>>()

    /**
     * You come upon a factory in which many robots are zooming around handing small microchips to each other.
     *
     * Upon closer examination, you notice that each bot only proceeds when it has two microchips, and once it does,
     * it gives each one to a different bot or puts it in a marked "output" bin. Sometimes, bots take microchips
     * from "input" bins, too.
     *
     * Inspecting one of the microchips, it seems like they each contain a single number; the bots must use some
     * logic to decide what to do with each chip. You access the local control computer and download the bots'
     * instructions (your puzzle input).
     *
     * Some of the instructions specify that a specific-valued microchip should be given to a specific bot;
     * the rest of the instructions indicate what a given bot should do with its lower-value or higher-value chip.
     *
     * For example, consider the following instructions:
     *
     *     value 5 goes to bot 2
     *     bot 2 gives low to bot 1 and high to bot 0
     *     value 3 goes to bot 1
     *     bot 1 gives low to output 1 and high to bot 0
     *     bot 0 gives low to output 2 and high to output 0
     *     value 2 goes to bot 2
     *
     * - Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip and a value-5 chip.
     * - Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its higher one (5) to bot 0.
     * - Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives the value-3 chip to bot 0.
     * - Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
     *
     * In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a value-2 microchip,
     * and output bin 2 contains a value-3 microchip. In this configuration, bot number 2 is responsible
     * for comparing value-5 microchips with value-2 microchips.
     *
     * Based on your instructions, what is the number of the bot that is responsible for comparing value-61
     * microchips with value-17 microchips?
     */
    override fun processPartOne(): Int = findBot(Pair(TARGET_HIGH_CHIP, TARGET_LOW_CHIP))
    // result 157 for part 1

    internal fun findBot(chips: Pair<Int, Int>): Int {
        if (initialValues.isNotEmpty()) {
            initialValues.forEach { value ->
                bots[value.outId]!!.putIn(value.value)
            }
            initialValues.clear()
        }

        do {
            val target = bots.values.firstOrNull { it.hasTwo(chips) }
            if (target == null) {
                bots.values.filter { it.inBin.size >= 2 }.forEach { it.giveToBots(bots, outputs) }
            } else {
                return target.botId
            }
        } while (true)
    }

    /**
     * What do you get if you multiply together the values of one chip in each of outputs 0, 1, and 2?
     */
    override fun processPartTwo(): Int = getOutResult(Triple(OUTPUT_BIN_0, OUTPUT_BIN_1, OUTPUT_BIN_2))
    // result 1085 for part 2

    private fun getOutResult(outBins: Triple<Int, Int, Int>): Int {
        if (initialValues.isNotEmpty()) {
            initialValues.forEach { value ->
                bots[value.outId]!!.putIn(value.value)
            }
            initialValues.clear()
        }

        do {
            if (outputs[outBins.first].isNullOrEmpty() ||
                outputs[outBins.second].isNullOrEmpty() ||
                outputs[outBins.third].isNullOrEmpty()
            ) {
                bots.values.filter { it.inBin.size >= 2 }.forEach { it.giveToBots(bots, outputs) }
            } else {
                return outputs[outBins.first]!!.first() * outputs[outBins.second]!!.first() *
                    outputs[outBins.third]!!.first()
            }
        } while (true)
    }

    companion object {
        private const val TARGET_LOW_CHIP = 17
        private const val TARGET_HIGH_CHIP = 61
        private const val OUTPUT_BIN_0 = 0
        private const val OUTPUT_BIN_1 = 1
        private const val OUTPUT_BIN_2 = 2
    }
}
