package aoc.year2016.entity

import aoc.ksp.BaseEntity
import aoc.ksp.TypeConverter

object FactoryInOutConverter : TypeConverter<FactoryInOut> {
    override fun convert(
        collection: MatchGroupCollection,
        fieldName: String,
    ): FactoryInOut {
        val value = BaseEntity.getAsString(collection, fieldName)
        return when (value.uppercase()) {
            "BOT" -> FactoryInOut.BOT
            "OUTPUT" -> FactoryInOut.OUTPUT
            "VALUE" -> FactoryInOut.VALUE
            else -> throw IllegalArgumentException("Unknown FactoryInOut: $value")
        }
    }
}
