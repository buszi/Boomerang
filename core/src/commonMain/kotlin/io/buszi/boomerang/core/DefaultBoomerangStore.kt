package io.buszi.boomerang.core

/**
 * Default implementation of the BoomerangStore interface.
 * Uses a private mutable map to store key-value pairs.
 */
open class DefaultBoomerangStore() : BoomerangStore {

    /**
     * The internal storage for key-value pairs.
     */
    protected val store = mutableMapOf<String, Boomerang>()

    /**
     * Constructs a DefaultBoomerangStore initialized with the contents of the given Bundle.
     * Typically used for state restoration.
     *
     * @param boomerang The Bundle to initialize the store with
     */
    constructor(boomerang: Boomerang) : this() {
        restoreState(boomerang)
    }

    /**
     * Retrieves a Bundle value for the given key.
     *
     * @param key The key to retrieve the value for
     * @return The Bundle value associated with the key, or null if no value exists
     */
    override fun getValue(key: String): Boomerang? = store[key]

    /**
     * Stores a Bundle value with the given key.
     *
     * @param key The key to store the value with
     * @param value The Bundle value to store
     */
    override fun storeValue(key: String, value: Boomerang) {
        store[key] = value
    }

    /**
     * Removes a value for the given key.
     *
     * @param key The key to remove the value for
     */
    override fun dropValue(key: String) {
        store.remove(key)
    }

    /**
     * Packs the store into a Bundle.
     * Typically used to store current state during configuration changes in order to restore it using the constructor that accepts Bundle.
     *
     * @return A Bundle containing all key-value pairs in the store
     */
    fun packState(): Boomerang = buildBoomerang {
        store.forEach { (key, value) ->
            putBoomerang(key, value)
        }
    }

    protected fun restoreState(boomerang: Boomerang) {
        boomerang.getKeys().forEach { key ->
            boomerang.getBoomerang(key)?.let { store[key] = it }
        }
    }
}