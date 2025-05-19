package io.buszi.boomerang.core

import android.os.Bundle

/**
 * Converts an Android Bundle to an AndroidBoomerang.
 * 
 * This extension function is only available on the Android platform.
 * 
 * @return An AndroidBoomerang that wraps this Bundle
 */
fun Bundle.toBoomerang(): AndroidBoomerang = AndroidBoomerang(this)

/**
 * Converts a Boomerang to an Android Bundle.
 * 
 * This extension function is only available on the Android platform.
 * 
 * @return The Bundle from the AndroidBoomerang
 * @throws ClassCastException if this Boomerang is not an AndroidBoomerang
 */
fun Boomerang.toBundle(): Bundle = (this as AndroidBoomerang).bundle

/**
 * Stores an Android Bundle in a BoomerangStore with the given key.
 * 
 * This extension function is only available on the Android platform.
 * It converts the Bundle to an AndroidBoomerang before storing it.
 * 
 * @param key The key to store the value under
 * @param bundle The Android Bundle to store
 */
fun BoomerangStore.storeValue(key: String, bundle: Bundle) {
    storeValue(key, bundle.toBoomerang())
}
