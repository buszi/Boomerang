package io.buszi.boomerang.core

/**
 * Interface for a key-value store that stores navigation results as Android Bundles.
 * This store is used to pass data between different parts of the application.
 */
interface BoomerangStore {

    /**
     * Retrieves a Bundle value for the given key.
     *
     * @param key The key to retrieve the value for
     * @return The Bundle value associated with the key, or null if no value exists
     */
    fun getValue(key: String): Boomerang?

    /**
     * Stores a Bundle value with the given key.
     *
     * @param key The key to store the value with
     * @param value The Bundle value to store
     */
    fun storeValue(key: String, value: Boomerang)

    /**
     * Removes a value for the given key.
     *
     * @param key The key to remove the value for
     */
    fun dropValue(key: String)

    /**
     * Tries to consume a value using a BoomerangCatcher. Default implementation drops the value if it's caught (catcher returns true).
     *
     * @param key The key to try to catch the value for
     * @param catcher The BoomerangCatcher to use for catching the value
     */
    fun tryConsumeValue(key: String, catcher: BoomerangCatcher) {
        getValue(key)?.let { value ->
            val isCaught = catcher.tryCatch(value)
            if (isCaught) dropValue(key)
        }
    }

    fun packState(): Boomerang

    fun restoreState(boomerang: Boomerang)
}
