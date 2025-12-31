package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StringInput

class Day05 : AoCFileInput<List<String>, Int>() {
    override val inputFunction
        get() = StringInput::asIs

    // Notes:
    // \\w - any word character
    // \\w{2} - repeating times
    // (\\w) - creating group
    // \\1 reference to first group
    private val vowels: Regex = Regex("[aeiou]")
    private val sameChars: Regex = Regex("(\\w)\\1")

    private val badPairs: Regex = Regex("ab|cd|pq|xy")
    private val twoSamePairs: Regex = Regex("(\\w{2})\\w*\\1")
    private val charBetweenSameChars: Regex = Regex("(\\w)\\w\\1")

    /**
     * Santa needs help figuring out which strings in his text file are naughty or nice.
     *
     * A nice string is one with all of the following properties:
     * * It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
     * * It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
     * * It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
     *
     * For example:
     *
     *      ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...),
     *        a double letter (...dd...), and none of the disallowed substrings.
     *      aaa is nice because it has at least three vowels and a double letter,
     *        even though the letters used by different rules overlap.
     *      jchzalrnumimnmhp is naughty because it has no double letter.
     *      haegwjzuvuyypxyu is naughty because it contains the string xy.
     *      dvszwmarrgswjxmb is naughty because it contains only one vowel.
     *
     * How many strings are nice?
     */
    override fun processPartOne(): Int =
        input.count { line ->
            checkPartOneIsNice(line)
        }
    // result 236 for part 1

    /**
     * Realizing the error of his ways,
     * Santa has switched to a better model of determining whether a string is naughty or nice.
     * None of the old rules apply, as they are all clearly ridiculous.
     *
     * Now, a nice string is one with all of the following properties:
     * * It contains a pair of any two letters that appears at least twice in the string without overlapping,
     *   like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
     * * It contains at least one letter which repeats with exactly one letter between them,
     *   like xyx, abcdefeghi (efe), or even aaa.
     *
     * For example:
     *
     *      qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats with
     *        exactly one letter between them (zxz).
     *      xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between,
     *        even though the letters used by each rule overlap.
     *      uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
     *      ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo), but no pair that
     *        appears twice.
     *
     * How many strings are nice under these new rules?
     */
    override fun processPartTwo(): Int =
        input.count { line ->
            checkPartTwoIsNice(line)
        }
    // result 51 for part 2

    fun checkPartOneIsNice(line: String): Boolean {
        val firstCheck = vowels.findAll(line).count() > 2
        val secondCheck = line.contains(sameChars)
        val thirdCheck = !line.contains(badPairs)
        return firstCheck && secondCheck && thirdCheck
    }

    fun checkPartTwoIsNice(line: String): Boolean {
        val firstCheck = line.contains(twoSamePairs)
        val secondCheck = line.contains(charBetweenSameChars)
        return firstCheck && secondCheck
    }
}
