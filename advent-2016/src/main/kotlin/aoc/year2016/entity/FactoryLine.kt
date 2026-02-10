package aoc.year2016.entity

import aoc.ksp.FieldConverter
import aoc.ksp.GenerateStructure
import aoc.year2016.entity.FactoryInOut.BOT
import aoc.year2016.entity.FactoryInOut.OUTPUT

@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed class FactoryLine {
    data class Value(
        @FieldConverter(FactoryInOutConverter::class)
        val cmd: FactoryInOut,
        val value: Int,
        @FieldConverter(FactoryInOutConverter::class)
        val typeOut: FactoryInOut,
        val outId: Int,
    ) : FactoryLine()

    data class Bot(
        @FieldConverter(FactoryInOutConverter::class)
        val cmd: FactoryInOut,
        val botId: Int,
        @FieldConverter(FactoryInOutConverter::class)
        val lowOut: FactoryInOut,
        val lowId: Int,
        @FieldConverter(FactoryInOutConverter::class)
        val highOut: FactoryInOut,
        val highId: Int,
        val inBin: MutableList<Int> = mutableListOf(),
    ) : FactoryLine() {
        fun putIn(inValue: Int) {
            inBin.add(inValue)
        }

        fun hasTwo(chips: Pair<Int, Int>?): Boolean =
            if (chips == null) {
                false
            } else {
                (
                    inBin.size >= 2 &&
                        (inBin[0] == chips.first || inBin[1] == chips.first) &&
                        (inBin[0] == chips.second || inBin[1] == chips.second)
                )
            }

        fun giveToBots(
            bots: Map<Int, Bot>,
            outputs: MutableMap<Int, MutableList<Int>>,
        ) {
            if (inBin.size >= 2) {
                val first = inBin.removeFirst()
                val second = inBin.removeFirst()

                if (lowOut == OUTPUT && outputs[lowId] == null) {
                    outputs[lowId] = mutableListOf()
                }
                when (lowOut) {
                    BOT if first < second -> {
                        bots[lowId]?.putIn(first)
                    }

                    BOT if first > second -> {
                        bots[lowId]?.putIn(second)
                    }

                    OUTPUT if first < second -> {
                        outputs[lowId]!!.add(first)
                    }

                    OUTPUT if first > second -> {
                        outputs[lowId]!!.add(second)
                    }

                    else -> {} // do nothing
                }

                if (highOut == OUTPUT && outputs[highId] == null) {
                    outputs[highId] = mutableListOf()
                }
                when (highOut) {
                    BOT if first < second -> {
                        bots[highId]?.putIn(second)
                    }

                    BOT if first > second -> {
                        bots[highId]?.putIn(first)
                    }

                    OUTPUT if first < second -> {
                        outputs[highId]!!.add(second) // second is higher
                    }

                    OUTPUT if first > second -> {
                        outputs[highId]!!.add(first) // first is higher
                    }

                    else -> {} // do nothing
                }
            }
        }
    }
}
