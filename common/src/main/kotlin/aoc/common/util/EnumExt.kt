package aoc.common.util

import aoc.common.entity.CharConstants.CHAR_UNDERSCORE
import aoc.common.entity.CharConstants.EMPTY_SPACE

/**
 * Resolves [type] to an enum constant, falling back to [value] when the group did not match.
 *
 * The plain case is handled by `BaseEntity.getAsEnum`, which the processor emits for any enum
 * parameter; this stays for the converters that need a default rather than a failure.
 */
inline fun <reified T : Enum<T>> valueOrElse(
    type: String?,
    value: T,
): T {
    if (type.isNullOrEmpty()) {
        return value
    }
    return java.lang.Enum.valueOf(T::class.java, type.replace(EMPTY_SPACE, CHAR_UNDERSCORE).uppercase())
}
