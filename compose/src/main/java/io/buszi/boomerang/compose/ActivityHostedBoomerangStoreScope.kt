package io.buszi.boomerang.compose

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.buszi.boomerang.compose.LocalBoomerangStore.LocalBoomerangStoreInternal
import io.buszi.boomerang.core.BoomerangStoreHost

@Composable
fun ActivityHostedBoomerangStoreScope(content: @Composable () -> Unit) {
    val storeHost = (LocalActivity.current as? BoomerangStoreHost)
        ?: error("Activity this Composable is hosted in does not implement BoomerangStoreHost.")
    val store = storeHost.boomerangStore
        ?: error("BoomerangStore was not initialized yet. Maybe you forgot to use restoreOrCreateDefaultBoomerangStore?")
    CompositionLocalProvider(LocalBoomerangStoreInternal provides store, content)
}
