package aoc.common.cache

/**
 * Simple in-memory cache for parsed inputs to avoid re-parsing in tests.
 * Useful when running multiple test cases with the same input file.
 */
object InputCache {
    private val rawInputCache = mutableMapOf<String, List<String>>()
    private val parsedInputCache = mutableMapOf<String, Any>()
    private var enabled = true

    /**
     * Enable caching (enabled by default).
     */
    fun enable() {
        enabled = true
    }

    /**
     * Disable caching. Useful for debugging or when memory is a concern.
     */
    fun disable() {
        enabled = false
    }

    /**
     * Clear all cached data.
     */
    fun clear() {
        rawInputCache.clear()
        parsedInputCache.clear()
    }

    /**
     * Get or compute raw input lines.
     * @param key Cache key (typically file path or day identifier)
     * @param loader Function to load input if not cached
     * @return Cached or freshly loaded input lines
     */
    fun getRawInput(
        key: String,
        loader: () -> List<String>,
    ): List<String> {
        if (!enabled) return loader()
        return rawInputCache.getOrPut(key) { loader() }
    }

    /**
     * Get or compute parsed input.
     * @param key Cache key (typically file path or day identifier + parser type)
     * @param parser Function to parse input if not cached
     * @return Cached or freshly parsed input
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getParsedInput(
        key: String,
        parser: () -> T,
    ): T {
        if (!enabled) return parser()
        return parsedInputCache.getOrPut(key) { parser() } as T
    }

    /**
     * Invalidate a specific cache entry.
     */
    fun invalidate(key: String) {
        rawInputCache.remove(key)
        parsedInputCache.remove(key)
    }

    /**
     * Get cache statistics.
     */
    fun getStats(): CacheStats =
        CacheStats(
            rawInputEntries = rawInputCache.size,
            parsedInputEntries = parsedInputCache.size,
            enabled = enabled,
        )

    /**
     * Print cache statistics.
     */
    fun printStats() {
        val stats = getStats()
        println(
            "Input Cache: ${if (stats.enabled) "ENABLED" else "DISABLED"} | " +
                "Raw: ${stats.rawInputEntries} entries | " +
                "Parsed: ${stats.parsedInputEntries} entries",
        )
    }
}

/**
 * Cache statistics.
 */
data class CacheStats(
    val rawInputEntries: Int,
    val parsedInputEntries: Int,
    val enabled: Boolean,
)
