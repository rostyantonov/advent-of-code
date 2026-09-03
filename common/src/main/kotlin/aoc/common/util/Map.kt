package aoc.common.util

import aoc.common.entity.Position

fun <Type> MutableMap<Position, Type>.cloneData(): MutableMap<Position, Type> = this.toMutableMap()
