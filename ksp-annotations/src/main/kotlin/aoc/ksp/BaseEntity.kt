package aoc.ksp

import java.util.concurrent.ConcurrentHashMap

/**
 * Regex-group accessors used by the code the KSP StructureProcessor generates.
 *
 * Every supported type has a nullable getter (returns null when the group is absent or unparseable)
 * and a non-nullable getter that wraps it and fails loudly. The processor picks between them based
 * on whether the constructor parameter is marked nullable.
 */
object BaseEntity {
    private fun <T> required(
        name: String,
        type: String,
        value: T?,
    ): T =
        value ?: throw IllegalArgumentException(
            "Field '$name' (expected $type) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsString(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "String", getAsNullableString(collection, name))

    fun getAsNullableString(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value

    fun getAsChar(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "Char", getAsNullableChar(collection, name))

    fun getAsNullableChar(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.firstOrNull()

    fun getAsInt(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "Int", getAsNullableInt(collection, name))

    fun getAsNullableInt(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toIntOrNull()

    inline fun <reified T : Enum<T>> getAsEnum(
        collection: MatchGroupCollection,
        name: String,
    ): T =
        getAsNullableEnum<T>(collection, name)
            ?: throw IllegalArgumentException(
                "Field '$name' (expected ${T::class.simpleName}) not found or invalid in regex groups. " +
                    "Ensure regex has named group (?<$name>...) and that it matches one of " +
                    acceptedTokens(T::class.java).joinToString(", "),
            )

    /** The group value read as a [T] constant; see [asNullableEnum] for how the text is matched. */
    inline fun <reified T : Enum<T>> getAsNullableEnum(
        collection: MatchGroupCollection,
        name: String,
    ): T? = asNullableEnum<T>(collection[name]?.value ?: return null)

    /**
     * [raw] read as a [T] constant, normalised the way input text tends to differ from Kotlin
     * naming: case is ignored and spaces stand in for underscores ("turn on" -> TURN_ON).
     */
    inline fun <reified T : Enum<T>> asNullableEnum(raw: String): T? {
        @Suppress("UNCHECKED_CAST")
        return tokenTable(T::class.java)[normaliseToken(raw)] as T?
    }

    /**
     * The tokens an enum answers to, in declaration order, for the messages thrown when none of them
     * matched.
     */
    fun acceptedTokens(type: Class<out Enum<*>>): Collection<String> = tokenTable(type).keys

    /**
     * How input text is folded onto a token: case is ignored and spaces stand in for underscores,
     * so "turn on" reaches TURN_ON.
     *
     * Public only because [asNullableEnum] is inline; nothing outside [BaseEntity] needs to call it.
     */
    fun normaliseToken(raw: String): String = raw.trim().replace(' ', '_').uppercase()

    /**
     * The normalised token of every constant of [type], mapped to the constant itself.
     *
     * Cached per enum class: the table is derived by reflection, and the same enum is read once per
     * input line otherwise.
     *
     * Public only because [asNullableEnum] is inline; nothing outside [BaseEntity] needs to call it.
     */
    fun tokenTable(type: Class<out Enum<*>>): Map<String, Enum<*>> =
        tokenTables.computeIfAbsent(type) {
            type.enumConstants.orEmpty().associateBy { constant -> normaliseToken(constant.name) }
        }

    private val tokenTables = ConcurrentHashMap<Class<out Enum<*>>, Map<String, Enum<*>>>()
}
