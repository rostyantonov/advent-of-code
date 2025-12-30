package aoc.common.validation

/**
 * Validation utilities for Advent of Code solutions.
 * Helps ensure results are of expected types and within valid ranges.
 */
object ResultValidator {
    /**
     * Validate that a result is an integer within expected range.
     * @param result Result to validate
     * @param min Minimum acceptable value (inclusive)
     * @param max Maximum acceptable value (inclusive)
     * @throws ValidationException if validation fails
     */
    fun validateInteger(
        result: Any?,
        min: Long? = null,
        max: Long? = null,
    ): Long {
        val value =
            when (result) {
                is Number -> result.toLong()
                is String -> result.toLongOrNull() ?: throw ValidationException("Result is not a valid number: $result")
                else -> throw ValidationException("Result is not a number: $result (${result?.javaClass?.simpleName})")
            }

        min?.let {
            if (value < it) {
                throw ValidationException("Result $value is less than minimum $it")
            }
        }

        max?.let {
            if (value > it) {
                throw ValidationException("Result $value is greater than maximum $it")
            }
        }

        return value
    }

    /**
     * Validate that a result is a string matching expected pattern.
     * @param result Result to validate
     * @param pattern Regex pattern to match
     * @throws ValidationException if validation fails
     */
    fun validateString(
        result: Any?,
        pattern: Regex? = null,
    ): String {
        val value = result?.toString() ?: throw ValidationException("Result is null")

        pattern?.let {
            if (!it.matches(value)) {
                throw ValidationException("Result '$value' does not match pattern '${it.pattern}'")
            }
        }

        return value
    }

    /**
     * Validate that a result is not null.
     * @param result Result to validate
     * @throws ValidationException if result is null
     */
    fun validateNotNull(result: Any?): Any =
        result ?: throw ValidationException("Result is null")

    /**
     * Validate that a result equals expected value.
     * @param actual Actual result
     * @param expected Expected result
     * @throws ValidationException if values don't match
     */
    fun validateEquals(
        actual: Any?,
        expected: Any?,
    ) {
        if (actual != expected) {
            throw ValidationException("Expected $expected but got $actual")
        }
    }

    /**
     * Validate that a result is within a collection of valid values.
     * @param result Result to validate
     * @param validValues Collection of valid values
     * @throws ValidationException if result is not in valid values
     */
    fun validateInSet(
        result: Any?,
        validValues: Collection<Any>,
    ): Any {
        if (result !in validValues) {
            throw ValidationException("Result $result is not in valid set: $validValues")
        }
        return result
    }

    /**
     * Validate boolean result.
     * @param result Result to validate
     * @return Boolean value
     * @throws ValidationException if not a valid boolean
     */
    fun validateBoolean(result: Any?): Boolean =
        when (result) {
            is Boolean -> result
            is String -> result.toBooleanStrictOrNull() ?: throw ValidationException("Result is not a valid boolean: $result")
            else -> throw ValidationException("Result is not a boolean: $result")
        }
}

/**
 * Exception thrown when validation fails.
 */
class ValidationException(
    message: String,
) : Exception(message)
