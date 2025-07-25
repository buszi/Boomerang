package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.BoomerangCatcher
import kotlinx.serialization.Serializable

/**
 * Creates a [BoomerangCatcher] that deserializes the caught boomerang into an object of type [T] using Kotlinx Serialization.
 *
 * This function simplifies the creation of a boomerang catcher that works with serializable objects.
 * It automatically handles the deserialization process before passing the typed object to the provided catcher function.
 *
 * @param T The type of serializable object to deserialize the boomerang into
 * @param catcher A function that processes the deserialized object and returns true if it was handled successfully
 * @return A [BoomerangCatcher] that deserializes the boomerang and delegates to the provided catcher function
 */
inline fun <reified T : @Serializable Any> kotlinxSerializationBoomerangCatcher(
    crossinline catcher: (T) -> Boolean,
) = BoomerangCatcher { boomerang ->
    val value = BoomerangFormat.deserialize<T>(boomerang)
    catcher(value)
}
