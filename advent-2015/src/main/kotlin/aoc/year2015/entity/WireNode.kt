package aoc.year2015.entity

import aoc.common.entity.BitOperation
import aoc.common.entity.BitOperation.AND
import aoc.common.entity.BitOperation.DIRECT
import aoc.common.entity.BitOperation.LSHIFT
import aoc.common.entity.BitOperation.NOT
import aoc.common.entity.BitOperation.OR
import aoc.common.entity.BitOperation.RSHIFT
import aoc.common.entity.BitOperationConverter
import aoc.ksp.FieldConverter
import aoc.ksp.GenerateStructure

/**
 * WireNode entity representing a wire connection with bitwise operations.
 *
 * Uses KSP with custom BitOperationConverter to automatically generate the companion object.
 * Note: The `value` field is derived in init block from `left` when it contains a numeric value.
 *
 * Example inputs:
 * - "123 -> x" (DIRECT assignment): left=123, cmd=null -> value=123
 * - "x AND y -> z" (AND operation): left=x, cmd=AND, right=y -> value=null
 */
@GenerateStructure
data class WireNode(
    val name: String,
    val left: String? = null,
    @param:FieldConverter(BitOperationConverter::class)
    val cmd: BitOperation? = null,
    val right: String? = null,
) {
    val operation: BitOperation = cmd ?: DIRECT
    val value: UShort? = left?.toUShortOrNull()

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
        connectionsMap[name] = WireNode(name = name, left = value.toString())
        return value
    }
}
