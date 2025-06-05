package io.buszi.boomerang.core

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

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

/**
 * Stores a Parcelable object in the Boomerang with the given key.
 * 
 * This extension function is only available on the Android platform.
 * It casts the Boomerang to AndroidBoomerang and uses the underlying Bundle.
 * 
 * @param key The key to store the value under
 * @param value The Parcelable object to store
 */
fun <T : Parcelable> Boomerang.putParcelable(key: String, value: T) {
    (this as AndroidBoomerang).bundle.putParcelable(key, value)
}

/**
 * Retrieves a Parcelable object from the Boomerang with the given key.
 * 
 * This extension function is only available on the Android platform.
 * It handles API level differences for retrieving Parcelables from a Bundle.
 * 
 * @param key The key to retrieve the value from
 * @return The Parcelable object, or null if not found or not of type T
 */
inline fun <reified T : Parcelable> Boomerang.getParcelable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        (this as AndroidBoomerang).bundle.getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        (this as AndroidBoomerang).bundle.getParcelable(key)
    }

/**
 * Stores a Java Serializable object in the Boomerang with the given key.
 * 
 * This extension function is only available on the Android platform.
 * It casts the Boomerang to AndroidBoomerang and uses the underlying Bundle.
 * 
 * @param key The key to store the value under
 * @param value The Serializable object to store
 */
fun Boomerang.putJavaSerializable(key: String, value: Serializable) {
    (this as AndroidBoomerang).bundle.putSerializable(key, value)
}

/**
 * Retrieves a Java Serializable object from the Boomerang with the given key.
 * 
 * This extension function is only available on the Android platform.
 * It handles API level differences for retrieving Serializable objects from a Bundle.
 * 
 * @param key The key to retrieve the value from
 * @return The Serializable object, or null if not found or not of type T
 */
inline fun <reified T : Serializable> Boomerang.getJavaSerializable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        (this as AndroidBoomerang).bundle.getSerializable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        (this as AndroidBoomerang).bundle.getSerializable(key) as T?
    }

/**
 * Stores an Android Bundle in the Boomerang with the given key.
 * 
 * This extension function is only available on the Android platform.
 * It casts the Boomerang to AndroidBoomerang and uses the underlying Bundle.
 * 
 * @param key The key to store the value under
 * @param value The Android Bundle to store
 */
fun Boomerang.putBundle(key: String, value: Bundle) {
    (this as AndroidBoomerang).bundle.putBundle(key, value)
}

/**
 * Retrieves an Android Bundle from the Boomerang with the given key.
 * 
 * This extension function is only available on the Android platform.
 * It casts the Boomerang to AndroidBoomerang and uses the underlying Bundle.
 * 
 * @param key The key to retrieve the value from
 * @return The Android Bundle, or null if not found
 */
fun Boomerang.getBundle(key: String): Bundle? =
    (this as AndroidBoomerang).bundle.getBundle(key)
