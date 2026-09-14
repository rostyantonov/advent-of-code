package aoc.year2016.entity

import aoc.ksp.StructureName

/**
 * Which way a [WalkerInstruction] turns before walking.
 *
 * Not annotated with `@GenerateStructure`: this is a field of an instruction rather than an entity
 * of its own, so the generated companion reads it through the `direction` group like any other
 * field. The input spells a turn `L` or `R`, which `@StructureName` maps onto readable constants.
 */
enum class Turn {
    @StructureName("L")
    Left,

    @StructureName("R")
    Right,
}
