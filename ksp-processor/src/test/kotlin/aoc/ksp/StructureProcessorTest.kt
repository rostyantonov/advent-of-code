package aoc.ksp

import aoc.ksp.CompilationFixture.entity
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * What the processor generates, for each mode it supports.
 *
 * The fixture compiles the generated source alongside the entity, so every case here also asserts
 * that what came out actually typechecks - the failure mode these tests exist to catch.
 */
class StructureProcessorTest {
    @Test
    fun `standard mode reads every parameter from its named group`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Present",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        data class Present(
                            val length: Int,
                            val name: String,
                            val tag: Char,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Present"))
        assertContains(companion, "object PresentCompanion : IStructure<Present>")
        assertContains(companion, """length = BaseEntity.getAsInt(collection, "length")""")
        assertContains(companion, """name = BaseEntity.getAsString(collection, "name")""")
        assertContains(companion, """tag = BaseEntity.getAsChar(collection, "tag")""")
    }

    @Test
    fun `nullable parameters use the nullable getters`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Tower",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        data class Tower(
                            val name: String,
                            val items: String?,
                            val weight: Int?,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Tower"))
        assertContains(companion, """items = BaseEntity.getAsNullableString(collection, "items")""")
        assertContains(companion, """weight = BaseEntity.getAsNullableInt(collection, "weight")""")
        assertContains(companion, """name = BaseEntity.getAsString(collection, "name")""")
    }

    @Test
    fun `Long and Boolean read through their own getters`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Reading",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        data class Reading(
                            val total: Long,
                            val enabled: Boolean,
                            val cap: Long?,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Reading"))
        assertContains(companion, """total = BaseEntity.getAsLong(collection, "total")""")
        assertContains(companion, """enabled = BaseEntity.getAsBoolean(collection, "enabled")""")
        assertContains(companion, """cap = BaseEntity.getAsNullableLong(collection, "cap")""")
    }

    @Test
    fun `an enum parameter needs no converter`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Switch",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        enum class Power { TURN_ON, TURN_OFF }

                        @GenerateStructure
                        data class Switch(
                            val action: Power,
                            val fallback: Power?,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Switch"))
        assertContains(companion, """action = BaseEntity.getAsEnum<Power>(collection, "action")""")
        assertContains(companion, """fallback = BaseEntity.getAsNullableEnum<Power>(collection, "fallback")""")
        // Same package as the entity, so the type resolves without an import.
        assertFalse(companion.contains("import test.Power"))
    }

    @Test
    fun `an enum from another package is imported`() {
        val result =
            CompilationFixture.process(
                CompilationFixture.source(
                    fileName = "Power.kt",
                    contents =
                        """
                        package other

                        enum class Power { ON, OFF }
                        """.trimIndent(),
                ),
                entity(
                    name = "Switch",
                    imports = listOf("aoc.ksp.GenerateStructure", "other.Power"),
                    body =
                        """
                        @GenerateStructure
                        data class Switch(
                            val action: Power,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Switch"))
        assertContains(companion, "import other.Power")
    }

    @Test
    fun `an explicit converter wins over native enum support`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Bit",
                    imports = listOf("aoc.ksp.BaseEntity", "aoc.ksp.TypeConverter"),
                    body =
                        """
                        enum class Bit { SET, DIRECT }

                        object BitConverter : TypeConverter<Bit> {
                            override fun convert(
                                collection: MatchGroupCollection,
                                fieldName: String,
                            ): Bit = BaseEntity.getAsNullableEnum<Bit>(collection, fieldName) ?: Bit.DIRECT
                        }
                        """.trimIndent(),
                ),
                entity(
                    name = "Gate",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FieldConverter"),
                    body =
                        """
                        @GenerateStructure
                        data class Gate(
                            @FieldConverter(BitConverter::class)
                            val op: Bit,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Gate"))
        // A converter is the author overriding the default name matching, so it must not be
        // silently replaced by getAsEnum - that would drop BitConverter's fallback.
        assertContains(companion, """op = BitConverter.convert(collection, "op")""")
        assertFalse(companion.contains("getAsEnum"))
    }

    @Test
    fun `declared skips become overrides on the companion`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "StorageNode",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(skipHeaderLines = 2, skipFooterLines = 1)
                        data class StorageNode(
                            val x: Int,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("StorageNode"))
        assertContains(companion, "override val skipHeaderLines: Int = 2")
        assertContains(companion, "override val skipFooterLines: Int = 1")
    }

    @Test
    fun `a zero skip generates no override at all`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Plain",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        data class Plain(
                            val x: Int,
                        )
                        """.trimIndent(),
                ),
            )

        val companion = assertNotNull(result.companionFor("Plain"))
        assertFalse(companion.contains("skipHeaderLines"), "zero skips should fall through to the interface default")
        assertFalse(companion.contains("skipFooterLines"), "zero skips should fall through to the interface default")
    }

    @Test
    fun `line-based mode reads groups off the match and maps RANGE to the match span`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Decompress",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FromMatch", "aoc.ksp.MatchPart"),
                    body =
                        """
                        @GenerateStructure(lineBased = true)
                        data class Decompress(
                            val num: Int,
                            val times: Int,
                            @FromMatch(MatchPart.RANGE)
                            val range: IntRange,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Decompress"))
        assertContains(companion, "object DecompressCompanion : IStructureLine<Decompress>")
        assertContains(companion, """num = BaseEntity.getAsInt(collection.groups, "num")""")
        assertContains(companion, "range = collection.range")
    }

    @Test
    fun `custom line mode wires the raw line and every match`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Atom",
                    body = "data class Atom(val symbol: String)",
                ),
                entity(
                    name = "Molecule",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FromMatch", "aoc.ksp.MatchPart"),
                    body =
                        """
                        @GenerateStructure(customLine = true)
                        data class Molecule(
                            @FromMatch(MatchPart.LINE)
                            val stringValue: String,
                            @FromMatch(MatchPart.ALL_MATCHES)
                            val atoms: List<Atom>,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Molecule"))
        assertContains(companion, "object MoleculeCompanion : IStructureCustomLine<Molecule>")
        assertContains(companion, "stringValue = line")
        assertContains(companion, "atoms = collection.toList().map { Atom(it.value) }")
    }

    @Test
    fun `multi-structure mode routes on the uppercased subclass name`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Card",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(multiStructure = true, discriminatorField = "cmd")
                        sealed class Card {
                            data class Rect(val xPos: Int) : Card()

                            data class Row(val row: Int) : Card()
                        }
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Card"))
        assertContains(companion, """BaseEntity.getAsString(collection, "cmd").uppercase()""")
        assertContains(companion, """"RECT" -> {""")
        assertContains(companion, """"ROW" -> {""")
        assertContains(companion, "Card.Rect(")
    }

    @Test
    fun `StructureName overrides the token a subclass answers to`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "DanceMove",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.StructureName"),
                    body =
                        """
                        @GenerateStructure(multiStructure = true, discriminatorField = "type")
                        sealed class DanceMove {
                            @StructureName("s")
                            data class Spin(val steps: Int) : DanceMove()

                            @StructureName("x")
                            data class Exchange(val from: Int, val to: Int) : DanceMove()
                        }
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("DanceMove"))
        assertContains(companion, """"S" -> {""")
        assertContains(companion, """"X" -> {""")
        assertFalse(companion.contains(""""SPIN" ->"""), "the alias should replace the class name, not join it")
        // The KDoc mapping is generated from the same resolved tokens, so it cannot drift.
        assertContains(companion, """ * - "S" -> Spin""")
    }

    @Test
    fun `a defaulted subclass parameter is left to the constructor`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "FactoryLine",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure(multiStructure = true, discriminatorField = "cmd")
                        sealed class FactoryLine {
                            data class Bot(
                                val botId: Int,
                                val inBin: MutableList<Int> = mutableListOf(),
                            ) : FactoryLine()
                        }
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("FactoryLine"))
        assertContains(companion, """botId = BaseEntity.getAsInt(collection, "botId")""")
        assertFalse(companion.contains("inBin"), "a defaulted parameter has no group to read")
    }

    @Test
    fun `a field converter is imported and called instead of a getter`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "Point",
                    imports = listOf("aoc.ksp.BaseEntity", "aoc.ksp.TypeConverter"),
                    body =
                        """
                        data class Point(val x: Int)

                        object PointConverter : TypeConverter<Point> {
                            override fun convert(
                                collection: MatchGroupCollection,
                                fieldName: String,
                            ): Point = Point(BaseEntity.getAsInt(collection, fieldName))
                        }
                        """.trimIndent(),
                ),
                entity(
                    name = "Line",
                    imports = listOf("aoc.ksp.GenerateStructure", "aoc.ksp.FieldConverter"),
                    body =
                        """
                        @GenerateStructure
                        data class Line(
                            @FieldConverter(PointConverter::class)
                            val start: Point,
                        )
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        val companion = assertNotNull(result.companionFor("Line"))
        assertContains(companion, "import test.PointConverter")
        assertContains(companion, """start = PointConverter.convert(collection, "start")""")
    }

    @Test
    fun `an entity that already has a companion is skipped with a warning`() {
        val result =
            CompilationFixture.process(
                entity(
                    name = "HandRolled",
                    imports = listOf("aoc.ksp.GenerateStructure"),
                    body =
                        """
                        @GenerateStructure
                        data class HandRolled(
                            val x: Int,
                        ) {
                            companion object
                        }
                        """.trimIndent(),
                ),
            )

        assertTrue(result.succeeded, result.messages)
        assertNull(result.companionFor("HandRolled"))
        assertContains(result.messages, "already has a companion object, skipping generation")
    }
}
