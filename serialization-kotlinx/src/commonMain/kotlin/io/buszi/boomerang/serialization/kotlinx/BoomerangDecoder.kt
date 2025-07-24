package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.Boomerang
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
class RootBoomerangDecoder(
    val boomerang: Boomerang,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
) : AbstractDecoder() {
    private var currentPropertyName: String? = null
    private var currentIndex = 0

    private fun getCurrentPropertyName(): String =
        requireNotNull(currentPropertyName) { "Property name must be set before decoding a value" }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (currentIndex >= descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        
        val propertyName = descriptor.getElementName(currentIndex)
        currentPropertyName = propertyName
        
        val index = currentIndex
        currentIndex++
        return index
    }

    override fun decodeNotNullMark(): Boolean {
        val propertyName = getCurrentPropertyName()
        return boomerang.contains(propertyName)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        // For flat objects, we don't need to handle nested structures
        return this
    }

    override fun decodeString(): String {
        return boomerang.getString(getCurrentPropertyName()) ?: ""
    }

    override fun decodeInt(): Int {
        return boomerang.getInt(getCurrentPropertyName()) ?: 0
    }

    override fun decodeLong(): Long {
        return boomerang.getLong(getCurrentPropertyName()) ?: 0L
    }

    override fun decodeFloat(): Float {
        return boomerang.getFloat(getCurrentPropertyName()) ?: 0f
    }

    override fun decodeDouble(): Double {
        return boomerang.getDouble(getCurrentPropertyName()) ?: 0.0
    }

    override fun decodeBoolean(): Boolean {
        return boomerang.getBoolean(getCurrentPropertyName()) ?: false
    }

    override fun decodeByte(): Byte {
        return boomerang.getByte(getCurrentPropertyName()) ?: 0
    }

    override fun decodeChar(): Char {
        return boomerang.getChar(getCurrentPropertyName()) ?: '\u0000'
    }

    override fun decodeShort(): Short {
        return boomerang.getShort(getCurrentPropertyName()) ?: 0
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        val enumValue = boomerang.getString(getCurrentPropertyName()) ?: return 0
        return (0 until enumDescriptor.elementsCount).firstOrNull { 
            enumDescriptor.getElementName(it) == enumValue 
        } ?: 0
    }

    override fun decodeNull(): Nothing? {
        return null
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        // Reset the current property name and index when we're done with the structure
        currentPropertyName = null
        currentIndex = 0
    }
}
