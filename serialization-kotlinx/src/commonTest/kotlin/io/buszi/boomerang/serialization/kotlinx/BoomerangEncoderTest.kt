package io.buszi.boomerang.serialization.kotlinx

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BoomerangEncoderTest {

    enum class TestEnum {
        SMTH
    }

    @Serializable
    data class TestData(
        val stringValue: String,
        val intValue: Int,
        val longValue: Long,
        val floatValue: Float,
        val doubleValue: Double,
        val booleanValue: Boolean,
        val byteValue: Byte,
        val charValue: Char,
        val shortValue: Short,
        val enumValue: TestEnum,
    )

    @Test
    fun testEncodeFlat() {
        // Create a test data object with various property types
        val testData = TestData(
            stringValue = "test string",
            intValue = 42,
            longValue = 1234567890L,
            floatValue = 3.14f,
            doubleValue = 2.71828,
            booleanValue = true,
            byteValue = 127,
            charValue = 'A',
            shortValue = 32767,
            enumValue = TestEnum.SMTH,
        )

        val boomerang = BoomerangFormat.serialize(testData)

        // Check that all properties are present
        assertTrue(boomerang.contains("stringValue"))
        assertTrue(boomerang.contains("intValue"))
        assertTrue(boomerang.contains("longValue"))
        assertTrue(boomerang.contains("floatValue"))
        assertTrue(boomerang.contains("doubleValue"))
        assertTrue(boomerang.contains("booleanValue"))
        assertTrue(boomerang.contains("byteValue"))
        assertTrue(boomerang.contains("charValue"))
        assertTrue(boomerang.contains("shortValue"))
        assertTrue(boomerang.contains("enumValue"))

        // Check that the values are correct
        assertEquals("test string", boomerang.getString("stringValue"))
        assertEquals(42, boomerang.getInt("intValue"))
        assertEquals(1234567890L, boomerang.getLong("longValue"))
        assertEquals(3.14f, boomerang.getFloat("floatValue"))
        assertEquals(2.71828, boomerang.getDouble("doubleValue"))
        assertEquals(true, boomerang.getBoolean("booleanValue"))
        assertEquals(127.toByte(), boomerang.getByte("byteValue"))
        assertEquals('A', boomerang.getChar("charValue"))
        assertEquals(32767.toShort(), boomerang.getShort("shortValue"))
        assertEquals("SMTH", boomerang.getString("enumValue"))
    }
}
