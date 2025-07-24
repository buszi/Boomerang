package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.emptyBoomerang
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class BoomerangDecoderTest {

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
    fun testDecodeFlat() {
        // Create a Boomerang with test data
        val boomerang = emptyBoomerang().apply {
            putString("stringValue", "test string")
            putInt("intValue", 42)
            putLong("longValue", 1234567890L)
            putFloat("floatValue", 3.14f)
            putDouble("doubleValue", 2.71828)
            putBoolean("booleanValue", true)
            putByte("byteValue", 127)
            putChar("charValue", 'A')
            putShort("shortValue", 32767)
            putString("enumValue", "SMTH")
        }

        // Deserialize the Boomerang to a TestData object
        val testData = BoomerangFormat.deserialize<TestData>(boomerang)

        // Check that all properties are correctly deserialized
        assertEquals("test string", testData.stringValue)
        assertEquals(42, testData.intValue)
        assertEquals(1234567890L, testData.longValue)
        assertEquals(3.14f, testData.floatValue)
        assertEquals(2.71828, testData.doubleValue)
        assertEquals(true, testData.booleanValue)
        assertEquals(127.toByte(), testData.byteValue)
        assertEquals('A', testData.charValue)
        assertEquals(32767.toShort(), testData.shortValue)
        assertEquals(TestEnum.SMTH, testData.enumValue)
    }
}
