package aoc.ksp

import aoc.ksp.CompilationFixture.entity
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Every way the annotations can be misused, and the message the processor is expected to report.
 *
 * These cases used to be checked by temporarily breaking a real entity, running Gradle and reading
 * the console, so a regression in a diagnostic was invisible until someone hit it.
 */
class StructureDiagnosticsTest {
    @Test
    fun `two generation modes at once is rejected rather than silently picking one`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Confused",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(customLine = true, lineBased = true)
                        data class Confused(
                            val x: Int,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "customLine and lineBased are mutually exclusive on Confused")
    }

    @Test
    fun `multiStructure with a blank discriminator field is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Blank",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(multiStructure = true, discriminatorField = "")
                        sealed class Blank {
                            data class One(val x: Int) : Blank()
                        }
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "needs a non-blank discriminatorField on Blank")
    }

    @Test
    fun `a discriminator field without multiStructure warns that it does nothing`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Pointless",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(discriminatorField = "cmd")
                        data class Pointless(
                            val x: Int,
                        )
                        """.trimIndent(),
                ),
            )

        // A warning, not an error: the entity is still perfectly generatable.
        assertTrue(result.succeeded, result.messages)
        assertContains(result.messages, "discriminatorField is only used when multiStructure=true")
    }

    @Test
    fun `negative skip counts are rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Backwards",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(skipHeaderLines = -1)
                        data class Backwards(
                            val x: Int,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "skipHeaderLines/skipFooterLines cannot be negative on Backwards")
    }

    @Test
    fun `FromMatch outside customLine and lineBased is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Misplaced",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FromMatch", "aoc.ksp.MatchPart"),
                    body =
                        """
                        @GenerateStructure
                        data class Misplaced(
                            @FromMatch(MatchPart.LINE)
                            val whole: String,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "@FromMatch on 'whole' requires")
    }

    @Test
    fun `FromMatch LINE on a non-String parameter is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "WrongLine",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FromMatch", "aoc.ksp.MatchPart"),
                    body =
                        """
                        @GenerateStructure(customLine = true)
                        data class WrongLine(
                            @FromMatch(MatchPart.LINE)
                            val whole: Int,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "@FromMatch(LINE) needs a String parameter, but 'whole' is 'Int'")
    }

    @Test
    fun `FromMatch ALL_MATCHES on a non-List parameter is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "WrongAll",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FromMatch", "aoc.ksp.MatchPart"),
                    body =
                        """
                        @GenerateStructure(customLine = true)
                        data class WrongAll(
                            @FromMatch(MatchPart.ALL_MATCHES)
                            val atoms: String,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "@FromMatch(ALL_MATCHES) needs a List parameter, but 'atoms' is 'String'")
    }

    @Test
    fun `a custom line parameter with no FromMatch is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Unsourced",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(customLine = true)
                        data class Unsourced(
                            val whole: String,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "parameter 'whole' is missing @FromMatch")
    }

    @Test
    fun `FromMatch RANGE on a non-IntRange parameter is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "WrongRange",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FromMatch", "aoc.ksp.MatchPart"),
                    body =
                        """
                        @GenerateStructure(lineBased = true)
                        data class WrongRange(
                            @FromMatch(MatchPart.RANGE)
                            val span: String,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "@FromMatch(RANGE) needs an IntRange parameter, but 'span' is 'String'")
    }

    @Test
    fun `a match part that belongs to another mode is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "CrossMode",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FromMatch", "aoc.ksp.MatchPart"),
                    body =
                        """
                        @GenerateStructure(lineBased = true)
                        data class CrossMode(
                            @FromMatch(MatchPart.LINE)
                            val whole: String,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "@FromMatch(LINE) is not valid with lineBased=true")
    }

    @Test
    fun `two subclasses claiming the same token is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Clashing",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.StructureName"),
                    body =
                        """
                        @GenerateStructure(multiStructure = true)
                        sealed class Clashing {
                            @StructureName("x")
                            data class First(val a: Int) : Clashing()

                            @StructureName("X")
                            data class Second(val b: Int) : Clashing()
                        }
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, """Discriminator token "X" is already used by First""")
    }

    @Test
    fun `a blank StructureName is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Nameless",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.StructureName"),
                    body =
                        """
                        @GenerateStructure(multiStructure = true)
                        sealed class Nameless {
                            @StructureName("  ")
                            data class One(val a: Int) : Nameless()
                        }
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "@StructureName on One must not be blank")
    }

    @Test
    fun `an unsupported parameter type points at FieldConverter`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Exotic",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        data class Exotic(
                            val ratio: Double,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "Unsupported type: Double for parameter ratio")
        assertContains(result.messages, "use @FieldConverter annotation")
    }

    @Test
    fun `an entity with no primary constructor is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "SecondaryOnly",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        class SecondaryOnly {
                            val x: Int

                            constructor(x: Int) {
                                this.x = x
                            }
                        }
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "must have a primary constructor")
    }

    @Test
    fun `an entity with no parameters is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Empty",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        class Empty
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "must have at least one parameter")
    }

    @Test
    fun `a sealed hierarchy with no subclasses is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Barren",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(multiStructure = true)
                        sealed class Barren
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "requires a sealed class with subclasses")
    }
}
