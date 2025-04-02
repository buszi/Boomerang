package io.buszi.boomerang.core

import android.os.Bundle

open class BaseBoomerangStore : BoomerangStore {

    protected val store = mutableMapOf<String, Bundle?>()

    override fun tryConsumeValue(key: String): Bundle? {
        val value = store[key]
        if (value != null) store.remove(key)
        return value
    }

    override fun storeValue(key: String, value: Bundle) {
        store[key] = value
    }

    override fun clearValue(key: String) {
        store.remove(key)
    }
}
