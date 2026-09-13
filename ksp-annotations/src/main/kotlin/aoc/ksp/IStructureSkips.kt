package aoc.ksp

/**
 * Input-window trimming declared on the entity through
 * [GenerateStructure.skipHeaderLines] / [GenerateStructure.skipFooterLines].
 *
 * The KSP processor overrides these on the generated companion whenever the annotation asks for a
 * non-zero value, so the counts travel with the entity instead of being repeated at every call site.
 * `StructuredInput.of` and `StructuredMultiInput.of` read them from here.
 */
interface IStructureSkips {
    val skipHeaderLines: Int get() = 0

    val skipFooterLines: Int get() = 0
}
