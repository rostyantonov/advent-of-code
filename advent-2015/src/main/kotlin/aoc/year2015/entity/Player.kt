package aoc.year2015.entity

sealed interface Player {
    var initHealth: Int
    var health: Int
    var damage: Int
    var armor: Int
    var mana: Int
    var effects: MutableMap<Spell, Int>

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

    fun applyEffects() {
        effects.keys.forEach { spellEffect ->
            when (spellEffect) {
                Spell.POISON -> {
                    health -= 3
                }

                Spell.SHIELD -> {
                    armor = 7
                }

                Spell.RECHARGE -> {
                    mana += 101
                }

                Spell.INFINITE_LIFE_DRAIN -> {
                    health--
                }

                else -> {}
            }
            effects[spellEffect] = effects[spellEffect]!! - 1
        }
        effects =
            effects.filter { (_, length) ->
                length > 0
            } as MutableMap<Spell, Int>
    }

    fun resetShield() {
        if (!effects.containsKey(Spell.SHIELD)) {
            armor = 0
        }
    }
}

data class BossPlayer(
    override var initHealth: Int,
    override var health: Int = initHealth,
    override var damage: Int = 0,
    override var armor: Int = 0,
    override var effects: MutableMap<Spell, Int> = mutableMapOf(),
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
    override var effects: MutableMap<Spell, Int> = mutableMapOf()

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

data class WizardPlayer(
    override var initHealth: Int = 50,
    override var health: Int = initHealth,
    override var armor: Int = 0,
    override var mana: Int = 500,
    override var effects: MutableMap<Spell, Int> = mutableMapOf(),
) : Player {
    override var damage: Int = 0

    fun canCast(
        spell: Spell,
        otherPlayer: Player,
    ): Boolean {
        if (spell.cost > mana) {
            return false
        }
        return when (spell) {
            Spell.DRAIN, Spell.MAGIC_MISSILE -> true
            Spell.POISON -> !otherPlayer.effects.containsKey(spell)
            Spell.SHIELD, Spell.RECHARGE -> !effects.containsKey(spell)
            Spell.INFINITE_LIFE_DRAIN -> false // can't cast it
        }
    }

    fun cast(
        spell: Spell,
        otherPlayer: Player,
    ) {
        mana -= spell.cost
        when (spell) {
            Spell.DRAIN -> {
                otherPlayer.health -= 2
                health += 2
            }

            Spell.MAGIC_MISSILE -> {
                otherPlayer.health -= 4
            }

            Spell.POISON -> {
                otherPlayer.effects[spell] = 6
            }

            Spell.SHIELD -> {
                effects[spell] = 6
            }

            Spell.RECHARGE -> {
                effects[spell] = 5
            }

            Spell.INFINITE_LIFE_DRAIN -> {
                effects[spell] = Int.MAX_VALUE
            }
        }
    }
}
