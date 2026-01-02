package aoc.year2015.entity

import aoc.common.entity.BaseEntity
import aoc.common.entity.BitOperation
import aoc.common.entity.TypeConverter
import aoc.common.util.valueOrElse

object BitOperationConverter : TypeConverter<BitOperation> {
    override fun convert(
        collection: MatchGroupCollection,
        fieldName: String,
    ): BitOperation {
        val value = BaseEntity.getAsNullableString(collection, fieldName)
        return valueOrElse(value, BitOperation.DIRECT)
    }
}
