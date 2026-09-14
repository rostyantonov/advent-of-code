package aoc.year2017.entity

import aoc.ksp.GenerateStructure

/**
 * One of the two dueling generators of Day 15, holding only its starting value.
 *
 * The seed is a Long because every value the generator produces is one, and the multiplication that
 * produces the next one overflows an Int.
 */
@GenerateStructure
data class DuelGenerator(
    val seed: Long,
)
