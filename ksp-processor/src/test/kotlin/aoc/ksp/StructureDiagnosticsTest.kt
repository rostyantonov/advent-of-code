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
                        @GenerateStructure(customLine = true, multiStructure = true)
                        sealed class Confused
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "customLine and multiStructure are mutually exclusive on Confused")
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
    fun `FromMatch outside customLine is rejected`() {
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
    fun `an interface target is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Contract",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        interface Contract {
                            val x: Int
                        }
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "@GenerateStructure must be on a class, but Contract is a")
    }

    @Test
    fun `multiStructure on a class that is not sealed is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "NotSealed",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(multiStructure = true)
                        data class NotSealed(
                            val x: Int,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "must be on a sealed class or interface, but NotSealed is neither")
    }

    @Test
    fun `an abstract target is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Partial",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        abstract class Partial(
                            val x: Int,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "cannot generate for the abstract class Partial")
    }

    @Test
    fun `a nested target is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Outer",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        class Outer {
                            @GenerateStructure
                            data class Inner(
                                val x: Int,
                            )
                        }
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "must be on a top-level class, but Inner is nested")
    }

    @Test
    fun `a converter that produces the wrong type is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Point",
                    imports = listOf("aoc.ksp.BaseEntity", "aoc.ksp.TypeConverter"),
                    body =
                        """
                        data class Point(val x: Int)

                        data class Span(val from: Int)

                        object PointConverter : TypeConverter<Point> {
                            override fun convert(
                                collection: MatchGroupCollection,
                                fieldName: String,
                            ): Point = Point(BaseEntity.getAsInt(collection, fieldName))
                        }
                        """.trimIndent(),
                ),
                entity(
                    name = "Mismatched",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FieldConverter"),
                    body =
                        """
                        @GenerateStructure
                        data class Mismatched(
                            @FieldConverter(PointConverter::class)
                            val start: Span,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "@FieldConverter(PointConverter) produces Point, but 'start' is Span")
    }

    @Test
    fun `an ALL_MATCHES element with no single-String constructor is rejected`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Atom",
                    body = "data class Atom(val weight: Int)",
                ),
                entity(
                    name = "Molecule",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FromMatch", "aoc.ksp.MatchPart"),
                    body =
                        """
                        @GenerateStructure(customLine = true)
                        data class Molecule(
                            @FromMatch(MatchPart.ALL_MATCHES)
                            val atoms: List<Atom>,
                        )
                        """.trimIndent(),
                ),
            )

        assertFalse(result.succeeded)
        assertContains(result.messages, "Atom needs a primary constructor taking a single String")
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
