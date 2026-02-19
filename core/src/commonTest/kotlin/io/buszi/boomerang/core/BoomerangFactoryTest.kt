package io.buszi.boomerang.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BoomerangFactoryTest {

    @Test
    fun buildBoomerangCreatesAndConfigures() {
        val boomerang = buildBoomerang {
            putString("name", "Alice")
            putInt("age", 30)
        }
        assertEquals("Alice", boomerang.getString("name"))
        assertEquals(30, boomerang.getInt("age"))
    }

    @Test
    fun buildBoomerangWithNoOpsCreatesEmpty() {
        val boomerang = buildBoomerang {}
        assertTrue(boomerang.getKeys().isEmpty())
    }

    @Test
    fun emptyBoomerangCreatesEmptyInstance() {
        val boomerang = emptyBoomerang()
        assertTrue(boomerang.getKeys().isEmpty())
        assertNull(boomerang.getString("any"))
    }

    @Test
    fun boomerangOfWithStringPair() {
        val boomerang = boomerangOf("name" to "Alice")
        assertEquals("Alice", boomerang.getString("name"))
    }

    @Test
    fun boomerangOfWithIntPair() {
        val boomerang = boomerangOf("age" to 42)
        assertEquals(42, boomerang.getInt("age"))
    }

    @Test
    fun boomerangOfWithLongPair() {
        val boomerang = boomerangOf("big" to 9999999999L)
        assertEquals(9999999999L, boomerang.getLong("big"))
    }

    @Test
    fun boomerangOfWithFloatPair() {
        val boomerang = boomerangOf("pi" to 3.14f)
        assertEquals(3.14f, boomerang.getFloat("pi"))
    }

    @Test
    fun boomerangOfWithDoublePair() {
        val boomerang = boomerangOf("e" to 2.71828)
        assertEquals(2.71828, boomerang.getDouble("e"))
    }

    @Test
    fun boomerangOfWithBooleanPair() {
        val boomerang = boomerangOf("flag" to true)
        assertEquals(true, boomerang.getBoolean("flag"))
    }

    @Test
    fun boomerangOfWithBytePair() {
        val boomerang = boomerangOf("b" to 42.toByte())
        assertEquals(42.toByte(), boomerang.getByte("b"))
    }

    @Test
    fun boomerangOfWithByteArrayPair() {
        val bytes = byteArrayOf(1, 2, 3)
        val boomerang = boomerangOf("bytes" to bytes)
        val retrieved = boomerang.getByteArray("bytes")
        assertNotNull(retrieved)
        assertTrue(bytes.contentEquals(retrieved))
    }

    @Test
    fun boomerangOfWithCharPair() {
        val boomerang = boomerangOf("ch" to 'X')
        assertEquals('X', boomerang.getChar("ch"))
    }

    @Test
    fun boomerangOfWithShortPair() {
        val boomerang = boomerangOf("s" to 100.toShort())
        assertEquals(100.toShort(), boomerang.getShort("s"))
    }

    @Test
    fun boomerangOfWithMultiplePairs() {
        val boomerang = boomerangOf(
            "name" to "Alice",
            "age" to 30,
            "active" to true,
        )
        assertEquals("Alice", boomerang.getString("name"))
        assertEquals(30, boomerang.getInt("age"))
        assertEquals(true, boomerang.getBoolean("active"))
    }

    @Test
    fun boomerangOfWithNoPairsCreatesEmpty() {
        val boomerang = boomerangOf()
        assertTrue(boomerang.getKeys().isEmpty())
    }

    @Test
    fun boomerangOfWithUnsupportedTypeThrows() {
        assertFailsWith<IllegalStateException> {
            boomerangOf("bad" to listOf(1, 2, 3))
        }
    }
}
