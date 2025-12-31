package aoc.year2015.entity

import aoc.common.entity.Position
import aoc.ksp.BaseEntity
import aoc.ksp.TypeConverter
import kotlin.text.MatchGroupCollection

/**
 * TypeConverter for Position that parses "x,y" format from a single regex group.
 *
 * This converter expects a single regex group containing coordinates in "x,y" format.
 *
 * Example usage:
 * ```
 * @GenerateStructure
 * data class Instruction(
 *     @FieldConverter(PositionConverter::class)
 *     val start: Position,
 *     @FieldConverter(PositionConverter::class)
 *     val end: Position,
 * )
 *
 * // With regex: "(?<start>\\d+,\\d+) to (?<end>\\d+,\\d+)"
 * // Input: "0,0 to 999,999"
 * ```
 */
object PositionConverter : TypeConverter<Position> {
    override fun convert(
        collection: MatchGroupCollection,
        fieldName: String,
    ): Position {
        val value = BaseEntity.getAsString(collection, fieldName)
        val (row, col) = value.split(",").map { it.toInt() }
        return Position(row, col)
    }
}
