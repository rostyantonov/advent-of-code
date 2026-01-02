package aoc.common.entity

import aoc.common.util.valueOrElse
import aoc.ksp.BaseEntity
import aoc.ksp.TypeConverter

object BitOperationConverter : TypeConverter<BitOperation> {
    override fun convert(
        collection: MatchGroupCollection,
        fieldName: String,
    ): BitOperation {
        val value = BaseEntity.getAsNullableString(collection, fieldName)
        return valueOrElse(value, BitOperation.DIRECT)
    }
}