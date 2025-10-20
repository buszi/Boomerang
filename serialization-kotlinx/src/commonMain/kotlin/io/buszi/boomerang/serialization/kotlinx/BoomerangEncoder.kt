package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.Boomerang
import io.buszi.boomerang.core.emptyBoomerang
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
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
        // Support List/Collection. If at root (no current property), store under reserved ROOT_LIST_KEY.
        val property = currentPropertyName ?: ROOT_LIST_KEY
        return ListEncoder(property)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        // For root-level object, we serialize into the same boomerang (no current property set)
        if (currentPropertyName == null) return this

        // For nested objects (non-list) under a property, create a sub-boomerang and store it under the property key
        return when (descriptor.kind) {
            is StructureKind.LIST -> this // Collections are handled by beginCollection
            else -> {
                val property = getCurrentPropertyName()
                val sub = emptyBoomerang()
                ObjectEncoder(sub) { finished ->
                    boomerang.putBoomerang(property, finished)
                }
            }
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        // Reset the current property name when we're done with the structure
        currentPropertyName = null
    }

    private inner class ListEncoder(
        private val propertyName: String,
    ) : AbstractEncoder() {
        private val elements = mutableListOf<Boomerang>()
        private var currentElement: Boomerang? = null

        override val serializersModule: SerializersModule
            get() = this@RootBoomerangEncoder.serializersModule

        private fun requireCurrentElement(): Boomerang =
            requireNotNull(currentElement) { "Element must be selected via encodeElement before encoding value" }

        override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
            // Start a new element container
            currentElement = emptyBoomerang()
            return true
        }

        // Primitive/enum elements are stored under key "v"
        private fun key() = "v"

        override fun encodeString(value: String) {
            requireCurrentElement().putString(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeInt(value: Int) {
            requireCurrentElement().putInt(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeLong(value: Long) {
            requireCurrentElement().putLong(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeFloat(value: Float) {
            requireCurrentElement().putFloat(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeDouble(value: Double) {
            requireCurrentElement().putDouble(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeBoolean(value: Boolean) {
            requireCurrentElement().putBoolean(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeByte(value: Byte) {
            requireCurrentElement().putByte(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeChar(value: Char) {
            requireCurrentElement().putChar(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeShort(value: Short) {
            requireCurrentElement().putShort(key(), value)
            elements.add(requireCurrentElement())
        }

        override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
            requireCurrentElement().putString(key(), enumDescriptor.getElementName(index))
            elements.add(requireCurrentElement())
        }

        override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
            // For object elements, encode into the current element boomerang using a sub-encoder
            val target = requireCurrentElement()
            return ObjectEncoder(target) { finished ->
                elements.add(finished)
            }
        }

        override fun endStructure(descriptor: SerialDescriptor) {
            // When finishing the collection, store all accumulated elements under the list key
            if (descriptor.kind is StructureKind.LIST) {
                boomerang.putBoomerangsList(propertyName, elements)
            }
        }
    }

    private inner class ObjectEncoder(
        private val target: Boomerang,
        private val onFinished: ((Boomerang) -> Unit)? = null,
    ) : AbstractEncoder() {
        private var currentPropertyName: String? = null

        override val serializersModule: SerializersModule
            get() = this@RootBoomerangEncoder.serializersModule

        private fun name(): String = requireNotNull(currentPropertyName) { "Property name must be set before encoding" }

        override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
            currentPropertyName = descriptor.getElementName(index)
            return true
        }

        override fun encodeString(value: String) { target.putString(name(), value) }
        override fun encodeInt(value: Int) { target.putInt(name(), value) }
        override fun encodeLong(value: Long) { target.putLong(name(), value) }
        override fun encodeFloat(value: Float) { target.putFloat(name(), value) }
        override fun encodeDouble(value: Double) { target.putDouble(name(), value) }
        override fun encodeBoolean(value: Boolean) { target.putBoolean(name(), value) }
        override fun encodeByte(value: Byte) { target.putByte(name(), value) }
        override fun encodeChar(value: Char) { target.putChar(name(), value) }
        override fun encodeShort(value: Short) { target.putShort(name(), value) }
        override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
            target.putString(name(), enumDescriptor.getElementName(index))
        }

        override fun encodeNull() { /* ignore */ }

        override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
            // Nested collections inside object element
            val prop = name()
            return ListInObjectEncoder(target, prop)
        }

        override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = this

        override fun endStructure(descriptor: SerialDescriptor) {
            // If this ObjectEncoder was used as a list element encoder, notify enclosing list
            if (descriptor.kind !is StructureKind.LIST) {
                onFinished?.invoke(target)
            }
            currentPropertyName = null
        }
    }

    private inner class ListInObjectEncoder(
        private val target: Boomerang,
        private val propertyName: String,
    ) : AbstractEncoder() {
        private val elements = mutableListOf<Boomerang>()
        private var currentElement: Boomerang? = null

        override val serializersModule: SerializersModule
            get() = this@RootBoomerangEncoder.serializersModule

        private fun requireCurrentElement(): Boomerang =
            requireNotNull(currentElement) { "Element must be selected via encodeElement before encoding value" }

        override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
            currentElement = emptyBoomerang()
            return true
        }

        private fun key() = "v"

        override fun encodeString(value: String) { requireCurrentElement().putString(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeInt(value: Int) { requireCurrentElement().putInt(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeLong(value: Long) { requireCurrentElement().putLong(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeFloat(value: Float) { requireCurrentElement().putFloat(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeDouble(value: Double) { requireCurrentElement().putDouble(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeBoolean(value: Boolean) { requireCurrentElement().putBoolean(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeByte(value: Byte) { requireCurrentElement().putByte(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeChar(value: Char) { requireCurrentElement().putChar(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeShort(value: Short) { requireCurrentElement().putShort(key(), value); elements.add(requireCurrentElement()) }
        override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) { requireCurrentElement().putString(key(), enumDescriptor.getElementName(index)); elements.add(requireCurrentElement()) }

        override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
            val targetElement = requireCurrentElement()
            return ObjectEncoder(targetElement) { finished ->
                elements.add(finished)
            }
        }

        override fun endStructure(descriptor: SerialDescriptor) {
            if (descriptor.kind is StructureKind.LIST) {
                target.putBoomerangsList(propertyName, elements)
            }
        }
    }
}
