package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StringInput
import aoc.year2015.entity.BossPlayer
import aoc.year2015.entity.BossPlayer.Companion.getPlayerFromLines
import aoc.year2015.entity.Spell
import aoc.year2015.entity.WizardPlayer
import java.util.LinkedList
import kotlin.properties.Delegates

class Day22 : AoCFileInput<List<String>, Int>() {
    override val inputFunction
        get() = StringInput::asIs

    private var manaSpent by Delegates.notNull<Int>()

    /**
     * Little Henry Case decides that defeating bosses with swords and stuff is boring.
     * Now he's playing the game with a wizard. Of course, he gets stuck on another boss and needs your help again.
     *
     * In this version, combat still proceeds with the player and the boss taking alternating turns.
     * The player still goes first. Now, however, you don't get any equipment;
     * instead, you must choose one of your spells to cast. The first character at or below 0 hit points loses.
     *
     * Since you're a wizard, you don't get to wear armor, and you can't attack normally. However,
     * since you do magic damage, your opponent's armor is ignored, and so the boss effectively has zero armor as well.
     * As before, if armor (from a spell, in this case) would reduce damage below 1, it becomes 1 instead - that is,
     * the boss' attacks always deal at least 1 damage.
     *
     * On each of your turns, you must select one of your spells to cast. If you cannot afford to cast any spell,
     * you lose. Spells cost mana; you start with 500 mana, but have no maximum limit.
     * You must have enough mana to cast a spell, and its cost is immediately deducted when you cast it.
     * Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.
     *
     *     Magic Missile costs 53 mana. It instantly does 4 damage.
     *     Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
     *     Shield costs 113 mana. It starts an effect that lasts for 6 turns.
     *          While it is active, your armor is increased by 7.
     *     Poison costs 173 mana. It starts an effect that lasts for 6 turns.
     *          At the start of each turn while it is active, it deals the boss 3 damage.
     *     Recharge costs 229 mana. It starts an effect that lasts for 5 turns.
     *          At the start of each turn while it is active, it gives you 101 new mana.
     *
     * Effects all work the same way. Effects apply at the start of both the player's turns and the boss' turns.
     * Effects are created with a timer (the number of turns they last); at the start of each turn,
     * after they apply any effect they have, their timer is decreased by one. If this decreases the timer to zero,
     * the effect ends. You cannot cast a spell that would start an effect which is already active.
     * However, effects can be started on the same turn they end.
     *
     * You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input.
     * What is the least amount of mana you can spend and still win the fight?
     * (Do not include mana recharge effects as "spending" negative mana.)
     */
    override fun processPartOne(): Int {
        manaSpent = Int.MAX_VALUE
        val player = WizardPlayer()
        val boss = getPlayerFromLines(input)

        iterateManaSpent(player, boss)
        return manaSpent
        // result 900 for part 1
    }

    /**
     * On the next run through the game, you increase the difficulty to hard.
     *
     * At the start of each player turn (before any other effects apply), you lose 1 hit point. If this brings you to
     * or below 0 hit points, you lose.
     *
     * With the same starting stats for you and the boss, what is the least amount of mana you can spend and still
     * win the fight?
     */
    override fun processPartTwo(): Int {
        manaSpent = Int.MAX_VALUE
        val player = WizardPlayer()
        val boss = getPlayerFromLines(input)
        player.cast(Spell.INFINITE_LIFE_DRAIN, boss)
        iterateManaSpent(player, boss)
        return manaSpent
        // result 1216 for part 2
    }

    private fun iterateManaSpent(
        player: WizardPlayer,
        boss: BossPlayer,
        spellChain: LinkedList<Spell> = LinkedList(),
    ) {
        player.applyEffects()
        boss.applyEffects()
        var manaSpent = spellChain.sumOf { it.cost }
        if (boss.health <= 0) {
            this.manaSpent = minOf(this.manaSpent, manaSpent)
            return
        } else if (player.health <= 0) {
            return
        }
        Spell.entries.forEach { spell ->
            if (player.canCast(spell, boss)) {
                val playerCopy =
                    player.copy(
                        effects = player.effects.toMutableMap(),
                    )
                val bossCopy = boss.copy(effects = boss.effects.toMutableMap())
                playerCopy.cast(spell, bossCopy)
                spellChain.add(spell)
                manaSpent = spellChain.sumOf { it.cost }

                playerCopy.applyEffects()
                bossCopy.applyEffects()
                if (bossCopy.health <= 0) {
                    this.manaSpent = minOf(this.manaSpent, manaSpent)
                } else {
                    bossCopy.attack(playerCopy)
                    playerCopy.resetShield()
                    if (playerCopy.health > 0) {
                        if (this.manaSpent > manaSpent) {
                            iterateManaSpent(playerCopy, bossCopy, spellChain)
                        }
                    }
                }
                spellChain.removeLast()
            }
        }
    }
}
