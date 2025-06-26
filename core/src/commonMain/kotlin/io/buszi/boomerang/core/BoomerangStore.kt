package io.buszi.boomerang.core

import io.buszi.boomerang.core.BoomerangConfig.logger

/**
 * Interface for a key-value store that stores navigation results as Boomerang objects.
 * This store is used to pass data between different parts of the application.
 *
 * This interface is available on all supported platforms (Android, iOS, Desktop).
 */
interface BoomerangStore {

    /**
     * Retrieves a Boomerang value for the given key.
     *
     * @param key The key to retrieve the value for
     * @return The Boomerang value associated with the key, or null if no value exists
     */
    fun getValue(key: String): Boomerang?

    /**
     * Stores a Boomerang value with the given key.
     *
     * @param key The key to store the value with
     * @param value The Boomerang value to store
     */
    fun storeValue(key: String, value: Boomerang)

    /**
     * Stores an event notification with the given key.
     * Unlike storeValue, this method doesn't require a value parameter as it creates
     * a simple event notification without additional data.
     *
     * @param key The key to store the event with
     */
    fun storeEvent(key: String)

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
        logger?.log("BoomerangStore", "Trying to consume value for key $key")
        getValue(key)?.let { value ->
            logger?.log("BoomerangStore", "Boomerang for key $key is present")
            val isCaught = catcher.tryCatch(value)
            if (isCaught) {
                logger?.log("BoomerangStore", "Boomerang for key $key caught by catcher")
                dropValue(key)
            } else {
                logger?.log("BoomerangStore", "Boomerang for key $key not caught by catcher")
            }
        } ?: run {
            logger?.log("BoomerangStore", "Boomerang for key $key not present")
        }
    }

    /**
     * Packs the current state of the store into a Boomerang object.
     * This is useful for saving the state of the store for later restoration.
     *
     * @return A Boomerang object containing the current state of the store
     */
    fun packState(): Boomerang

    /**
     * Restores the state of the store from a Boomerang object.
     * This is useful for restoring a previously saved state.
     *
     * @param boomerang The Boomerang object containing the state to restore
     */
    fun restoreState(boomerang: Boomerang)

    companion object {
        const val EVENT_KEY = "boomerang_event_internal"
    }
}
