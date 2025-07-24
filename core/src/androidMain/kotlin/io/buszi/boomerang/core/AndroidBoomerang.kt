package io.buszi.boomerang.core

import android.os.Build
import android.os.Bundle
import io.buszi.boomerang.core.BoomerangConfig.logger

/**
 * Android-specific implementation of the Boomerang interface that uses Android's Bundle for storage.
 * 
 * This implementation is used only on the Android platform, providing a consistent API
 * across all platforms while using platform-appropriate storage mechanisms.
 * 
 * @property bundle The Bundle used for storage
 */
class AndroidBoomerang(val bundle: Bundle = Bundle()) : Boomerang {

    override fun putString(key: String, value: String) {
        logger?.log("AndroidBoomerang", "Storing String value for key: $key")
        bundle.putString(key, value)
    }

    override fun getString(key: String): String? {
        val value = bundle.getString(key)
        logger?.log("AndroidBoomerang", "Retrieved String value for key: $key, value: $value")
        return value
    }

    override fun putInt(key: String, value: Int) {
        logger?.log("AndroidBoomerang", "Storing Int value for key: $key")
        bundle.putInt(key, value)
    }

    override fun getInt(key: String): Int? {
        val value = if (bundle.containsKey(key)) bundle.getInt(key) else null
        logger?.log("AndroidBoomerang", "Retrieved Int value for key: $key, value: $value")
        return value
    }

    override fun putLong(key: String, value: Long) {
        logger?.log("AndroidBoomerang", "Storing Long value for key: $key")
        bundle.putLong(key, value)
    }

    override fun getLong(key: String): Long? {
        val value = if (bundle.containsKey(key)) bundle.getLong(key) else null
        logger?.log("AndroidBoomerang", "Retrieved Long value for key: $key, value: $value")
        return value
    }

    override fun putFloat(key: String, value: Float) {
        logger?.log("AndroidBoomerang", "Storing Float value for key: $key")
        bundle.putFloat(key, value)
    }

    override fun getFloat(key: String): Float? {
        val value = if (bundle.containsKey(key)) bundle.getFloat(key) else null
        logger?.log("AndroidBoomerang", "Retrieved Float value for key: $key, value: $value")
        return value
    }

    override fun putDouble(key: String, value: Double) {
        logger?.log("AndroidBoomerang", "Storing Double value for key: $key")
        bundle.putDouble(key, value)
    }

    override fun getDouble(key: String): Double? {
        val value = if (bundle.containsKey(key)) bundle.getDouble(key) else null
        logger?.log("AndroidBoomerang", "Retrieved Double value for key: $key, value: $value")
        return value
    }

    override fun putBoolean(key: String, value: Boolean) {
        logger?.log("AndroidBoomerang", "Storing Boolean value for key: $key")
        bundle.putBoolean(key, value)
    }

    override fun getBoolean(key: String): Boolean? {
        val value = if (bundle.containsKey(key)) bundle.getBoolean(key) else null
        logger?.log("AndroidBoomerang", "Retrieved Boolean value for key: $key, value: $value")
        return value
    }

    override fun putBoomerang(key: String, boomerang: Boomerang) {
        logger?.log("AndroidBoomerang", "Storing nested Boomerang for key: $key")
        bundle.putBundle(key, boomerang.toBundle())
    }

    override fun getBoomerang(key: String): Boomerang? {
        val value = if (bundle.containsKey(key)) bundle.getBundle(key)?.let(::AndroidBoomerang) else null
        logger?.log("AndroidBoomerang", "Retrieved nested Boomerang for key: $key, exists: ${value != null}")
        return value
    }

    override fun putBoomerangsList(key: String, boomerangs: List<Boomerang>) {
        logger?.log("AndroidBoomerang", "Storing list of Boomerangs of size ${boomerangs.size} for key: $key")
        val arrayList = ArrayList<Bundle>()
        boomerangs.forEach { arrayList.add(it.toBundle()) }
        bundle.putParcelableArrayList(key, arrayList)
    }

    override fun getBoomerangsList(key: String): List<Boomerang>? {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelableArrayList(key, Bundle::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelableArrayList(key)
        }
            ?.map(::AndroidBoomerang)
        logger?.log("AndroidBoomerang", "Retrieved list of Boomerangs of size ${value?.size} for key: $key, exists: ${value != null}")
        return value
    }

    override fun putByte(key: String, value: Byte) {
        logger?.log("AndroidBoomerang", "Storing Byte value for key: $key")
        bundle.putByte(key, value)
    }

    override fun getByte(key: String): Byte? {
        val value = if (bundle.containsKey(key)) bundle.getByte(key) else null
        logger?.log("AndroidBoomerang", "Retrieved Byte value for key: $key, value: $value")
        return value
    }

    override fun putByteArray(key: String, value: ByteArray) {
        logger?.log("AndroidBoomerang", "Storing ByteArray value for key: $key")
        bundle.putByteArray(key, value)
    }

    override fun getByteArray(key: String): ByteArray? {
        val value = bundle.getByteArray(key)
        logger?.log("AndroidBoomerang", "Retrieved ByteArray value for key: $key, exists: ${value != null}")
        return value
    }

    override fun putChar(key: String, value: Char) {
        logger?.log("AndroidBoomerang", "Storing Char value for key: $key")
        bundle.putChar(key, value)
    }

    override fun getChar(key: String): Char? {
        val value = if (bundle.containsKey(key)) bundle.getChar(key) else null
        logger?.log("AndroidBoomerang", "Retrieved Char value for key: $key, value: $value")
        return value
    }

    override fun putShort(key: String, value: Short) {
        logger?.log("AndroidBoomerang", "Storing Short value for key: $key")
        bundle.putShort(key, value)
    }

    override fun getShort(key: String): Short? {
        val value = if (bundle.containsKey(key)) bundle.getShort(key) else null
        logger?.log("AndroidBoomerang", "Retrieved Short value for key: $key, value: $value")
        return value
    }

    override fun contains(key: String): Boolean {
        val result = bundle.containsKey(key)
        logger?.log("AndroidBoomerang", "Checking if contains key: $key, result: $result")
        return result
    }

    override fun getKeys(): Set<String> {
        val keys = bundle.keySet()
        logger?.log("AndroidBoomerang", "Getting all keys, count: ${keys.size}")
        return keys
    }

    override fun remove(key: String) {
        logger?.log("AndroidBoomerang", "Removing value for key: $key")
        bundle.remove(key)
    }

    override fun clear() {
        logger?.log("AndroidBoomerang", "Clearing all values")
        bundle.clear()
    }
}
