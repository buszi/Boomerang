package io.buszi.boomerang.core

/**
 * A platform-agnostic implementation of the Boomerang interface that uses a MutableMap for storage.
 * 
 * This implementation is used on iOS and Desktop platforms, providing a consistent API
 * across all platforms while using platform-appropriate storage mechanisms.
 */
class MapBoomerang : Boomerang {
    private val map = mutableMapOf<String, Any>()

    override fun putString(key: String, value: String) {
        map[key] = value
    }

    override fun getString(key: String): String? {
        return map[key] as? String
    }

    override fun putInt(key: String, value: Int) {
        map[key] = value
    }

    override fun getInt(key: String): Int? {
        return map[key] as? Int
    }

    override fun putLong(key: String, value: Long) {
        map[key] = value
    }

    override fun getLong(key: String): Long? {
        return map[key] as? Long
    }

    override fun putFloat(key: String, value: Float) {
        map[key] = value
    }

    override fun getFloat(key: String): Float? {
        return map[key] as? Float
    }

    override fun putDouble(key: String, value: Double) {
        map[key] = value
    }

    override fun getDouble(key: String): Double? {
        return map[key] as? Double
    }

    override fun putBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    override fun getBoolean(key: String): Boolean? {
        return map[key] as? Boolean
    }

    override fun putBoomerang(key: String, boomerang: Boomerang) {
        map[key] = boomerang
    }

    override fun getBoomerang(key: String): Boomerang? {
        return map[key] as? Boomerang
    }

    override fun contains(key: String): Boolean {
        return map.containsKey(key)
    }

    override fun getKeys(): Set<String> {
        return map.keys
    }

    override fun remove(key: String) {
        map.remove(key)
    }

    override fun clear() {
        map.clear()
    }
}
