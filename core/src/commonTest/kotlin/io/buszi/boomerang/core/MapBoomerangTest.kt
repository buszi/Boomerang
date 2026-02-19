package io.buszi.boomerang.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapBoomerangTest {

    @Test
    fun putAndGetString() {
        val boomerang = MapBoomerang()
        boomerang.putString("key", "hello")
        assertEquals("hello", boomerang.getString("key"))
    }

    @Test
    fun putAndGetInt() {
        val boomerang = MapBoomerang()
        boomerang.putInt("key", 42)
        assertEquals(42, boomerang.getInt("key"))
    }

    @Test
    fun putAndGetLong() {
        val boomerang = MapBoomerang()
        boomerang.putLong("key", 1234567890L)
        assertEquals(1234567890L, boomerang.getLong("key"))
    }

    @Test
    fun putAndGetFloat() {
        val boomerang = MapBoomerang()
        boomerang.putFloat("key", 3.14f)
        assertEquals(3.14f, boomerang.getFloat("key"))
    }

    @Test
    fun putAndGetDouble() {
        val boomerang = MapBoomerang()
        boomerang.putDouble("key", 2.71828)
        assertEquals(2.71828, boomerang.getDouble("key"))
    }

    @Test
    fun putAndGetBoolean() {
        val boomerang = MapBoomerang()
        boomerang.putBoolean("key", true)
        assertEquals(true, boomerang.getBoolean("key"))

        boomerang.putBoolean("key2", false)
        assertEquals(false, boomerang.getBoolean("key2"))
    }

    @Test
    fun putAndGetByte() {
        val boomerang = MapBoomerang()
        boomerang.putByte("key", 127.toByte())
        assertEquals(127.toByte(), boomerang.getByte("key"))
    }

    @Test
    fun putAndGetByteArray() {
        val boomerang = MapBoomerang()
        val bytes = byteArrayOf(1, 2, 3, 4, 5)
        boomerang.putByteArray("key", bytes)
        val retrieved = boomerang.getByteArray("key")
        assertNotNull(retrieved)
        assertTrue(bytes.contentEquals(retrieved))
    }

    @Test
    fun putAndGetChar() {
        val boomerang = MapBoomerang()
        boomerang.putChar("key", 'Z')
        assertEquals('Z', boomerang.getChar("key"))
    }

    @Test
    fun putAndGetShort() {
        val boomerang = MapBoomerang()
        boomerang.putShort("key", 32767.toShort())
        assertEquals(32767.toShort(), boomerang.getShort("key"))
    }

    @Test
    fun putAndGetNestedBoomerang() {
        val inner = MapBoomerang()
        inner.putString("innerKey", "innerValue")

        val outer = MapBoomerang()
        outer.putBoomerang("nested", inner)

        val retrieved = outer.getBoomerang("nested")
        assertNotNull(retrieved)
        assertEquals("innerValue", retrieved.getString("innerKey"))
    }

    @Test
    fun putAndGetBoomerangsList() {
        val b1 = MapBoomerang().apply { putInt("id", 1) }
        val b2 = MapBoomerang().apply { putInt("id", 2) }

        val boomerang = MapBoomerang()
        boomerang.putBoomerangsList("list", listOf(b1, b2))

        val retrieved = boomerang.getBoomerangsList("list")
        assertNotNull(retrieved)
        assertEquals(2, retrieved.size)
        assertEquals(1, retrieved[0].getInt("id"))
        assertEquals(2, retrieved[1].getInt("id"))
    }

    @Test
    fun getBoomerangsListReturnsNullForNonBoomerangList() {
        val boomerang = MapBoomerang()
        boomerang.putString("key", "not a list")
        assertNull(boomerang.getBoomerangsList("key"))
    }

    @Test
    fun getMissingKeyReturnsNull() {
        val boomerang = MapBoomerang()
        assertNull(boomerang.getString("missing"))
        assertNull(boomerang.getInt("missing"))
        assertNull(boomerang.getLong("missing"))
        assertNull(boomerang.getFloat("missing"))
        assertNull(boomerang.getDouble("missing"))
        assertNull(boomerang.getBoolean("missing"))
        assertNull(boomerang.getByte("missing"))
        assertNull(boomerang.getByteArray("missing"))
        assertNull(boomerang.getChar("missing"))
        assertNull(boomerang.getShort("missing"))
        assertNull(boomerang.getBoomerang("missing"))
        assertNull(boomerang.getBoomerangsList("missing"))
    }

    @Test
    fun getWithWrongTypeReturnsNull() {
        val boomerang = MapBoomerang()
        boomerang.putString("key", "hello")
        assertNull(boomerang.getInt("key"))
        assertNull(boomerang.getLong("key"))
        assertNull(boomerang.getFloat("key"))
        assertNull(boomerang.getDouble("key"))
        assertNull(boomerang.getBoolean("key"))
        assertNull(boomerang.getBoomerang("key"))
    }

    @Test
    fun containsReturnsTrueForExistingKey() {
        val boomerang = MapBoomerang()
        boomerang.putString("key", "value")
        assertTrue(boomerang.contains("key"))
    }

    @Test
    fun containsReturnsFalseForMissingKey() {
        val boomerang = MapBoomerang()
        assertFalse(boomerang.contains("missing"))
    }

    @Test
    fun getKeysReturnsAllStoredKeys() {
        val boomerang = MapBoomerang()
        boomerang.putString("a", "1")
        boomerang.putInt("b", 2)
        boomerang.putBoolean("c", true)

        val keys = boomerang.getKeys()
        assertEquals(setOf("a", "b", "c"), keys)
    }

    @Test
    fun getKeysReturnsEmptySetWhenEmpty() {
        val boomerang = MapBoomerang()
        assertTrue(boomerang.getKeys().isEmpty())
    }

    @Test
    fun removeDeletesKey() {
        val boomerang = MapBoomerang()
        boomerang.putString("key", "value")
        assertTrue(boomerang.contains("key"))

        boomerang.remove("key")
        assertFalse(boomerang.contains("key"))
        assertNull(boomerang.getString("key"))
    }

    @Test
    fun removeNonExistentKeyIsNoOp() {
        val boomerang = MapBoomerang()
        boomerang.remove("missing") // should not throw
    }

    @Test
    fun clearRemovesAllEntries() {
        val boomerang = MapBoomerang()
        boomerang.putString("a", "1")
        boomerang.putInt("b", 2)
        boomerang.putBoolean("c", true)

        boomerang.clear()
        assertTrue(boomerang.getKeys().isEmpty())
        assertNull(boomerang.getString("a"))
    }

    @Test
    fun overwriteValueWithSameKey() {
        val boomerang = MapBoomerang()
        boomerang.putString("key", "first")
        boomerang.putString("key", "second")
        assertEquals("second", boomerang.getString("key"))
    }
}
