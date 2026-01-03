package aoc.year2015.entity

import aoc.year2015.entity.Shop.Armor
import aoc.year2015.entity.Shop.Ring
import aoc.year2015.entity.Shop.Weapons

data class ItemSet(
    val weapon: Weapons,
    val armor: Armor,
    val leftRing: Ring,
    val rightRing: Ring,
) {
    val price: Int
        get() = weapon.cost + armor.cost + leftRing.cost + rightRing.cost
}
