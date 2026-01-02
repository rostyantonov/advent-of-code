package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.year2015.entity.Aunt

class Day16 : AoCFileInput<List<Aunt>, Int>() {
    override val inputFunction
        get() =
            StructuredInput(
                regex =
                    Regex(
                        "Sue (?<index>\\d+): " +
                            "(?<param1name>\\w+): (?<param1val>\\d+), " +
                            "(?<param2name>\\w+): (?<param2val>\\d+), " +
                            "(?<param3name>\\w+): (?<param3val>\\d+)",
                    ),
                builder = Aunt::fromLine,
            )::getStructInput

    private val matchingAunt =
        mutableMapOf<String, Int>().apply {
            put("children", 3)
            put("cats", 7)
            put("samoyeds", 2)
            put("pomeranians", 3)
            put("akitas", 0)
            put("vizslas", 0)
            put("goldfish", 5)
            put("trees", 3)
            put("cars", 2)
            put("perfumes", 1)
        }

    /**
     * Your Aunt Sue has given you a wonderful gift, and you'd like to send her a thank you card.
     * However, there's a small problem: she signed it "From, Aunt Sue".
     *
     * You have 500 Aunts named "Sue".
     *
     * So, to avoid sending the card to the wrong person, you need to figure out which Aunt Sue
     * (which you conveniently number 1 to 500, for sanity) gave you the gift. You open the present and,
     * as luck would have it, good ol' Aunt Sue got you a My First Crime Scene Analysis Machine!
     * Just what you wanted. Or needed, as the case may be.
     *
     * The My First Crime Scene Analysis Machine (MFCSAM for short) can detect a few specific compounds
     * in a given sample, as well as how many distinct kinds of those compounds there are.
     * According to the instructions, these are what the MFCSAM can detect:
     *
     *     children, by human DNA age analysis.
     *     cats. It doesn't differentiate individual breeds.
     *     Several seemingly random breeds of dog: samoyeds, pomeranians, akitas, and vizslas.
     *     goldfish. No other kinds of fish.
     *     trees, all in one group.
     *     cars, presumably by exhaust or gasoline or something.
     *     perfumes, which is handy, since many of your Aunts Sue wear a few kinds.
     *
     * In fact, many of your Aunts Sue have many of these. You put the wrapping from the gift into the MFCSAM.
     * It beeps inquisitively at you a few times and then prints out a message on ticker tape:
     *
     * children: 3
     * cats: 7
     * samoyeds: 2
     * pomeranians: 3
     * akitas: 0
     * vizslas: 0
     * goldfish: 5
     * trees: 3
     * cars: 2
     * perfumes: 1
     *
     * You make a list of the things you can remember about each Aunt Sue.
     * Things missing from your list aren't zero - you simply don't remember the value.
     *
     * What is the number of the Sue that got you the gift?
     */
    override fun processPartOne(): Int =
        input
            .first { currentAunt ->
                var result = true
                currentAunt.param.keys.forEach { key ->
                    if (currentAunt.param[key] != matchingAunt[key]) {
                        result = false
                    }
                }
                result
            }.index
    // result 103 for part 1

    /**
     * As you're about to send the thank you note, something in the MFCSAM's instructions catches your eye.
     * Apparently, it has an outdated retroencabulator, and so the output from the machine isn't exact values
     * - some of them indicate ranges.
     *
     * In particular, the cats and trees readings indicates that there are greater than that many
     * (due to the unpredictable nuclear decay of cat dander and tree pollen),
     * while the pomeranians and goldfish readings indicate that there are fewer than that many
     * (due to the modial interaction of magnetoreluctance).
     *
     * What is the number of the real Aunt Sue?
     */
    override fun processPartTwo(): Int =
        input
            .first { currentAunt ->

                var result = true
                currentAunt.param.keys.forEach { key ->
                    if (key == "cats" || key == "trees") {
                        if (currentAunt.param[key]!! > matchingAunt[key]!!) {
                            return@forEach
                        } else {
                            result = false
                        }
                    } else if (key == "pomeranians" || key == "goldfish") {
                        if (currentAunt.param[key]!! < matchingAunt[key]!!) {
                            return@forEach
                        } else {
                            result = false
                        }
                    } else if (currentAunt.param[key] == matchingAunt[key]) {
                        return@forEach
                    } else {
                        result = false
                    }
                }
                result
            }.index
    // result 405 for part 2
}
