package aoc.year2015.entity

sealed interface Player {
    var initHealth: Int
    var health: Int
    var damage: Int
    var armor: Int
    var mana: Int

    fun resetHealth() {
        health = initHealth
    }

    fun attack(opponent: Player) {
        var attack = damage - opponent.armor
        if (attack <= 0) {
            attack = 1
        }
        opponent.health -= attack
    }
}

data class BossPlayer(
    override var initHealth: Int,
    override var health: Int = initHealth,
    override var damage: Int = 0,
    override var armor: Int = 0,
) : Player {
    override var mana: Int = 0

    companion object {
        private const val HIT_POINTS = "Hit Points: "
        private const val DAMAGE = "Damage: "
        private const val ARMOR = "Armor: "

        fun getPlayerFromLines(lines: List<String>): BossPlayer {
            var initHealth = 0
            var damage = 0
            var armor = 0
            lines.forEach { line ->
                if (line.startsWith(HIT_POINTS)) {
                    initHealth = line.substring(HIT_POINTS.length).toInt()
                } else if (line.startsWith(DAMAGE)) {
                    damage = line.substring(DAMAGE.length).toInt()
                } else if (line.startsWith(ARMOR)) {
                    armor = line.substring(ARMOR.length).toInt()
                }
            }
            return BossPlayer(initHealth = initHealth, damage = damage, armor = armor)
        }
    }
}

data class WarriorPlayer(
    override var initHealth: Int = 100,
    override var health: Int = initHealth,
    override var damage: Int = 0,
    override var armor: Int = 0,
) : Player {
    override var mana: Int = 0

    var itemSet: ItemSet? = null
        set(value) {
            field = value
            damage = (itemSet?.weapon?.damage ?: 0) +
                (itemSet?.leftRing?.damage ?: 0) +
                (itemSet?.rightRing?.damage ?: 0)

            armor = (itemSet?.armor?.armor ?: 0) +
                (itemSet?.leftRing?.armor ?: 0) +
                (itemSet?.rightRing?.armor ?: 0)
        }
}
