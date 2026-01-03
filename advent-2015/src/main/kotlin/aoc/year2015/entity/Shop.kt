package aoc.year2015.entity

object Shop {
    enum class Weapons(
        override val cost: Int,
        override val damage: Int,
        override val armor: Int = 0,
    ) : Item {
        DAGGER(cost = 8, damage = 4),
        SHORTSWORD(cost = 10, damage = 5),
        WARHAMMER(cost = 25, damage = 6),
        LONGSWORD(cost = 40, damage = 7),
        GREATAXE(cost = 74, damage = 8),
    }

    enum class Armor(
        override val cost: Int,
        override val damage: Int = 0,
        override val armor: Int,
    ) : Item {
        NOTHING(cost = 0, armor = 0),
        LEATHER(cost = 13, armor = 1),
        CHAINMAIL(cost = 31, armor = 2),
        SPLINTMAIL(cost = 53, armor = 3),
        BANDEDMAIN(cost = 75, armor = 4),
        PLATEMAIL(cost = 102, armor = 5),
    }

    enum class Ring(
        override val cost: Int = 0,
        override val damage: Int = 0,
        override val armor: Int = 0,
    ) : Item {
        NOTHING_1,
        NOTHING_2,
        DAMAGE_1(cost = 25, damage = 1),
        DAMAGE_2(cost = 50, damage = 2),
        DAMAGE_3(cost = 100, damage = 3),
        DEFENCE_1(cost = 20, armor = 1),
        DEFENCE_2(cost = 40, armor = 2),
        DEFENCE_3(cost = 80, armor = 3),
    }
}
