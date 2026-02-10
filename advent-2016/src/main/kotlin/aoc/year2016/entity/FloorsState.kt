package aoc.year2016.entity

import aoc.common.entity.IDataClass
import aoc.common.util.clone

data class FloorsState(
    val floors: List<Floor>,
    var elevator: Int = 0,
) : IDataClass<FloorsState> {
    var steps: Int = 0

    override fun clone(): FloorsState =
        copy(floors = floors.clone()).also { state ->
            state.steps = steps
        }

    fun generateNextStates(): List<FloorsState> {
        val nextStates = mutableListOf<FloorsState>()
        val floorsIndices =
            when (elevator) {
                0 -> listOf(1)
                floors.size - 1 -> listOf(floors.size - 2)
                else -> listOf(elevator - 1, elevator + 1)
            }

        val floorEntities = floors[elevator].powerEntities
        floorEntities.forEachIndexed { firstIndex, firstEntity ->
            var newState = clone()
            newState.floors[elevator].powerEntities.remove(firstEntity)

            // Move one entity
            if (newState.floors[elevator].isSafe()) {
                floorsIndices.forEach { newElevator ->
                    val floorState = newState.clone()

                    floorState.floors[newElevator].powerEntities.add(firstEntity)
                    if (floorState.floors[newElevator].isSafe()) {
                        floorState.elevator = newElevator
                        floorState.steps = steps + 1
                        nextStates.add(floorState)
                    }
                }
            }

            // Move two entities
            floorEntities.forEachIndexed { secondIndex, secondEntity ->
                if (secondIndex <= firstIndex) return@forEachIndexed
                if (firstEntity.javaClass != secondEntity.javaClass &&
                    firstEntity.name != secondEntity.name
                ) {
                    return@forEachIndexed
                }

                floorsIndices.forEach { newElevator ->
                    newState = clone()
                    newState.floors[elevator].powerEntities.remove(firstEntity)
                    newState.floors[elevator].powerEntities.remove(secondEntity)

                    if (newState.floors[elevator].isSafe()) {
                        newState.floors[newElevator].powerEntities.add(firstEntity)
                        newState.floors[newElevator].powerEntities.add(secondEntity)
                        if (newState.floors[newElevator].isSafe()) {
                            newState.elevator = newElevator
                            newState.steps = steps + 1
                            nextStates.add(newState)
                        }
                    }
                }
            }
        }
        return nextStates
    }

    // AI section
    // AI suggested implementations for equals and hashCode to ignore specific element names
    // Increased speed by avoiding generating all permutations of element names decreasing memory usage
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FloorsState) return false
        if (elevator != other.elevator) return false

        // Use the canonical representation for comparison
        return getCanonicalRepresentation() == other.getCanonicalRepresentation()
    }

    override fun hashCode(): Int {
        var result = elevator
        result = 31 * result + getCanonicalRepresentation().hashCode()
        return result
    }

    /**
     * Creates a representation where names are stripped.
     * It maps pairs of (GeneratorFloor, MicrochipFloor) and sorts them.
     */
    private fun getCanonicalRepresentation(): List<Pair<Int, Int>> {
        val pairs = mutableMapOf<String, Pair<Int, Int>>()

        floors.forEachIndexed { floorIdx, floor ->
            floor.powerEntities.forEach { entity ->
                val current = pairs.getOrDefault(entity.name, Pair(-1, -1))
                pairs[entity.name] =
                    if (entity is PowerEntity.Generator) {
                        current.copy(first = floorIdx)
                    } else {
                        current.copy(second = floorIdx)
                    }
            }
        }
        // Sort the pairs so that the specific element names no longer matter
        return pairs.values.sortedBy { it.first * 10 + it.second }
    }

    // End of AI section
}
