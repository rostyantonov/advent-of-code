package aoc.year2015.entity

import aoc.common.entity.BitOperation
import aoc.common.entity.BitOperation.AND
import aoc.common.entity.BitOperation.DIRECT
import aoc.common.entity.BitOperation.LSHIFT
import aoc.common.entity.BitOperation.NOT
import aoc.common.entity.BitOperation.OR
import aoc.common.entity.BitOperation.RSHIFT
import aoc.common.entity.BaseEntity
import aoc.common.entity.IStructure
import aoc.common.util.valueOrElse

/**
 * WireNode entity representing a wire connection with bitwise operations.
 * 
 * Note: This entity uses manual IStructure implementation instead of KSP because:
 * - The `value` field needs to be derived from the `left` regex group when it contains a numeric value
 * - The same `left` group can be either a wire name (String) or a numeric value (UShort)
 * - This dual interpretation of a single regex group is not easily handled by KSP's automatic generation
 *
 * Example inputs:
 * - "123 -> x" (DIRECT assignment): left=123, operation=DIRECT, value=123
 * - "x AND y -> z" (AND operation): left=x, operation=AND, right=y, value=null
 */
data class WireNode(
    val name: String,
    val left: String? = null,
    val operation: BitOperation = DIRECT,
    val right: String? = null,
    val value: UShort? = null,
) {
    fun getValue(connectionsMap: MutableMap<String, WireNode>): UShort {
        if (operation == DIRECT && value != null) {
            return value
        }
        val value =
            when (operation) {
                NOT -> {
                    (right!!.toUShortOrNull() ?: connectionsMap[right]!!.getValue(connectionsMap)).inv()
                }

                AND -> {
                    (left!!.toUShortOrNull() ?: connectionsMap[left]!!.getValue(connectionsMap))
                        .and(right!!.toUShortOrNull() ?: connectionsMap[right]!!.getValue(connectionsMap))
                }

                OR -> {
                    (left!!.toUShortOrNull() ?: connectionsMap[left]!!.getValue(connectionsMap))
                        .or(right!!.toUShortOrNull() ?: connectionsMap[right]!!.getValue(connectionsMap))
                }

                LSHIFT -> {
                    (left!!.toUShortOrNull() ?: connectionsMap[left]!!.getValue(connectionsMap))
                        .toUInt()
                        .shl((right!!.toUShortOrNull() ?: connectionsMap[right]!!.getValue(connectionsMap)).toInt())
                        .toUShort()
                }

                RSHIFT -> {
                    (left!!.toUShortOrNull() ?: connectionsMap[left]!!.getValue(connectionsMap))
                        .toUInt()
                        .shr((right!!.toUShortOrNull() ?: connectionsMap[right]!!.getValue(connectionsMap)).toInt())
                        .toUShort()
                }

                DIRECT -> {
                    connectionsMap[left]!!.getValue(connectionsMap)
                }
            }
        connectionsMap[name] = WireNode(name, value = value)
        return value
    }

    companion object : IStructure<WireNode> {
        override fun create(collection: MatchGroupCollection): WireNode =
            WireNode(
                name = BaseEntity.getAsString(collection, "name"),
                left = BaseEntity.getAsNullableString(collection, "left"),
                operation = valueOrElse<BitOperation>(BaseEntity.getAsNullableString(collection, "cmd"), DIRECT),
                right = BaseEntity.getAsNullableString(collection, "right"),
                value = BaseEntity.getAsNullableUShort(collection, "left"),
            )
    }
}
