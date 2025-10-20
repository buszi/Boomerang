package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.Boomerang
import io.buszi.boomerang.core.emptyBoomerang
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
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
        // If this is a list/collection, return a dedicated decoder for it
        return when (descriptor.kind) {
            is StructureKind.LIST -> {
                val listKey = currentPropertyName ?: ROOT_LIST_KEY
                return ListDecoder(listKey)
            }
            else -> {
                // For root-level object, keep using this decoder
                if (currentPropertyName == null) return this
                // For nested object under a property, decode from the nested Boomerang stored under that key
                val key = getCurrentPropertyName()
                val nested = boomerang.getBoomerang(key) ?: emptyBoomerang()
                RootBoomerangDecoder(nested, serializersModule)
            }
        }
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

    private inner class ListDecoder(
        propertyName: String,
    ) : AbstractDecoder(), CompositeDecoder {
        private val elements: List<Boomerang> = boomerang.getBoomerangsList(propertyName) ?: emptyList()
        private var index: Int = 0
        private var currentElement: Boomerang? = null

        override val serializersModule: SerializersModule
            get() = this@RootBoomerangDecoder.serializersModule

        private fun element(): Boomerang = requireNotNull(currentElement) { "decodeElementIndex must be called before decoding an element" }

        override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
            if (index >= elements.size) return CompositeDecoder.DECODE_DONE
            currentElement = elements[index]
            val result = index
            index++
            return result
        }

        private fun key() = "v"

        override fun decodeString(): String = element().getString(key()) ?: ""
        override fun decodeInt(): Int = element().getInt(key()) ?: 0
        override fun decodeLong(): Long = element().getLong(key()) ?: 0L
        override fun decodeFloat(): Float = element().getFloat(key()) ?: 0f
        override fun decodeDouble(): Double = element().getDouble(key()) ?: 0.0
        override fun decodeBoolean(): Boolean = element().getBoolean(key()) ?: false
        override fun decodeByte(): Byte = element().getByte(key()) ?: 0
        override fun decodeChar(): Char = element().getChar(key()) ?: '\u0000'
        override fun decodeShort(): Short = element().getShort(key()) ?: 0

        override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
            val enumValue = element().getString(key()) ?: return 0
            return (0 until enumDescriptor.elementsCount).firstOrNull {
                enumDescriptor.getElementName(it) == enumValue
            } ?: 0
        }

        override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
            // For object elements, delegate to a new RootBoomerangDecoder using the element boomerang
            return RootBoomerangDecoder(element(), serializersModule)
        }

        override fun endStructure(descriptor: SerialDescriptor) {
            // Nothing special; when LIST finishes, parent will continue
        }
    }
}
