package aoc.year2016.entity

import aoc.common.entity.IDataClass
import aoc.common.util.cloneSortable
import aoc.ksp.IStructureCustomLine
import aoc.year2016.entity.PowerEntity.Generator
import aoc.year2016.entity.PowerEntity.Microchip
import java.util.SortedSet

data class Floor(
    val powerEntities: SortedSet<PowerEntity> = sortedSetOf(comparator),
) : IDataClass<Floor> {
    override fun clone(): Floor = copy(powerEntities = powerEntities.cloneSortable(comparator))

    fun isSafe(): Boolean {
        val generators = powerEntities.filterIsInstance<Generator>()
        val microchips = powerEntities.filterIsInstance<Microchip>()
        if (generators.isEmpty() || microchips.isEmpty()) return true
        return microchips.all { microchip ->
            generators.any { it.name == microchip.name }
        }
    }

    companion object : IStructureCustomLine<Floor> {
        override fun create(
            line: String,
            collection: Sequence<MatchResult>,
        ): Floor =
            Floor(
                powerEntities = collection.map { PowerEntityCompanion.create(it.groups) }.toSortedSet(comparator),
            )

        private val comparator =
            Comparator { a: PowerEntity, b: PowerEntity ->
                val classComparison = a::class.simpleName?.compareTo(b::class.simpleName ?: "") ?: 0

                if (classComparison != 0) {
                    classComparison
                } else {
                    a.name.compareTo(b.name)
                }
            }
    }
}
