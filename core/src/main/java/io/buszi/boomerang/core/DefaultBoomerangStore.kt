package io.buszi.boomerang.core

import android.os.Bundle

open class DefaultBoomerangStore() : BoomerangStore {

    protected val store = mutableMapOf<String, Bundle>()

    constructor(bundle: Bundle) : this() {
        bundle.keySet()?.filterNotNull()?.forEach { key ->
            bundle.getBundle(key)?.let { store[key] = it }
        }
    }

    override fun getValue(key: String): Bundle? = store[key]

    override fun storeValue(key: String, value: Bundle) {
        store[key] = value
    }

    override fun dropValue(key: String) {
        store.remove(key)
    }

    fun packState(): Bundle {
        val bundle = Bundle()
        store.forEach { (key, value) ->
            bundle.putBundle(key, value)
        }
        return bundle
    }
}
