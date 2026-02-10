package aoc.year2016

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.common.util.clone
import aoc.year2016.entity.Floor
import aoc.year2016.entity.FloorsState
import aoc.year2016.entity.PowerEntity.Generator
import aoc.year2016.entity.PowerEntity.Microchip
import kotlin.collections.mutableSetOf

class Day11 : AoCFileInput<List<Floor>, Int>() {
    override val inputFunction
        get() =
            StructuredInput(
                regex =
                    Regex(
                        "(?<powerEntity>(?<name>\\w+)(?:-compatible)? (?<type>generator|microchip))",
                    ),
                builder = Floor::fromLine,
            )::getStructInput

    /**
     * You come upon a column of four floors that have been entirely sealed off from the rest of the building
     * except for a small dedicated lobby. There are some radiation warnings and a big sign which reads
     * "Radioisotope Testing Facility".
     *
     * According to the project status board, this facility is currently being used to experiment with
     * Radioisotope Thermoelectric Generators (RTGs, or simply "generators") that are designed to be paired
     * with specially-constructed microchips. Basically, an RTG is a highly radioactive rock that generates
     * electricity through heat.
     *
     * The experimental RTGs have poor radiation containment, so they're dangerously radioactive. The chips
     * are prototypes and don't have normal radiation shielding, but they do have the ability to generate an
     * electromagnetic radiation shield when powered. Unfortunately, they can only be powered by their
     * corresponding RTG. An RTG powering a microchip is still dangerous to other microchips.
     *
     * In other words, if a chip is ever left in the same area as another RTG, and it's not connected to its
     * own RTG, the chip will be fried. Therefore, it is assumed that you will follow procedure and keep chips
     * connected to their corresponding RTG when they're in the same room, and away from other RTGs otherwise.
     *
     * These microchips sound very interesting and useful to your current activities, and you'd like to try to
     * retrieve them. The fourth floor of the facility has an assembling machine which can make a self-contained,
     * shielded computer for you to take with you - that is, if you can bring it all of the RTGs and microchips.
     *
     * Within the radiation-shielded part of the facility (in which it's safe to have these pre-assembly RTGs),
     * there is an elevator that can move between the four floors. Its capacity rating means it can carry at
     * most yourself and two RTGs or microchips in any combination. (They're rigged to some heavy diagnostic
     * equipment - the assembling machine will detach it for you.) As a security measure, the elevator will
     * only function if it contains at least one RTG or microchip. The elevator always stops on each floor to
     * recharge, and this takes long enough that the items within it and the items on that floor can irradiate
     * each other. (You can prevent this if a Microchip and its Generator end up on the same floor in this way,
     * as they can be connected while the elevator is recharging.)
     *
     * You make some notes of the locations of each component of interest (your puzzle input). Before you don
     * a hazmat suit and start moving things around, you'd like to have an idea of what you need to do.
     *
     * When you enter the containment area, you and the elevator will start on the first floor.
     *
     * For example, suppose the isolated area has the following arrangement:
     *
     * The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
     * The second floor contains a hydrogen generator.
     * The third floor contains a lithium generator.
     * The fourth floor contains nothing relevant.
     *
     * As a diagram (F# for a Floor number, E for Elevator, H for Hydrogen, L for Lithium, M for Microchip,
     * and G for Generator), the initial state looks like this:
     *
     *      F4 .  .  .  .  .
     *      F3 .  .  .  LG .
     *      F2 .  HG .  .  .
     *      F1 E  .  HM .  LM
     *
     * Then, to get everything up to the assembling machine on the fourth floor, the following steps could be taken:
     *
     * *    Bring the Hydrogen-compatible Microchip to the second floor, which is safe because it can get power from
     *      the Hydrogen Generator:
     *
     *
     *      F4 .  .  .  .  .
     *      F3 .  .  .  LG .
     *      F2 E  HG HM .  .
     *      F1 .  .  .  .  LM
     *
     * *    Bring both Hydrogen-related items to the third floor, which is safe because the Hydrogen-compatible
     *      microchip is getting power from its generator:
     *
     *
     *      F4 .  .  .  .  .
     *      F3 E  HG HM LG .
     *      F2 .  .  .  .  .
     *      F1 .  .  .  .  LM
     *
     * *    Leave the Hydrogen Generator on floor three, but bring the Hydrogen-compatible Microchip back down
     *      with you so you can still use the elevator:
     *
     *      F4 .  .  .  .  .
     *      F3 .  HG .  LG .
     *      F2 E  .  HM .  .
     *      F1 .  .  .  .  LM
     *
     * *    At the first floor, grab the Lithium-compatible Microchip, which is safe because Microchips don't affect
     *      each other:
     *
     *
     *      F4 .  .  .  .  .
     *      F3 .  HG .  LG .
     *      F2 .  .  .  .  .
     *      F1 E  .  HM .  LM
     *
     * *    Bring both Microchips up one floor, where there is nothing to fry them:
     *
     *
     *      F4 .  .  .  .  .
     *      F3 .  HG .  LG .
     *      F2 E  .  HM .  LM
     *      F1 .  .  .  .  .
     *
     * *    Bring both Microchips up again to floor three, where they can be temporarily connected to their
     *      corresponding generators while the elevator recharges, preventing either of them from being fried:
     *
     *
     *      F4 .  .  .  .  .
     *      F3 E  HG HM LG LM
     *      F2 .  .  .  .  .
     *      F1 .  .  .  .  .
     *
     * *    Bring both Microchips to the fourth floor:
     *
     *
     *      F4 E  .  HM .  LM
     *      F3 .  HG .  LG .
     *      F2 .  .  .  .  .
     *      F1 .  .  .  .  .
     *
     * *    Leave the Lithium-compatible microchip on the fourth floor, but bring the Hydrogen-compatible one so
     *      you can still use the elevator; this is safe because although the Lithium Generator is on the
     *      destination floor, you can connect Hydrogen-compatible microchip to the Hydrogen Generator there:
     *
     *
     *      F4 .  .  .  .  LM
     *      F3 E  HG HM LG .
     *      F2 .  .  .  .  .
     *      F1 .  .  .  .  .
     *
     * *    Bring both Generators up to the fourth floor, which is safe because you can connect
     *      the Lithium-compatible Microchip to the Lithium Generator upon arrival:
     *
     *
     *      F4 E  HG .  LG LM
     *      F3 .  .  HM .  .
     *      F2 .  .  .  .  .
     *      F1 .  .  .  .  .
     *
     * *    Bring the Lithium Microchip with you to the third floor so you can use the elevator:
     *
     *      F4 .  HG .  LG .
     *      F3 E  .  HM .  LM
     *      F2 .  .  .  .  .
     *      F1 .  .  .  .  .
     *
     * *    Bring both Microchips to the fourth floor:
     *
     *      F4 E  HG HM LG LM
     *      F3 .  .  .  .  .
     *      F2 .  .  .  .  .
     *      F1 .  .  .  .  .
     *
     * In this arrangement, it takes 11 steps to collect all of the objects at the fourth floor for assembly.
     * (Each elevator stop counts as one step, even if nothing is added to or removed from it.)
     *
     * In your situation, what is the minimum number of steps required to bring all of the objects to the fourth floor?
     */
    override fun processPartOne(): Int = solve(FloorsState(input.clone()))
    // result 33 for part 1

