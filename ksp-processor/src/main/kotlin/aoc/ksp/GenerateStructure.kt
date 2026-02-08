package aoc.ksp

/**
 * Annotation to mark data classes that should have their IStructure companion object generated.
 *
 * The KSP processor will generate a companion object implementing IStructure<T>, IStructureCustomLine<T>,
 * IStructureMulti<T>, or IStructureLine<T> that:
 * - Extracts field values from regex named groups matching the field names (when customLine=false, multiStructure=false, lineBased=false)
 * - Processes the entire line and match sequence (when customLine=true)
 * - Handles sealed classes with multiple regex patterns (when multiStructure=true)
 * - Finds all matches in a line and returns a list (when lineBased=true)
 * - Supports Int, Long, String, Char, UShort (and nullable variants)
 *
 * @param customLine If true, generates IStructureCustomLine<T> with create(line, collection: Sequence<MatchResult>)
 *                   If false (default), generates IStructure<T> with create(collection: MatchGroupCollection)
 * @param multiStructure If true, generates IStructureMulti<T> for sealed classes with create(collection: MatchGroupCollection)
 *                       Requires a discriminator field (typically 'type' or 'cmd') to route to the correct subclass
 * @param lineBased If true, generates IStructureLine<T> with create(collection: MatchResult) and fromLine returns List<T>
 *                  Useful for parsing multiple occurrences of a pattern in a single line
 * @param discriminatorField The field name used to determine which sealed subclass to instantiate (default: "type")
 *                           Only used when multiStructure=true
 *
 * Example (standard):
 * ```
 * @GenerateStructure
 * data class Present(
 *     val length: Int,
 *     val width: Int,
 *     val height: Int,
 * )
 * ```
 *
 * Example (custom line):
 * ```
 * @GenerateStructure(customLine = true)
 * data class Molecule(
 *     val stringValue: String,
 *     val atoms: List<Atom>,
 * )
 * ```
 *
 * Example (multi-structure):
 * ```
 * @GenerateStructure(multiStructure = true, discriminatorField = "cmd")
 * sealed class AsmInstruction {
 *     data class Jmp(val offset: Int) : AsmInstruction()
 *     data class Inc(val register: String) : AsmInstruction()
 * }
 * ```
 *
 * Example (line-based):
 * ```
 * @GenerateStructure(lineBased = true)
 * data class WalkerInstruction(
 *     val direction: Char,
 *     val steps: Int,
 * )
 * // Parses "R3, L5, R2" -> List(WalkerInstruction('R', 3), WalkerInstruction('L', 5), WalkerInstruction('R', 2))
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateStructure(
    val customLine: Boolean = false,
    val multiStructure: Boolean = false,
    val lineBased: Boolean = false,
    val discriminatorField: String = "type",
)
