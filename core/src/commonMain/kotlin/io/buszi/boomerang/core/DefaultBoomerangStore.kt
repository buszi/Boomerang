package io.buszi.boomerang.core

import io.buszi.boomerang.core.BoomerangConfig.logger

/**
 * Default implementation of the BoomerangStore interface.
 * Uses a private mutable map to store key-value pairs.
 */
open class DefaultBoomerangStore : BoomerangStore {

    /**
     * The internal storage for key-value pairs.
     */
    protected val store = mutableMapOf<String, Boomerang>()

    /**
     * Retrieves a Boomerang value for the given key.
     *
     * @param key The key to retrieve the value for
     * @return The Boomerang value associated with the key, or null if no value exists
     */
    override fun getValue(key: String): Boomerang? {
        logger?.log("DefaultBoomerangStore", "Getting Boomerang for key $key")
        return store[key]
    }

    /**
     * Stores a Boomerang value with the given key.
     *
     * @param key The key to store the value with
     * @param value The Boomerang value to store
     */
    override fun storeValue(key: String, value: Boomerang) {
        logger?.log("DefaultBoomerangStore", "Storing Boomerang for key $key")
        store[key] = value
    }

    /**
     * Removes a value for the given key.
     *
     * @param key The key to remove the value for
     */
    override fun dropValue(key: String) {
        logger?.log("DefaultBoomerangStore", "Dropping Boomerang for key $key")
        store.remove(key)
    }

    /**
     * Packs the store into a Boomerang object.
     * Typically used to store current state during configuration changes in order to restore it later.
     *
     * @return A Boomerang object containing all key-value pairs in the store
     */
    override fun packState(): Boomerang = buildBoomerang {
        logger?.log("DefaultBoomerangStore", "Packing DefaultBoomerangStore state")
        store.forEach { (key, value) ->
            putBoomerang(key, value)
        }
    }

    /**
     * Restores the state of the store from a Boomerang object.
     * Typically used to restore state after configuration changes.
     *
     * @param boomerang The Boomerang object containing the state to restore
     */
    override fun restoreState(boomerang: Boomerang) {
        logger?.log("DefaultBoomerangStore", "Restoring DefaultBoomerangStore state")
        boomerang.getKeys().forEach { key ->
            boomerang.getBoomerang(key)?.let { store[key] = it }
        }
    }
}
