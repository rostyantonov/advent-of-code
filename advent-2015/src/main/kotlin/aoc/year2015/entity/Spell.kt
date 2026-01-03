package aoc.year2015.entity

enum class Spell(
    val cost: Int,
) {
    MAGIC_MISSILE(53),
    DRAIN(73),
    SHIELD(113),
    POISON(173),
    RECHARGE(229),
    INFINITE_LIFE_DRAIN(0),
}
