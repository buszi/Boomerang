package io.buszi.boomerang.core

import androidx.lifecycle.Lifecycle

/**
 * Asserts that the lifecycle event is valid for catching a boomerang.
 * Valid events are ON_START and ON_RESUME.
 *
 * @throws IllegalArgumentException if the event is not ON_START or ON_RESUME
 */
fun Lifecycle.Event.assertValidForBoomerangCatcher() {
    require(this == Lifecycle.Event.ON_START || this == Lifecycle.Event.ON_RESUME) {
        "LifecycleEvent $this is not valid for catching a boomerang. Event should be ON_START or ON_RESUME."
    }
}
