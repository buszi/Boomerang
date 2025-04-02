package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import io.buszi.boomerang.core.BoomerangCatcher

@Composable
fun CatchBoomerangLifecycleEffect(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    catcher: BoomerangCatcher,
) {
    val currentKey = rememberUpdatedState(key)
    val currentCatcher = rememberUpdatedState(catcher)
    val store = LocalBoomerangStore.current
    LifecycleEventEffect(lifecycleEvent) {
        store.tryCatch(currentKey.value, currentCatcher.value)
    }
}
