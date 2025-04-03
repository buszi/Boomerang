package io.buszi.boomerang.core

import androidx.lifecycle.Lifecycle

fun Lifecycle.Event.assertValidForBoomerangCatcher() {
    require(this == Lifecycle.Event.ON_START || this == Lifecycle.Event.ON_RESUME) {
        "LifecycleEvent $this is not valid for catching a boomerang. Event should be ON_START or ON_RESUME."
    }
}
