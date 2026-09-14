package aoc.year2017.entity

import aoc.ksp.GenerateStructure

/**
 * One step across the hex grid of Day 11.
 *
 * The input is nothing but these tokens - `se,ne,se,n,...` - so the constant is the whole entity
 * rather than a field of one. The lowercase input resolves because the matching uppercases.
 */
@GenerateStructure
enum class HexDirection {
    N,
    NE,
    SE,
    S,
    SW,
    NW,
}
