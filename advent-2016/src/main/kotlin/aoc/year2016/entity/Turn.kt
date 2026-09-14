package aoc.year2016.entity

/**
 * Which way a [WalkerInstruction] turns before walking.
 *
 * Not annotated: this is a field of an instruction rather than an entity of its own, so the
 * generated companion reads it through the `direction` group like any other field.
 */
enum class Turn {
    L,
    R,
}
