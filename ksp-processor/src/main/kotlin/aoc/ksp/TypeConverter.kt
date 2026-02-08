package aoc.ksp

import kotlin.text.MatchGroupCollection

/**
 * Interface for custom type converters used with @FieldConverter annotation.
 */
interface TypeConverter<T> {
    fun convert(collection: MatchGroupCollection,
                fieldName: String): T
}
