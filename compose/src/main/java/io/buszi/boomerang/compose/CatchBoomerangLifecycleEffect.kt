package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import io.buszi.boomerang.core.BoomerangCatcher
import io.buszi.boomerang.core.assertValidForBoomerangCatcher

/**
 * A Composable effect that tries to catch a boomerang value from the BoomerangStore when a specific lifecycle event occurs.
 * This effect is typically used to process data that was stored in the BoomerangStore when the UI becomes visible.
 * [Example usage](https://github.com/buszi/Boomerang/blob/main/app/src/main/java/io/buszi/boomerang/FullComposePreviewActivity.kt)
 *
 * @param key The key to try to catch the value for
 * @param lifecycleEvent The lifecycle event that triggers the catch attempt (default is ON_START)
 * @param catcher The BoomerangCatcher to use for catching the value
 * @throws IllegalArgumentException if the lifecycleEvent is not ON_START or ON_RESUME
 */
@Composable
fun CatchBoomerangLifecycleEffect(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    catcher: BoomerangCatcher,
) {
    lifecycleEvent.assertValidForBoomerangCatcher()
    val currentKey = rememberUpdatedState(key)
    val currentCatcher = rememberUpdatedState(catcher)
    val store = LocalBoomerangStore.current
    LifecycleEventEffect(lifecycleEvent) {
        store.tryConsumeValue(currentKey.value, currentCatcher.value)
    }
}
