package io.buszi.boomerang.core

/**
 * A key-value store for storing and retrieving data across different parts of your application.
 * This interface is implemented by platform-specific classes to provide a consistent API across all platforms.
 * 
 * Boomerang is designed to be used in multiplatform projects and works on Android, iOS, and Desktop.
 */
interface Boomerang {

    /**
     * Stores a String value with the given key.
     * 
     * @param key The key to store the value under
     * @param value The String value to store
     */
    fun putString(key: String, value: String)

    /**
     * Retrieves a String value for the given key.
     * 
     * @param key The key to retrieve the value for
     * @return The String value, or null if no value exists for the key
     */
    fun getString(key: String): String?

    /**
     * Stores an Int value with the given key.
     * 
     * @param key The key to store the value under
     * @param value The Int value to store
     */
    fun putInt(key: String, value: Int)

    /**
     * Retrieves an Int value for the given key.
     * 
     * @param key The key to retrieve the value for
     * @return The Int value, or null if no value exists for the key
     */
    fun getInt(key: String): Int?

    /**
     * Stores a Long value with the given key.
     * 
     * @param key The key to store the value under
     * @param value The Long value to store
     */
    fun putLong(key: String, value: Long)

    /**
     * Retrieves a Long value for the given key.
     * 
     * @param key The key to retrieve the value for
     * @return The Long value, or null if no value exists for the key
     */
    fun getLong(key: String): Long?

    /**
     * Stores a Float value with the given key.
     * 
     * @param key The key to store the value under
     * @param value The Float value to store
     */
    fun putFloat(key: String, value: Float)

    /**
     * Retrieves a Float value for the given key.
     * 
     * @param key The key to retrieve the value for
     * @return The Float value, or null if no value exists for the key
     */
    fun getFloat(key: String): Float?

    /**
     * Stores a Double value with the given key.
     * 
     * @param key The key to store the value under
     * @param value The Double value to store
     */
    fun putDouble(key: String, value: Double)

    /**
     * Retrieves a Double value for the given key.
     * 
     * @param key The key to retrieve the value for
     * @return The Double value, or null if no value exists for the key
     */
    fun getDouble(key: String): Double?

    /**
     * Stores a Boolean value with the given key.
     * 
     * @param key The key to store the value under
     * @param value The Boolean value to store
     */
    fun putBoolean(key: String, value: Boolean)

    /**
     * Retrieves a Boolean value for the given key.
     * 
     * @param key The key to retrieve the value for
     * @return The Boolean value, or null if no value exists for the key
     */
    fun getBoolean(key: String): Boolean?

    /**
     * Stores a Boomerang instance with the given key.
     * This allows for nested data structures.
     * 
     * @param key The key to store the Boomerang under
     * @param boomerang The Boomerang instance to store
     */
    fun putBoomerang(key: String, boomerang: Boomerang)

    /**
     * Retrieves a Boomerang instance for the given key.
     * 
     * @param key The key to retrieve the Boomerang for
     * @return The Boomerang instance, or null if no Boomerang exists for the key
     */
    fun getBoomerang(key: String): Boomerang?

    /**
     * Checks if a value exists for the given key.
     * 
     * @param key The key to check
     * @return true if a value exists for the key, false otherwise
     */
    fun contains(key: String): Boolean

    /**
     * Gets all keys in this Boomerang instance.
     * 
     * @return A Set of all keys
     */
    fun getKeys(): Set<String>

    /**
     * Removes the value for the given key.
     * 
     * @param key The key to remove the value for
     */
    fun remove(key: String)

    /**
     * Removes all values from this Boomerang instance.
     */
    fun clear()
}

/**
 * Factory for creating platform-specific Boomerang instances.
 * 
 * This is an expect declaration implemented differently on each platform:
 * - On Android, it creates an AndroidBoomerang backed by a Bundle
 * - On iOS and Desktop, it creates a MapBoomerang backed by a MutableMap
 */
expect object BoomerangFactory {
    /**
     * Creates a new platform-specific Boomerang instance.
     * 
     * @return A new Boomerang instance appropriate for the current platform
     */
    fun create(): Boomerang
}

/**
 * Creates a new Boomerang instance and configures it using the provided builder function.
 * 
 * This is a convenient way to create and populate a Boomerang in a single expression.
 * Works on all supported platforms (Android, iOS, Desktop).
 * 
 * @param builder A function that configures the Boomerang instance
 * @return The configured Boomerang instance
 */
inline fun buildBoomerang(builder: Boomerang.() -> Unit): Boomerang = BoomerangFactory.create().apply(builder)

/**
 * Creates a new Boomerang instance with the provided key-value pairs.
 * 
 * This is a convenient way to create a Boomerang with initial values.
 * Works on all supported platforms (Android, iOS, Desktop).
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
            else -> error("Unsupported type: ${value::class.simpleName}")
        }
    }
}
