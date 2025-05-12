package io.buszi.boomerang.core

import android.os.Bundle

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

    override fun contains(key: String): Boolean {
        return bundle.containsKey(key)
    }

    override fun remove(key: String) {
        bundle.remove(key)
    }

    override fun clear() {
        bundle.clear()
    }
}
