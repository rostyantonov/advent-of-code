package aoc.common.entity

import aoc.ksp.BaseEntity
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
    fun `test getAsLong with valid long`() {
        val regex = Regex("(?<num>\\d+)")
        val match = regex.matchEntire("9876543210")!!

        assertEquals(9876543210L, BaseEntity.getAsLong(match.groups, "num"))
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
    fun `test getAsUShort with valid unsigned short`() {
        val regex = Regex("(?<num>\\d+)")
        val match = regex.matchEntire("1234")!!

        assertEquals(1234u.toUShort(), BaseEntity.getAsUShort(match.groups, "num"))
    }

    @Test
    fun `test getAsDouble with valid double`() {
        val regex = Regex("(?<num>[\\d.]+)")
        val match = regex.matchEntire("3.14159")!!

        assertEquals(3.14159, BaseEntity.getAsDouble(match.groups, "num"))
    }

    @Test
    fun `test getAsFloat with valid float`() {
        val regex = Regex("(?<num>[\\d.]+)")
        val match = regex.matchEntire("2.5")!!

        assertEquals(2.5f, BaseEntity.getAsFloat(match.groups, "num"))
    }

    @Test
    fun `test getAsBoolean with true`() {
        val regex = Regex("(?<bool>\\w+)")
        val match = regex.matchEntire("true")!!

        assertEquals(true, BaseEntity.getAsBoolean(match.groups, "bool"))
    }

    @Test
    fun `test getAsBoolean with false`() {
        val regex = Regex("(?<bool>\\w+)")
        val match = regex.matchEntire("false")!!

        assertEquals(false, BaseEntity.getAsBoolean(match.groups, "bool"))
    }

    @Test
    fun `test getAsByte with valid byte`() {
        val regex = Regex("(?<num>\\d+)")
        val match = regex.matchEntire("127")!!

        assertEquals(127.toByte(), BaseEntity.getAsByte(match.groups, "num"))
    }

    @Test
    fun `test getAsShort with valid short`() {
        val regex = Regex("(?<num>\\d+)")
        val match = regex.matchEntire("32000")!!

        assertEquals(32000.toShort(), BaseEntity.getAsShort(match.groups, "num"))
    }

    @Test
    fun `test nullable variants return null for non-existent groups`() {
        val regex = Regex("(?<exists>\\w+)")
        val match = regex.matchEntire("test")!!

        // Test with a group that exists but has null value
        val regex2 = Regex("(?<num>\\d+)?")
        val match2 = regex2.matchEntire("")!!

        assertNull(BaseEntity.getAsNullableInt(match2.groups, "num"))
        assertNull(BaseEntity.getAsNullableLong(match2.groups, "num"))
        assertNull(BaseEntity.getAsNullableDouble(match2.groups, "num"))
        assertNull(BaseEntity.getAsNullableFloat(match2.groups, "num"))
        assertNull(BaseEntity.getAsNullableBoolean(match2.groups, "num"))
        assertNull(BaseEntity.getAsNullableByte(match2.groups, "num"))
        assertNull(BaseEntity.getAsNullableShort(match2.groups, "num"))
        assertNull(BaseEntity.getAsNullableUShort(match2.groups, "num"))
    }
}
