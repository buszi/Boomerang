package io.buszi.boomerang.core

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * Android-specific implementation of boomerangOf that adds support for Parcelable, Serializable, and Bundle types.
 * 
 * This function extends the common implementation to handle Android-specific types.
 * 
 * @param pairs Key-value pairs to store in the Boomerang
 * @return A new Boomerang instance with the provided key-value pairs
 * @throws IllegalArgumentException if any value has an unsupported type
 */
fun boomerangOf(vararg pairs: Pair<String, Any>): Boomerang = buildBoomerang {
    pairs.forEach { (key, value) ->
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> putDouble(key, value)
            is Boolean -> putBoolean(key, value)
            is Byte -> putByte(key, value)
            is ByteArray -> putByteArray(key, value)
            is Char -> putChar(key, value)
            is Short -> putShort(key, value)
            is Bundle -> putBundle(key, value)
            is Parcelable -> putParcelable(key, value)
            is Serializable -> putJavaSerializable(key, value)
            else -> error("Unsupported type: ${value::class.simpleName}")
        }
    }
}
