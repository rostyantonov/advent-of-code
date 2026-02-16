package aoc.common.util

import aoc.common.entity.CharConstants.CHAR_HYPHEN
import kotlin.String
import kotlin.text.substringAfter
import kotlin.text.substringBefore

fun String.asLongRange(delimiter: Char = CHAR_HYPHEN) =
    substringBefore(delimiter).toLong()..substringAfter(delimiter).toLong()
