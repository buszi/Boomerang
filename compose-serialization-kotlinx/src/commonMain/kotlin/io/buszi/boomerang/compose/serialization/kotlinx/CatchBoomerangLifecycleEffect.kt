package io.buszi.boomerang.compose.serialization.kotlinx

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import io.buszi.boomerang.compose.CatchBoomerangLifecycleEffect
import io.buszi.boomerang.serialization.kotlinx.kotlinxSerializationBoomerangCatcher
import kotlinx.serialization.Serializable

/**
 * A Composable effect that catches serializable objects of type [T] at a specific lifecycle event.
 * 
 * This function uses Kotlinx Serialization to deserialize the caught object before passing it to the catcher function.
 * The catcher function returns a boolean indicating whether the object was successfully handled.
 *
 * @param T The type of serializable object to catch
 * @param key The unique identifier for this effect, used to match the thrown object
 * @param lifecycleEvent The lifecycle event at which to catch the object, defaults to [Lifecycle.Event.ON_START]
 * @param catcher A function that processes the caught object and returns true if it was handled successfully
 */
@Composable
inline fun <reified T : @Serializable Any> CatchSerializableLifecycleEffect(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Boolean,
) {
    CatchBoomerangLifecycleEffect(
        key = key,
        lifecycleEvent = lifecycleEvent,
        catcher = kotlinxSerializationBoomerangCatcher<T>(catcher),
    )
}

/**
 * A Composable effect that consumes serializable objects of type [T] at a specific lifecycle event.
 * 
 * This function is similar to [CatchSerializableLifecycleEffect] but automatically returns true after
 * the catcher function is called, indicating that the object was successfully handled.
 *
 * @param T The type of serializable object to consume
 * @param key The unique identifier for this effect, used to match the thrown object
 * @param lifecycleEvent The lifecycle event at which to consume the object, defaults to [Lifecycle.Event.ON_START]
 * @param catcher A function that processes the caught object without needing to return a handling status
 */
@Composable
inline fun <reified T : @Serializable Any> ConsumeSerializableLifecycleEffect(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Unit,
) {
    CatchBoomerangLifecycleEffect(
        key = key,
        lifecycleEvent = lifecycleEvent,
        catcher = kotlinxSerializationBoomerangCatcher<T> { value ->
            catcher(value)
            true
        },
    )
}

/**
 * A Composable effect that catches serializable objects of type [T] at a specific lifecycle event.
 * 
 * This overload automatically uses the qualified name of type [T] as the key.
 * The catcher function returns a boolean indicating whether the object was successfully handled.
 *
 * @param T The type of serializable object to catch
 * @param lifecycleEvent The lifecycle event at which to catch the object, defaults to [Lifecycle.Event.ON_START]
 * @param catcher A function that processes the caught object and returns true if it was handled successfully
 */
@Composable
inline fun <reified T : @Serializable Any> CatchSerializableLifecycleEffect(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Boolean,
) {
    CatchSerializableLifecycleEffect(
        key = requireNotNull(T::class.qualifiedName),
        lifecycleEvent = lifecycleEvent,
        catcher = catcher,
    )
}

/**
 * A Composable effect that consumes serializable objects of type [T] at a specific lifecycle event.
 * 
 * This overload automatically uses the qualified name of type [T] as the key and is similar to 
 * [CatchSerializableLifecycleEffect] but automatically returns true after the catcher function is called,
 * indicating that the object was successfully handled.
 *
 * @param T The type of serializable object to consume
 * @param lifecycleEvent The lifecycle event at which to consume the object, defaults to [Lifecycle.Event.ON_START]
 * @param catcher A function that processes the caught object without needing to return a handling status
 */
@Composable
inline fun <reified T : @Serializable Any> ConsumeSerializableLifecycleEffect(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Unit,
) {
    ConsumeSerializableLifecycleEffect(
        key = requireNotNull(T::class.qualifiedName),
        lifecycleEvent = lifecycleEvent,
        catcher = catcher,
    )
}
