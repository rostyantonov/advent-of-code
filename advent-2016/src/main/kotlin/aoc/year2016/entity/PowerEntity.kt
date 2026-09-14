package aoc.year2016.entity

import aoc.common.entity.IDataClass
import aoc.ksp.GenerateStructure

@GenerateStructure(multiStructure = true, discriminatorField = "type")
sealed interface PowerEntity : IDataClass<PowerEntity> {
    val name: String

    data class Generator(
        override val name: String,
    ) : PowerEntity {
        override fun clone(): Generator = copy()
    }

    data class Microchip(
        override val name: String,
    ) : PowerEntity {
        override fun clone(): Microchip = copy()
    }
}
