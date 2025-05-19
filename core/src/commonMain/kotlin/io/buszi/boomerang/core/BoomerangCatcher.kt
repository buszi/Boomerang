package io.buszi.boomerang.core

/**
 * A functional interface for catching and processing Boomerang values from a BoomerangStore.
 * Implementations of this interface determine whether a Boomerang value should be "caught" (processed and removed).
 * 
 * This interface is available on all supported platforms (Android, iOS, Desktop).
 */
fun interface BoomerangCatcher {
    /**
     * Tries to catch (process) a Boomerang value.
     *
     * @param value The Boomerang value to try to catch
     * @return true if the value was successfully caught and should be removed from the store, false otherwise
     */
    fun tryCatch(value: Boomerang): Boolean
}
