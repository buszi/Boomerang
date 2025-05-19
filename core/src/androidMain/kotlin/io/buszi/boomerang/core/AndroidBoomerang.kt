package io.buszi.boomerang.core

import android.os.Bundle

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
        bundle.putString(key, value)
    }

    override fun getString(key: String): String? {
        return bundle.getString(key)
    }

    override fun putInt(key: String, value: Int) {
        bundle.putInt(key, value)
    }

    override fun getInt(key: String): Int? {
        return if (bundle.containsKey(key)) bundle.getInt(key) else null
    }

    override fun putLong(key: String, value: Long) {
        bundle.putLong(key, value)
    }

    override fun getLong(key: String): Long? {
        return if (bundle.containsKey(key)) bundle.getLong(key) else null
    }

    override fun putFloat(key: String, value: Float) {
        bundle.putFloat(key, value)
    }

    override fun getFloat(key: String): Float? {
        return if (bundle.containsKey(key)) bundle.getFloat(key) else null
    }

    override fun putDouble(key: String, value: Double) {
        bundle.putDouble(key, value)
    }

    override fun getDouble(key: String): Double? {
        return if (bundle.containsKey(key)) bundle.getDouble(key) else null
    }

    override fun putBoolean(key: String, value: Boolean) {
        bundle.putBoolean(key, value)
    }

    override fun getBoolean(key: String): Boolean? {
        return if (bundle.containsKey(key)) bundle.getBoolean(key) else null
    }

    override fun putBoomerang(key: String, boomerang: Boomerang) {
        bundle.putBundle(key, (boomerang as AndroidBoomerang).bundle)
    }

    override fun getBoomerang(key: String): Boomerang? {
        return if (bundle.containsKey(key)) bundle.getBundle(key)?.let(::AndroidBoomerang) else null
    }

    override fun contains(key: String): Boolean {
        return bundle.containsKey(key)
    }

    override fun getKeys(): Set<String> {
        return bundle.keySet()
    }

    override fun remove(key: String) {
        bundle.remove(key)
    }

    override fun clear() {
        bundle.clear()
    }
}
