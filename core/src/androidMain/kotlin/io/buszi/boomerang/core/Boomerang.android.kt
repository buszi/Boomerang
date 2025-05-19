package io.buszi.boomerang.core

/**
 * Android-specific implementation of the BoomerangFactory.
 * 
 * Creates AndroidBoomerang instances that use Android's Bundle for storage.
 */
actual object BoomerangFactory {
    /**
     * Creates a new AndroidBoomerang instance.
     * 
     * @return A new AndroidBoomerang instance
     */
    actual fun create(): Boomerang = AndroidBoomerang()
}
