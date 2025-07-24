package io.buszi.boomerang.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.buszi.boomerang.core.Boomerang
import io.buszi.boomerang.core.BoomerangCatcher
import io.buszi.boomerang.core.assertValidForBoomerangCatcher
import io.buszi.boomerang.core.eventBoomerangCatcher

/**
 * Extension function for Fragment that sets up a lifecycle observer to catch a boomerang value when a specific lifecycle event occurs.
 * This function should be called in the Fragment's onCreate method to not multiply the observers.
 * The observer will try to catch the value when the specified lifecycle event occurs and will remove itself when the fragment is destroyed.
 * [Example usage](https://github.com/buszi/Boomerang/blob/main/app/src/main/java/io/buszi/boomerang/FullFragmentPreviewActivity.kt)
 *
 * @param key The key to try to catch the value for
 * @param lifecycleEvent The lifecycle event that triggers the catch attempt (default is ON_START)
 * @param catcher The BoomerangCatcher to use for catching the value
 * @throws IllegalArgumentException if the lifecycleEvent is not ON_START or ON_RESUME
 * @throws IllegalStateException if called after Fragment's onCreate, if the Fragment is not attached to an Activity, 
 *                              if the Activity does not implement BoomerangStoreHost, or if the BoomerangStore is not initialized
 */
fun Fragment.catchBoomerangWithLifecycleEvent(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    catcher: BoomerangCatcher,
) {
    lifecycleEvent.assertValidForBoomerangCatcher()
    if (lifecycle.currentState != Lifecycle.State.INITIALIZED)
        error("This function should be used in Fragment's onCreate.")

    val observer = object : LifecycleEventObserver {
        override fun onStateChanged(
            source: LifecycleOwner,
            event: Lifecycle.Event
        ) {
            if (event == lifecycleEvent) {
                val store = findBoomerangStore()
                store.tryConsumeValue(key, catcher)
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                lifecycle.removeObserver(this)
            }
        }
    }
    lifecycle.addObserver(observer)
}

/**
 * Extension function for Fragment that sets up a lifecycle observer to catch a boomerang value when a specific lifecycle event occurs.
 * This is an inline version that accepts a lambda function instead of a BoomerangCatcher.
 * This function should be called in the Fragment's onCreate method to not multiply the observers.
 * [Example usage](https://github.com/buszi/Boomerang/blob/main/app/src/main/java/io/buszi/boomerang/FullFragmentPreviewActivity.kt)
 *
 * @param key The key to try to catch the value for
 * @param lifecycleEvent The lifecycle event that triggers the catch attempt (default is ON_START)
 * @param catcher Lambda function that will be called with the caught Boomerang value
 * @throws IllegalArgumentException if the lifecycleEvent is not ON_START or ON_RESUME
 * @throws IllegalStateException if called after Fragment's onCreate, if the Fragment is not attached to an Activity, 
 *                              if the Activity does not implement BoomerangStoreHost, or if the BoomerangStore is not initialized
 */
inline fun Fragment.catchBoomerangWithLifecycleEvent(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (Boomerang) -> Unit,
) {
    catchBoomerangWithLifecycleEvent(
        key = key,
        lifecycleEvent = lifecycleEvent,
        catcher = BoomerangCatcher { boomerang ->
            catcher(boomerang)
            true
        },
    )
}

/**
 * Extension function for Fragment that simplifies catching event notifications from the BoomerangStore.
 * This is a specialized version of catchBoomerangWithLifecycleEvent specifically for handling
 * simple event notifications without additional data.
 * 
 * This function should be called in the Fragment's onCreate method to not multiply the observers.
 * [Example usage](https://github.com/buszi/Boomerang/blob/main/app/src/androidMain/kotlin/io/buszi/boomerang/FullFragmentPreviewActivity.kt)
 *
 * @param key The event key to catch
 * @param lifecycleEvent The lifecycle event that triggers the catch attempt (default is ON_START)
 * @param onEvent Callback function to execute when the event is caught
 * @throws IllegalArgumentException if the lifecycleEvent is not ON_START or ON_RESUME
 * @throws IllegalStateException if called after Fragment's onCreate, if the Fragment is not attached to an Activity,
 *                              if the Activity does not implement BoomerangStoreHost, or if the BoomerangStore is not initialized
 */
inline fun Fragment.catchEventBoomerangWithLifecycleEvent(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline onEvent: () -> Unit,
) {
    catchBoomerangWithLifecycleEvent(
        key = key,
        lifecycleEvent = lifecycleEvent,
        catcher = eventBoomerangCatcher(key, onEvent),
    )
}
