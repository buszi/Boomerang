package io.buszi.boomerang.core

/**
 * iOS-specific implementation of the BoomerangFactory.
 * 
 * Creates MapBoomerang instances that use a MutableMap for storage.
 */
actual object BoomerangFactory {
    /**
     * Creates a new MapBoomerang instance.
     * 
     * @return A new MapBoomerang instance
     */
    actual fun create(): Boomerang = MapBoomerang()
}
