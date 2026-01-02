package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.year2015.entity.WireNode
import aoc.year2015.entity.WireNodeCompanion

class Day07 : AoCFileInput<List<WireNode>, Int>() {
    override val inputFunction
        get() = StructuredInput(
            regex =
                Regex(
                    "(?<left>[\\da-z]+)? *(?<cmd>NOT|AND|OR|LSHIFT|RSHIFT)? *(?<right>[\\da-z]+)? -> (?<name>[a-z]+)",
                ),
            builder = WireNodeCompanion::fromLine,
        )::getStructInput

    private lateinit var connectionsMap: MutableMap<String, WireNode>

    /**
     * This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates!
     * Unfortunately, little Bobby is a little under the recommended age range, and he needs help assembling the
     * circuit.
     *
     * Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535).
     * A signal is provided to each wire by a gate, another wire, or some specific value.
     * Each wire can only get a signal from one source, but can provide its signal to multiple destinations.
     * A gate provides no signal until all of its inputs have a signal.
     *
     * The included instructions booklet describes how to connect the parts together:
     * x AND y -> z means to connect wires x and y to an AND gate, and then connect its output to wire z.
     *
     * For example:
     *
     *      123 -> x means that the signal 123 is provided to wire x.
     *      x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
     *      p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
     *      NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
     *
     * Other possible gates include OR (bitwise OR) and RSHIFT (right-shift).
     *
     * For example, here is a simple circuit:
     *
     *      123 -> x
     *      456 -> y
     *      x AND y -> d
     *      x OR y -> e
     *      x LSHIFT 2 -> f
     *      y RSHIFT 2 -> g
     *      NOT x -> h
     *      NOT y -> i
     * After it is run, these are the signals on the wires:
     *
     *      d: 72
     *      e: 507
     *      f: 492
     *      g: 114
     *      h: 65412
     *      i: 65079
     *      x: 123
     *      y: 456
     *
     * In little Bobby's kit's instructions booklet (provided as your puzzle input),
     * what signal is ultimately provided to wire a?
     */
    override fun processPartOne(): Int {
        loadConnectionsMap()
        return getCalculated("a").toInt()
    }
    // result 3 176 for part 1

    /**
     * Now, take the signal you got on wire a, override wire b to that signal,
     * and reset the other wires (including wire a). What new signal is ultimately provided to wire a?
     */
    override fun processPartTwo(): Int {
        loadConnectionsMap()
        val aResult = getCalculated("a")
        // 3 176 was result for "a"

        loadConnectionsMap()
        connectionsMap["b"] = WireNode(name = "b", left = aResult.toString())
        return getCalculated("a").toInt()
    }
    // result 14 710 for part 2

    fun loadConnectionsMap() {
        connectionsMap =
            input.associateBy { connection ->
                connection.name
            } as MutableMap<String, WireNode>
    }

    fun getCalculated(name: String): UShort = connectionsMap[name]!!.getValue(connectionsMap)
}
