package io.buszi.boomerang.fragment.serialization.kotlinx

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import io.buszi.boomerang.fragment.catchBoomerangWithLifecycleEvent
import io.buszi.boomerang.serialization.kotlinx.kotlinxSerializationBoomerangCatcher
import kotlinx.serialization.Serializable

/**
 * Extension function for Fragment that catches serializable objects of type [T] at a specific lifecycle event.
 * 
 * This function uses Kotlinx Serialization to deserialize the caught object before passing it to the catcher function.
 * The catcher function returns a boolean indicating whether the object was successfully handled.
 *
 * @param T The type of serializable object to catch
 * @param key The unique identifier for this catcher, used to match the thrown object
 * @param lifecycleEvent The lifecycle event at which to catch the object, defaults to [Lifecycle.Event.ON_START]
 * @param catcher A function that processes the caught object and returns true if it was handled successfully
 */
inline fun <reified T : @Serializable Any> Fragment.catchSerializableWithLifecycleEvent(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Boolean,
) {
    catchBoomerangWithLifecycleEvent(
        key = key,
        lifecycleEvent = lifecycleEvent,
        catcher = kotlinxSerializationBoomerangCatcher<T>(catcher),
    )
}

/**
 * Extension function for Fragment that consumes serializable objects of type [T] at a specific lifecycle event.
 * 
 * This function is similar to [catchSerializableWithLifecycleEvent] but automatically returns true after
 * the catcher function is called, indicating that the object was successfully handled.
 *
 * @param T The type of serializable object to consume
 * @param key The unique identifier for this consumer, used to match the thrown object
 * @param lifecycleEvent The lifecycle event at which to consume the object, defaults to [Lifecycle.Event.ON_START]
 * @param catcher A function that processes the caught object without needing to return a handling status
 */
inline fun <reified T : @Serializable Any> Fragment.consumeSerializableWithLifecycleEvent(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Unit,
) {
    catchBoomerangWithLifecycleEvent(
        key = key,
        lifecycleEvent = lifecycleEvent,
        catcher = kotlinxSerializationBoomerangCatcher<T> { value ->
            catcher(value)
            true
        },
    )
}

/**
 * Extension function for Fragment that catches serializable objects of type [T] at a specific lifecycle event.
 * 
 * This overload automatically uses the qualified name of type [T] as the key.
 * The catcher function returns a boolean indicating whether the object was successfully handled.
 *
 * @param T The type of serializable object to catch
 * @param lifecycleEvent The lifecycle event at which to catch the object, defaults to [Lifecycle.Event.ON_START]
 * @param catcher A function that processes the caught object and returns true if it was handled successfully
 */
inline fun <reified T : @Serializable Any> Fragment.catchSerializableWithLifecycleEvent(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Boolean,
) {
    catchSerializableWithLifecycleEvent(
        key = requireNotNull(T::class.qualifiedName),
        lifecycleEvent = lifecycleEvent,
        catcher = catcher,
    )
}

/**
 * Extension function for Fragment that consumes serializable objects of type [T] at a specific lifecycle event.
 * 
 * This overload automatically uses the qualified name of type [T] as the key and is similar to 
 * [catchSerializableWithLifecycleEvent] but automatically returns true after the catcher function is called,
 * indicating that the object was successfully handled.
 *
 * @param T The type of serializable object to consume
 * @param lifecycleEvent The lifecycle event at which to consume the object, defaults to [Lifecycle.Event.ON_START]
 * @param catcher A function that processes the caught object without needing to return a handling status
 */
inline fun <reified T : @Serializable Any> Fragment.consumeSerializableWithLifecycleEvent(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Unit,
) {
    consumeSerializableWithLifecycleEvent(
        key = requireNotNull(T::class.qualifiedName),
        lifecycleEvent = lifecycleEvent,
        catcher = catcher,
    )
}
