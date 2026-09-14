package aoc.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration

/** The first annotation on this declaration whose short name is [name], or null. */
internal fun KSAnnotated.annotationNamed(name: String): KSAnnotation? =
    annotations.firstOrNull { it.shortName.asString() == name }

/**
 * The value of the [name] argument, or null when it is absent or not a [T].
 *
 * KSP surfaces annotation arguments as a flat list of name/value pairs, so every lookup would
 * otherwise repeat the same `arguments.find { it.name?.asString() == … }.value as? T` dance.
 */
internal inline fun <reified T> KSAnnotation.argument(name: String): T? =
    arguments.find { it.name?.asString() == name }?.value as? T

/**
 * The `@GenerateStructure` arguments, read once and validated once.
 *
 * [from] returns null when the annotation is self-contradictory, having already reported why; the
 * caller should stop rather than generate something arbitrary.
 */
internal data class StructureOptions(
    val customLine: Boolean,
    val multiStructure: Boolean,
    val lineBased: Boolean,
    val isEnum: Boolean,
    val discriminatorField: String,
    val skipHeaderLines: Int,
    val skipFooterLines: Int,
) {
    /** The runtime interface the generated companion implements, used for the log line. */
    val companionInterface: String
        get() =
            when {
                isEnum -> "IStructureEnum"
                multiStructure -> "IStructureMulti"
                customLine -> "IStructureCustomLine"
                lineBased -> "IStructureLine"
                else -> "IStructure"
            }

    companion object {
        private const val DEFAULT_DISCRIMINATOR = "type"

        fun from(
            classDeclaration: KSClassDeclaration,
            logger: KSPLogger,
        ): StructureOptions? {
            val annotation = classDeclaration.annotationNamed("GenerateStructure")
            val className = classDeclaration.simpleName.asString()

            val options =
                StructureOptions(
                    customLine = annotation?.argument<Boolean>("customLine") ?: false,
                    multiStructure = annotation?.argument<Boolean>("multiStructure") ?: false,
                    lineBased = annotation?.argument<Boolean>("lineBased") ?: false,
                    // Read off the class kind rather than from an annotation argument: an enum
                    // cannot be mistaken for anything else, and a flag could contradict it.
                    isEnum = classDeclaration.classKind == ClassKind.ENUM_CLASS,
                    discriminatorField = annotation?.argument<String>("discriminatorField") ?: DEFAULT_DISCRIMINATOR,
                    skipHeaderLines = annotation?.argument<Int>("skipHeaderLines") ?: 0,
                    skipFooterLines = annotation?.argument<Int>("skipFooterLines") ?: 0,
                )

            // The generation modes are mutually exclusive, but the dispatch is a `when`, so without
            // this check a class asking for two of them would silently get whichever branch comes
            // first.
            val requestedModes =
                listOf(
                    "customLine" to options.customLine,
                    "multiStructure" to options.multiStructure,
                    "lineBased" to options.lineBased,
                ).filter { (_, enabled) -> enabled }
                    .map { (name, _) -> name }
            if (requestedModes.size > 1) {
                logger.error(
                    "@GenerateStructure: ${requestedModes.joinToString(" and ")} are mutually exclusive on $className",
                    classDeclaration,
                )
                return null
            }

            // An enum entity has no constructor parameters to read, so every other mode's template
            // would generate a call to a constructor that does not exist.
            if (options.isEnum && requestedModes.isNotEmpty()) {
                logger.error(
                    "@GenerateStructure: ${requestedModes.single()} has no meaning on the enum $className; " +
                        "an enum is parsed from its whole token",
                    classDeclaration,
                )
                return null
            }

            if (options.multiStructure && options.discriminatorField.isBlank()) {
                logger.error(
                    "@GenerateStructure(multiStructure=true) needs a non-blank discriminatorField on $className",
                    classDeclaration,
                )
                return null
            }
            if (!options.multiStructure && options.discriminatorField != DEFAULT_DISCRIMINATOR) {
                logger.warn(
                    "@GenerateStructure: discriminatorField is only used when multiStructure=true, " +
                        "so it has no effect on $className",
                    classDeclaration,
                )
            }

            if (options.skipHeaderLines < 0 || options.skipFooterLines < 0) {
                logger.error(
                    "@GenerateStructure: skipHeaderLines/skipFooterLines cannot be negative on $className",
                    classDeclaration,
                )
                return null
            }

            return options
        }
    }
}
