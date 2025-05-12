package io.buszi.boomerang.core

/**
 * A functional interface for catching and processing Bundle values from a BoomerangStore.
 * Implementations of this interface determine whether a Bundle value should be "caught" (processed and removed).
 */
fun interface BoomerangCatcher {
    /**
     * Tries to catch (process) a Bundle value.
     *
     * @param value The Bundle value to try to catch
     * @return true if the value was successfully caught and should be removed from the store, false otherwise
     */
    fun tryCatch(value: Boomerang): Boolean
}