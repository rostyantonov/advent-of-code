package aoc.common.util

import aoc.common.entity.CharConstants.CHAR_UNDERSCORE
import aoc.common.entity.CharConstants.EMPTY_SPACE

inline fun <reified T : Enum<T>> safeValue(type: String): T =
    java.lang.Enum.valueOf(T::class.java, type.replace(EMPTY_SPACE, CHAR_UNDERSCORE).uppercase())

inline fun <reified T : Enum<T>> valueOrElse(
    type: String?,
    value: T,
): T {
    if (type.isNullOrEmpty()) {
        return value
    }
    return java.lang.Enum.valueOf(T::class.java, type.replace(EMPTY_SPACE, CHAR_UNDERSCORE).uppercase())
}
