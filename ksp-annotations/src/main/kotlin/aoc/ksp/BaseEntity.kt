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

    fun getAsLong(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "Long", getAsNullableLong(collection, name))

    fun getAsNullableLong(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toLongOrNull()

    fun getAsBoolean(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "Boolean", getAsNullableBoolean(collection, name))

    /**
     * Note this is stricter than [String.toBoolean], which maps anything that is not "true" to
     * false: a group that matched something else is a regex mistake, not a false.
     */
    fun getAsNullableBoolean(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toBooleanStrictOrNull()

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
     * [raw] read as a [T] constant, for the enum entities whose whole token is the constant and so
     * have no named group to read.
     */
    inline fun <reified T : Enum<T>> asEnum(raw: String): T =
        asNullableEnum<T>(raw)
            ?: throw IllegalArgumentException(
                "'$raw' is not a ${T::class.simpleName}; expected one of " +
                    acceptedTokens(T::class.java).joinToString(", "),
            )

    /**
     * [raw] read as a [T] constant, normalised the way input text tends to differ from Kotlin
     * naming: case is ignored and spaces stand in for underscores ("turn on" -> TURN_ON).
     *
     * The single normalisation rule for both entry points, so a field enum and an entity enum cannot
     * disagree about what a token means.
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
     * so "turn on" reaches TURN_ON and "l" reaches an `@StructureName("L")`.
     *
     * Public only because [asNullableEnum] is inline; nothing outside [BaseEntity] needs to call it.
     */
    fun normaliseToken(raw: String): String = raw.trim().replace(' ', '_').uppercase()

    /**
     * The normalised token of every constant of [type], mapped to the constant itself.
     *
     * A constant is spelled by its own name unless [StructureName] gives it one - the alias replaces
     * the name rather than joining it, exactly as it does for a sealed subclass discriminator, so
     * `@StructureName("L") Left` answers to "L" and not to "LEFT". The processor rejects two
     * constants claiming one token at compile time, so the last-wins here is unreachable from
     * generated code.
     *
     * Cached per enum class: the table is derived by reflection, and the same enum is read once per
     * input line otherwise.
     *
     * Public only because [asNullableEnum] is inline; nothing outside [BaseEntity] needs to call it.
     */
    fun tokenTable(type: Class<out Enum<*>>): Map<String, Enum<*>> =
        tokenTables.computeIfAbsent(type) {
            type.enumConstants.orEmpty().associateBy { constant ->
                val alias = type.getField(constant.name).getAnnotation(StructureName::class.java)?.value
                normaliseToken(alias ?: constant.name)
            }
        }

    private val tokenTables = ConcurrentHashMap<Class<out Enum<*>>, Map<String, Enum<*>>>()
}
