package aoc.year2016

import aoc.common.input.AoCFileInput
import aoc.common.input.StringInput
import aoc.ksp.BaseEntity.getAsChar

class Day07 : AoCFileInput<List<String>, Int>() {
    override val inputFunction: (List<String>) -> List<String>
        get() = StringInput::asIs

    private val abbaPattern = Regex("\\w*(?<a>\\w)(?<b>\\w)\\2\\1\\w*")
    private val hypernetAbbaPattern = Regex("\\[$abbaPattern]")
    private val splitPattern = Regex("(?=\\[)|(?<=])")
    private val abaPattern = Regex("(?=(?<a>\\w)(?<b>\\w)\\1)")

    /**
     * While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7, of course;
     * IPv6 is much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).
     *
     * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any
     * four-character sequence which consists of a pair of two different characters followed by the
     * reverse of that pair, such as xyyx or abba. However, the IP also must not have an ABBA within
     * any hypernet sequences, which are contained by square brackets.
     *
     * For example:
     *     abba[mnop]qrst supports TLS (abba outside square brackets).
     *     abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
     *     aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
     *     ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string).
     *
     * How many IPs in your puzzle input support TLS?
     */
    override fun processPartOne(): Int =
        input.count { ip ->
            val segments = ip.split(splitPattern)
            val hasAbbaInHypernet = segments.any { containsAbbaPattern(hypernetAbbaPattern, it) }
            val hasAbbaOutsideHypernet = segments.any { containsAbbaPattern(abbaPattern, it) }
            !hasAbbaInHypernet && hasAbbaOutsideHypernet
        }
    // result 118 for part 1

    private fun containsAbbaPattern(
        pattern: Regex,
        text: String,
    ): Boolean =
        pattern.findAll(text).any { matchResult ->
            val groups = matchResult.groups
            getAsChar(groups, "a") != getAsChar(groups, "b")
        }

    /**
     * You would also like to know which IPs support SSL (super-secret listening).
     *
     * An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences
     * (outside any square bracketed sections), and a corresponding Byte Allocation Block, or BAB,
     * anywhere in the hypernet sequences. An ABA is any three-character sequence which consists of
     * the same character twice with a different character between them, such as xyx or aba.
     * A corresponding BAB is the same characters but in reversed positions: yxy and bab, respectively.
     *
     * For example:
     *     aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
     *     xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
     *     aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence
     *          is not related, because the interior character must be different).
     *     zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb,
     *          even though zaz and zbz overlap).
     *
     * How many IPs in your puzzle input support SSL?
     */
    override fun processPartTwo(): Int =
        input.count { ip ->
            supportsSsl(ip.split(splitPattern))
        }
    // result 260 for part 2

    private fun supportsSsl(segments: List<String>): Boolean {
        val abaPatterns = mutableSetOf<Pair<Char, Char>>()
        val babPatterns = mutableSetOf<Pair<Char, Char>>()

        segments.forEach { segment ->
            if (isHypernetSegment(segment)) {
                abaPatterns.addAll(extractAbaPairs(segment, reversed = true))
            } else {
                babPatterns.addAll(extractAbaPairs(segment, reversed = false))
            }
        }

        return abaPatterns.any { babPatterns.contains(it) }
    }

    private fun isHypernetSegment(segment: String): Boolean = segment.startsWith('[')

    private fun extractAbaPairs(
        text: String,
        reversed: Boolean,
    ): Set<Pair<Char, Char>> =
        abaPattern
            .findAll(text)
            .map { matchResult ->
                val groups = matchResult.groups
                if (reversed) {
                    Pair(getAsChar(groups, "b"), getAsChar(groups, "a"))
                } else {
                    Pair(getAsChar(groups, "a"), getAsChar(groups, "b"))
                }
            }.filter { (first, second) ->
                first != second
            }.toSet()
}
