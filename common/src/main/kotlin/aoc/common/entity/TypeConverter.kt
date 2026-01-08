package aoc.common.entity

import kotlin.text.MatchGroupCollection

/**
 * Interface for custom type converters that can be used with @GenerateStructure annotation.
 *
 * Implement this interface to support conversion of custom types from regex match groups.
 *
 * Example:
 * ```
 * object PositionConverter : TypeConverter<Position> {
 *     override fun convert(collection: MatchGroupCollection, fieldName: String): Position {
 *         val x = BaseEntity.getAsInt(collection, "${fieldName}X")
 *         val y = BaseEntity.getAsInt(collection, "${fieldName}Y")
 *         return Position(x, y)
 *     }
 * }
 * ```
 */
interface TypeConverter<T> {
    /**
     * Convert a value from the regex match group collection.
     *
     * @param collection The match group collection from regex matching
     * @param fieldName The name of the field being converted
     * @return The converted value of type T
     * @throws IllegalArgumentException if the conversion fails
     */
    fun convert(
        collection: MatchGroupCollection,
        fieldName: String,
    ): T
}
