package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.common.input.StructuredPairInput
import aoc.year2015.entity.Molecule
import aoc.year2015.entity.Replacement
import aoc.year2015.entity.ReplacementCompanion

class Day19 : AoCFileInput<Pair<List<Replacement>, Molecule>, Int>() {
    override val inputFunction
        get() =
            StructuredPairInput(
                firstFunction = firstFunction,
                secondFunction = secondFunction,
            )::getStructInput

    private val firstFunction
        get() =
            StructuredInput(
                regex = Regex("(?<what>\\w+) => (?<replace>\\w+)"),
                builder = ReplacementCompanion::fromLine,
            )::getStructInput

    private val secondFunction
        get() =
            StructuredInput(
                regex = Regex("[A-Z][a-df-z]|[A-Z]|e"),
                builder = Molecule::fromLine,
            )::getSingleStructInput

    /**
     * Rudolph the Red-Nosed Reindeer is sick! His nose isn't shining very brightly, and he needs medicine.
     *
     * Red-Nosed Reindeer biology isn't similar to regular reindeer biology; Rudolph is going to need custom-made
     * medicine. Unfortunately, Red-Nosed Reindeer chemistry isn't similar to regular reindeer chemistry, either.
     *
     * The North Pole is equipped with a Red-Nosed Reindeer nuclear fusion/fission plant,
     * capable of constructing any Red-Nosed Reindeer molecule you need. It works by starting with some input
     * molecule and then doing a series of replacements, one per step, until it has the right molecule.
     *
     * However, the machine has to be calibrated before it can be used. Calibration involves determining
     * the number of molecules that can be generated in one step from a given starting point.
     *
     * For example, imagine a simpler machine that supports only the following replacements:
     *
     *      H => HO
     *      H => OH
     *      O => HH
     *
     * Given the replacements above and starting with HOH, the following molecules could be generated:
     *
     *     HOOH (via H => HO on the first H).
     *     HOHO (via H => HO on the second H).
     *     OHOH (via H => OH on the first H).
     *     HOOH (via H => OH on the second H).
     *     HHHH (via O => HH).
     *
     * So, in the example above, there are 4 distinct molecules (not five, because HOOH appears twice)
     * after one replacement from HOH. Santa's favorite molecule, HOHOHO,
     * can become 7 distinct molecules (over nine replacements: six from H, and three from O).
     *
     * The machine replaces without regard for the surrounding characters. For example, given the string H2O,
     * the transition H => OO would result in OO2O.
     *
     * Your puzzle input describes all of the possible replacements and, at the bottom, the medicine molecule
     * for which you need to calibrate the machine. How many distinct molecules can be created after all
     * the different ways you can do one replacement on the medicine molecule?
     */
    override fun processPartOne(): Int {
        val variations = mutableSetOf<String>()

        input.first.forEach { replacement ->
            var position = 0
            while (true) {
                position = input.second.stringValue.indexOf(replacement.what, position)
                if (position == -1) {
                    break
                }
                variations.add(
                    input.second.stringValue.substring(0, position) + replacement.replace +
                        input.second.stringValue.substring(position + replacement.what.length),
                )
                position += replacement.what.length
            }
        }
        return variations.size
    }
    // result 509 for part 1

    /**
     * Now that the machine is calibrated, you're ready to begin molecule fabrication.
     *
     * Molecule fabrication always begins with just a single electron, e, and applying replacements one at a time,
     * just like the ones during calibration.
     *
     * For example, suppose you have the following replacements:
     *
     *      e => H
     *      e => O
     *      H => HO
     *      H => OH
     *      O => HH
     *
     * If you'd like to make HOH, you start with e, and then make the following replacements:
     *
     *     e => O to get O
     *     O => HH to get HH
     *     H => OH (on the second H) to get HOH
     *
     * So, you could make HOH after 3 steps. Santa's favorite molecule, HOHOHO, can be made in 6 steps.
     *
     * How long will it take to make the medicine? Given the available replacements and the medicine molecule
     * in your puzzle input, what is the fewest number of steps to go from e to the medicine molecule?
     */
    override fun processPartTwo(): Int {
        var result = input.second.atoms.size
        for (i in 0 until input.second.atoms.size) {
            val atom: String = input.second.atoms[i].value
            if (atom == "Rn" || atom == "Ar") {
                result--
            } else if (atom == "Y") {
                result -= 2
            }
        }
        return result - 1
    }
    // result 195 for part 2
}
