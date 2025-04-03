package io.buszi.boomerang.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.buszi.boomerang.core.BoomerangCatcher
import io.buszi.boomerang.core.assertValidForBoomerangCatcher

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
                store.tryCatch(key, catcher)
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                lifecycle.removeObserver(this)
            }
        }
    }
    lifecycle.addObserver(observer)
}
