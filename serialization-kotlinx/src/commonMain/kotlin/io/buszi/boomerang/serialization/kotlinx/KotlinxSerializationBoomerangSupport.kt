package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.Boomerang
import io.buszi.boomerang.core.BoomerangConfig
import io.buszi.boomerang.core.BoomerangStore
import kotlinx.serialization.Serializable

/**
 * Extension function for [BoomerangStore] that stores a serializable object of type [T] with the specified key.
 *
 * This function serializes the object using Kotlinx Serialization before storing it in the BoomerangStore.
 *
 * @param T The type of serializable object to store
 * @param key The unique identifier for the stored value
 * @param value The serializable object to store
 */
inline fun <reified T : @Serializable Any> BoomerangStore.storeValue(key: String, value: T) {
    val boomerang = BoomerangConfig.format.serialize(value)
    storeValue(key, boomerang)
}

/**
 * Extension function for [BoomerangStore] that stores a serializable object of type [T].
 *
 * This function automatically uses the qualified name of type [T] as the key and
 * serializes the object using Kotlinx Serialization before storing it in the BoomerangStore.
 *
 * @param T The type of serializable object to store
 * @param value The serializable object to store
 */
inline fun <reified T : @Serializable Any> BoomerangStore.storeValue(value: T) {
    val boomerang = BoomerangConfig.format.serialize(value)
    storeValue(requireNotNull(T::class.qualifiedName), boomerang)
}

/**
 * Extension function for [BoomerangStore] that retrieves and deserializes an object of type [T] with the specified key.
 *
 * This function retrieves the boomerang with the given key and deserializes it using Kotlinx Serialization.
 *
 * @param T The type of serializable object to retrieve
 * @param key The unique identifier for the stored value
 * @return The deserialized object of type [T], or null if no value is found for the given key
 */
inline fun <reified T : @Serializable Any> BoomerangStore.getSerializable(key: String): T? {
    val boomerang = getValue(key) ?: return null
    return BoomerangConfig.format.deserialize(boomerang)
}

/**
 * Extension function for [BoomerangStore] that retrieves and deserializes an object of type [T].
 *
 * This function automatically uses the qualified name of type [T] as the key and
 * retrieves the boomerang with that key, then deserializes it using Kotlinx Serialization.
 *
 * @param T The type of serializable object to retrieve
 * @return The deserialized object of type [T], or null if no value is found for the type's qualified name
 */
inline fun <reified T : @Serializable Any> BoomerangStore.getSerializable(): T? {
    val boomerang = getValue(requireNotNull(T::class.qualifiedName)) ?: return null
    return BoomerangConfig.format.deserialize(boomerang)
}

/**
 * Extension function for [Boomerang] that adds a serializable object of type [T] with the specified key.
 *
 * This function serializes the object using Kotlinx Serialization before adding it to the Boomerang.
 *
 * @param T The type of serializable object to add
 * @param key The unique identifier for the stored value
 * @param value The serializable object to add
 */
inline fun <reified T : @Serializable Any> Boomerang.putSerializable(key: String, value: T) {
    val boomerang = BoomerangConfig.format.serialize(value)
    putBoomerang(key, boomerang)
}

/**
 * Extension function for [Boomerang] that retrieves and deserializes an object of type [T] with the specified key.
 *
 * This function retrieves the boomerang with the given key and deserializes it using Kotlinx Serialization.
 *
 * @param T The type of serializable object to retrieve
 * @param key The unique identifier for the stored value
 * @return The deserialized object of type [T], or null if no value is found for the given key
 */
inline fun <reified T : @Serializable Any> Boomerang.getSerializable(key: String): T? {
    val boomerang = getBoomerang(key) ?: return null
    return BoomerangConfig.format.deserialize(boomerang)
}
