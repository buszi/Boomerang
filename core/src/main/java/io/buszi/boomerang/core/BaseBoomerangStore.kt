package io.buszi.boomerang.core

import android.os.Bundle

open class BaseBoomerangStore : BoomerangStore {

    protected val store = mutableMapOf<String, Bundle?>()

    override fun getValue(key: String): Bundle? = store[key]

    override fun storeValue(key: String, value: Bundle) {
        store[key] = value
    }

    override fun dropValue(key: String) {
        store.remove(key)
    }

    companion object {
        val DEFAULT_INSTANCE = BaseBoomerangStore()
    }
}
