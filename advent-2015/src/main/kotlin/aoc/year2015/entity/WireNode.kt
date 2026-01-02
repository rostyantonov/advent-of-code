package aoc.year2015.entity

import aoc.common.entity.BaseEntity.getAsNullableString
import aoc.common.entity.BaseEntity.getAsNullableUShort
import aoc.common.entity.BaseEntity.getAsString
import aoc.common.entity.BitOperation
import aoc.common.entity.BitOperation.AND
import aoc.common.entity.BitOperation.DIRECT
import aoc.common.entity.BitOperation.LSHIFT
import aoc.common.entity.BitOperation.NOT
import aoc.common.entity.BitOperation.OR
import aoc.common.entity.BitOperation.RSHIFT
import aoc.common.entity.IStructure
import aoc.common.util.valueOrElse

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
                name = getAsString(collection, "name"),
                left = getAsNullableString(collection, "left"),
                operation = valueOrElse<BitOperation>(getAsNullableString(collection, "cmd"), DIRECT),
                right = getAsNullableString(collection, "right"),
                value = getAsNullableUShort(collection, "left"),
            )
    }
}
