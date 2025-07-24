package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.emptyBoomerang
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
class RootBoomerangEncoder(
    override val serializersModule: SerializersModule = EmptySerializersModule(),
) : AbstractEncoder() {
    val boomerang = emptyBoomerang()
    private var currentPropertyName: String? = null

    private fun getCurrentPropertyName(): String =
        requireNotNull(currentPropertyName) { "Property name must be set before encoding a value" }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        currentPropertyName = descriptor.getElementName(index)
        return true
    }

    override fun encodeString(value: String) {
        boomerang.putString(getCurrentPropertyName(), value)
    }

    override fun encodeInt(value: Int) {
        boomerang.putInt(getCurrentPropertyName(), value)
    }

    override fun encodeLong(value: Long) {
        boomerang.putLong(getCurrentPropertyName(), value)
    }

    override fun encodeFloat(value: Float) {
        boomerang.putFloat(getCurrentPropertyName(), value)
    }

    override fun encodeDouble(value: Double) {
        boomerang.putDouble(getCurrentPropertyName(), value)
    }

    override fun encodeBoolean(value: Boolean) {
        boomerang.putBoolean(getCurrentPropertyName(), value)
    }

    override fun encodeByte(value: Byte) {
        boomerang.putByte(getCurrentPropertyName(), value)
    }

    override fun encodeChar(value: Char) {
        boomerang.putChar(getCurrentPropertyName(), value)
    }

    override fun encodeShort(value: Short) {
        boomerang.putShort(getCurrentPropertyName(), value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        boomerang.putString(getCurrentPropertyName(), enumDescriptor.getElementName(index))
    }

    override fun encodeNull() {
        // Do nothing for null values
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        // For flat objects, we don't need to handle collections
        return this
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        // For flat objects, we don't need to handle nested structures
        return this
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        // Reset the current property name when we're done with the structure
        currentPropertyName = null
    }
}
