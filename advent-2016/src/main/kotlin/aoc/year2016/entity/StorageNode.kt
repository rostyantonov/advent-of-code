package aoc.year2016.entity

import aoc.ksp.GenerateStructure

/**
 * Represents a storage node in the grid computing cluster (Day 22).
 *
 * Each node has a position (x, y) and storage capacity information.
 *
 * The input file has 2 header lines that are skipped during parsing.
 */
@GenerateStructure(multiStructure = false, discriminatorField = "", skipHeaderLines = 2)
data class StorageNode(
    val x: Int,
    val y: Int,
    val size: Int,
    val used: Int,
    val avail: Int,
) {
    val isEmpty: Boolean get() = used == 0
}
