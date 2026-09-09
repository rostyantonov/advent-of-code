package aoc.year2017.entity

import aoc.ksp.GenerateStructure
import aoc.ksp.StructureName

@GenerateStructure(multiStructure = true, discriminatorField = "type")
sealed class DanceMove {
    abstract fun execute(chars: CharArray): CharArray

    @StructureName("x")
    data class Exchange(
        val from: Int,
        val to: Int,
    ) : DanceMove() {
        override fun execute(chars: CharArray): CharArray {
            val tmp = chars[from]
            chars[from] = chars[to]
            chars[to] = tmp
            return chars
        }
    }

    @StructureName("p")
    data class Partner(
        val from: Char,
        val to: Char,
    ) : DanceMove() {
        override fun execute(chars: CharArray): CharArray {
            val tmp = chars[chars.indexOf(from)]
            val fromIndex = chars.indexOf(from)
            val toIndex = chars.indexOf(to)
            chars[fromIndex] = chars[toIndex]
            chars[toIndex] = tmp
            return chars
        }
    }

    @StructureName("s")
    data class Spin(
        val steps: Int,
    ) : DanceMove() {
        override fun execute(chars: CharArray): CharArray {
            val n = chars.size
            val effectiveSteps = steps % n
            if (effectiveSteps > 0) {
                val tmp = chars.takeLast(effectiveSteps).toCharArray()
                System.arraycopy(chars, 0, chars, effectiveSteps, n - effectiveSteps)
                System.arraycopy(tmp, 0, chars, 0, effectiveSteps)
            }
            return chars
        }
    }
}
