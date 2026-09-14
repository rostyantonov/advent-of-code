package aoc.common.entity

import aoc.ksp.BaseEntity
import aoc.ksp.StructureName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BaseEntityTest {
    @Test
    fun `test getAsInt with valid integer`() {
        val regex = Regex("(?<num>\\d+)")
        val match = regex.matchEntire("42")!!

        assertEquals(42, BaseEntity.getAsInt(match.groups, "num"))
    }

    @Test
    fun `test getAsInt throws on null`() {
        val regex = Regex("(?<num>\\d+)")
        val match = regex.matchEntire("abc")

        assertThrows<NullPointerException> {
            BaseEntity.getAsInt(match!!.groups, "num")
        }
    }

    @Test
    fun `test getAsNullableInt returns null for invalid input`() {
        val regex = Regex("(?<num>\\w+)")
        val match = regex.matchEntire("abc")!!

        assertNull(BaseEntity.getAsNullableInt(match.groups, "num"))
    }

    @Test
    fun `test getAsString with valid string`() {
        val regex = Regex("(?<text>\\w+)")
        val match = regex.matchEntire("hello")!!

        assertEquals("hello", BaseEntity.getAsString(match.groups, "text"))
    }

    @Test
    fun `test getAsChar with single character`() {
        val regex = Regex("(?<ch>\\w)")
        val match = regex.matchEntire("A")!!

        assertEquals('A', BaseEntity.getAsChar(match.groups, "ch"))
    }

    @Test
    fun `test getAsChar returns first character of string`() {
        val regex = Regex("(?<ch>\\w+)")
        val match = regex.matchEntire("ABC")!!

        assertEquals('A', BaseEntity.getAsChar(match.groups, "ch"))
    }

    @Test
    fun `test nullable variants return null for non-existent groups`() {
        // Test with a group that exists but has null value
        val regex = Regex("(?<num>\\d+)?")
        val match = regex.matchEntire("")!!

        assertNull(BaseEntity.getAsNullableInt(match.groups, "num"))
        assertNull(BaseEntity.getAsNullableString(match.groups, "num"))
        assertNull(BaseEntity.getAsNullableChar(match.groups, "num"))
    }

    private enum class Switch {
        TURN_ON,

        @StructureName("L")
        Left,
    }

    @Test
    fun `test asEnum matches a constant name ignoring case and spaces`() {
        assertEquals(Switch.TURN_ON, BaseEntity.asEnum<Switch>("turn on"))
        assertEquals(Switch.TURN_ON, BaseEntity.asEnum<Switch>(" TURN_ON "))
    }

    @Test
    fun `test asEnum matches the token StructureName names`() {
        assertEquals(Switch.Left, BaseEntity.asEnum<Switch>("L"))
        assertEquals(Switch.Left, BaseEntity.asEnum<Switch>("l"))
    }

    @Test
    fun `test an aliased constant no longer answers to its own name`() {
        assertNull(BaseEntity.asNullableEnum<Switch>("Left"))

        val thrown = assertThrows<IllegalArgumentException> { BaseEntity.asEnum<Switch>("Left") }
        assertEquals("'Left' is not a Switch; expected one of TURN_ON, L", thrown.message)
    }

    @Test
    fun `test getAsEnum reads the alias out of a named group`() {
        val regex = Regex("(?<direction>[A-Z])")
        val match = regex.matchEntire("L")!!

        assertEquals(Switch.Left, BaseEntity.getAsEnum<Switch>(match.groups, "direction"))
    }
}
