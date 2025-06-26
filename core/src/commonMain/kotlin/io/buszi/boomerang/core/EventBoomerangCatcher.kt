package io.buszi.boomerang.core

import io.buszi.boomerang.core.BoomerangStore.Companion.EVENT_KEY

/**
 * Creates a BoomerangCatcher specifically for handling event notifications.
 * This catcher checks if the Boomerang contains an event with the specified key
 * and calls the provided callback when the event is caught.
 *
 * This is useful when you only need to be notified that something happened,
 * without needing to pass any additional data.
 *
 * @param key The event key to match against
 * @param onEvent Callback function to execute when the event is caught
 * @return A BoomerangCatcher that handles the specified event
 */
inline fun eventBoomerangCatcher(
    key: String,
    crossinline onEvent: () -> Unit,
): BoomerangCatcher = BoomerangCatcher { boomerang ->
    val event = boomerang.getString(EVENT_KEY)
    if (event == key && boomerang.getKeys().size == 1) {
        onEvent()
        true
    } else {
        false
    }
}
