package io.buszi.boomerang.core

import io.buszi.boomerang.core.BoomerangConfig.logger

/**
 * A platform-agnostic implementation of the Boomerang interface that uses a MutableMap for storage.
 *
 * This implementation is used on iOS and Desktop platforms, providing a consistent API
 * across all platforms while using platform-appropriate storage mechanisms.
 */
class MapBoomerang : Boomerang {
    private val map = mutableMapOf<String, Any>()

    override fun putString(key: String, value: String) {
        logger?.log("MapBoomerang", "Storing String value for key: $key")
        map[key] = value
    }

    override fun getString(key: String): String? {
        val value = map[key] as? String
        logger?.log("MapBoomerang", "Retrieved String value for key: $key, value: $value")
        return value
    }

    override fun putInt(key: String, value: Int) {
        logger?.log("MapBoomerang", "Storing Int value for key: $key")
        map[key] = value
    }

    override fun getInt(key: String): Int? {
        val value = map[key] as? Int
        logger?.log("MapBoomerang", "Retrieved Int value for key: $key, value: $value")
        return value
    }

    override fun putLong(key: String, value: Long) {
        logger?.log("MapBoomerang", "Storing Long value for key: $key")
        map[key] = value
    }

    override fun getLong(key: String): Long? {
        val value = map[key] as? Long
        logger?.log("MapBoomerang", "Retrieved Long value for key: $key, value: $value")
        return value
    }

    override fun putFloat(key: String, value: Float) {
        logger?.log("MapBoomerang", "Storing Float value for key: $key")
        map[key] = value
    }

    override fun getFloat(key: String): Float? {
        val value = map[key] as? Float
        logger?.log("MapBoomerang", "Retrieved Float value for key: $key, value: $value")
        return value
    }

    override fun putDouble(key: String, value: Double) {
        logger?.log("MapBoomerang", "Storing Double value for key: $key")
        map[key] = value
    }

    override fun getDouble(key: String): Double? {
        val value = map[key] as? Double
        logger?.log("MapBoomerang", "Retrieved Double value for key: $key, value: $value")
        return value
    }

    override fun putBoolean(key: String, value: Boolean) {
        logger?.log("MapBoomerang", "Storing Boolean value for key: $key")
        map[key] = value
    }

    override fun getBoolean(key: String): Boolean? {
        val value = map[key] as? Boolean
        logger?.log("MapBoomerang", "Retrieved Boolean value for key: $key, value: $value")
        return value
    }

    override fun putBoomerang(key: String, boomerang: Boomerang) {
        logger?.log("MapBoomerang", "Storing nested Boomerang for key: $key")
        map[key] = boomerang
    }

    override fun getBoomerang(key: String): Boomerang? {
        val value = map[key] as? Boomerang
        logger?.log("MapBoomerang", "Retrieved nested Boomerang for key: $key, exists: ${value != null}")
        return value
    }

    override fun putBoomerangsList(key: String, boomerangs: List<Boomerang>) {
        logger?.log("MapBoomerang", "Storing list of Boomerangs of size ${boomerangs.size} for key: $key")
        map[key] = ArrayList(boomerangs)
    }

    override fun getBoomerangsList(key: String): List<Boomerang>? {
        val uncheckedValue = map[key] as? List<*>
        val value = uncheckedValue
            ?.filterIsInstance<Boomerang>()
            ?.takeIf { it.size == uncheckedValue.size }
        logger?.log("MapBoomerang", "Retrieved list of Boomerangs of size ${value?.size} for key: $key, exists: ${value != null}")
        return value
    }

    override fun putByte(key: String, value: Byte) {
        logger?.log("MapBoomerang", "Storing Byte value for key: $key")
        map[key] = value
    }

    override fun getByte(key: String): Byte? {
        val value = map[key] as? Byte
        logger?.log("MapBoomerang", "Retrieved Byte value for key: $key, value: $value")
        return value
    }

    override fun putByteArray(key: String, value: ByteArray) {
        logger?.log("MapBoomerang", "Storing ByteArray value for key: $key")
        map[key] = value
    }

    override fun getByteArray(key: String): ByteArray? {
        val value = map[key] as? ByteArray
        logger?.log("MapBoomerang", "Retrieved ByteArray value for key: $key, exists: ${value != null}")
        return value
    }

    override fun putChar(key: String, value: Char) {
        logger?.log("MapBoomerang", "Storing Char value for key: $key")
        map[key] = value
    }

    override fun getChar(key: String): Char? {
        val value = map[key] as? Char
        logger?.log("MapBoomerang", "Retrieved Char value for key: $key, value: $value")
        return value
    }

    override fun putShort(key: String, value: Short) {
        logger?.log("MapBoomerang", "Storing Short value for key: $key")
        map[key] = value
    }

    override fun getShort(key: String): Short? {
        val value = map[key] as? Short
        logger?.log("MapBoomerang", "Retrieved Short value for key: $key, value: $value")
        return value
    }

    override fun contains(key: String): Boolean {
        val result = map.containsKey(key)
        logger?.log("MapBoomerang", "Checking if contains key: $key, result: $result")
        return result
    }

    override fun getKeys(): Set<String> {
        val keys = map.keys
        logger?.log("MapBoomerang", "Getting all keys, count: ${keys.size}")
        return keys
    }

    override fun remove(key: String) {
        logger?.log("MapBoomerang", "Removing value for key: $key")
        map.remove(key)
    }

    override fun clear() {
        logger?.log("MapBoomerang", "Clearing all values")
        map.clear()
    }
}
