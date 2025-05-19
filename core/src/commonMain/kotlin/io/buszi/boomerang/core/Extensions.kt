package io.buszi.boomerang.core

/**
 * Extension function for BoomerangStore that allows storing a value using a builder function.
 * This is a convenient way to create and store a Boomerang in a single expression.
 * 
 * Example usage:
 * ```
 * boomerangStore.storeValue("myKey") {
 *     putString("name", "John")
 *     putInt("age", 30)
 * }
 * ```
 * 
 * @param key The key to store the value with
 * @param resultBuilder A lambda with receiver that configures the Boomerang instance
 */
inline fun BoomerangStore.storeValue(key: String, resultBuilder: Boomerang.() -> Unit) {
    storeValue(key, buildBoomerang(resultBuilder))
}