    /**
     * You step into the cleanroom separating the lobby from the isolated area and put on the hazmat suit.
     *
     * Upon entering the isolated containment area, however, you notice some extra parts on the first floor
     * that weren't listed on the record outside:
     *
     * * An elerium generator.
     * * An elerium-compatible microchip.
     * * A dilithium generator.
     * * A dilithium-compatible microchip.
     *
     * These work just like the other generators and microchips. You'll have to get them up to assembly as well.
     *
     * What is the minimum number of steps required to bring all of the objects, including these four new ones,
     * to the fourth floor?
     */
    override fun processPartTwo(): Int =
        solve(
            FloorsState(
                input.clone().also {
                    with(it[0].powerEntities) {
                        add(Generator("generator", "elerium"))
                        add(Microchip("microchip", "elerium"))
                        add(Generator("generator", "dilithium"))
                        add(Microchip("microchip", "dilithium"))
                    }
                },
            ),
        )
    // result 57 for part 2

    private fun solve(state: FloorsState): Int {
        var solvingQueue = LinkedHashSet<FloorsState>()
        val visitedStates = mutableSetOf<FloorsState>()

        solvingQueue.add(state)
        visitedStates.add(state)

        var solvingBackQueue = LinkedHashSet<FloorsState>()
        val visitedBackStates = mutableSetOf<FloorsState>()
        val backState =
            state.clone().also {
                it.elevator = it.floors.lastIndex
                it.floors.forEachIndexed { index, floor ->
                    if (index != it.floors.size - 1) {
                        it.floors
                            .last()
                            .powerEntities
                            .addAll(floor.powerEntities)
                        floor.powerEntities.clear()
                    }
                }
            }

        solvingBackQueue.add(backState)
        visitedBackStates.add(backState)

        lateinit var solvedState: List<FloorsState>
        var iteration = 0
        while (solvingQueue.isNotEmpty() && solvingBackQueue.isNotEmpty()) {
            solvingQueue = getAllNextStates(solvingQueue, visitedStates)
            solvedState = solvingQueue.filter { solvingBackQueue.contains(it) }
            if (solvedState.isNotEmpty()) {
                return solvedState.minOf { it.steps + solvingBackQueue.first { backState -> backState == it }.steps }
            }
            solvingBackQueue = getAllNextStates(solvingBackQueue, visitedBackStates)
            solvedState = solvingQueue.filter { solvingBackQueue.contains(it) }
            if (solvedState.isNotEmpty()) {
                return solvedState.minOf { it.steps + solvingBackQueue.first { backState -> backState == it }.steps }
            }
            iteration++
            visitedStates.removeIf { it.steps <= iteration - 3 }
            visitedBackStates.removeIf { it.steps <= iteration - 3 }
        }
        return -1 // unsolvable
    }

    private fun getAllNextStates(
        queue: LinkedHashSet<FloorsState>,
        visited: MutableSet<FloorsState>,
    ): LinkedHashSet<FloorsState> {
        val nextQueue = LinkedHashSet<FloorsState>()
        while (queue.isNotEmpty()) {
            val currentState = queue.removeFirst()
            val tmpNextStates = currentState.generateNextStates()
            val filteredNextStates = tmpNextStates.filter { visited.add(it) }
            nextQueue.addAll(filteredNextStates)
        }
        return nextQueue
    }
}
