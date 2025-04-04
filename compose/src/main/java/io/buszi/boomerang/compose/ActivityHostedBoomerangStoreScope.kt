package io.buszi.boomerang.compose

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.buszi.boomerang.compose.LocalBoomerangStore.LocalBoomerangStoreInternal
import io.buszi.boomerang.core.BoomerangStoreHost

/**
 * A Composable function that provides a BoomerangStore from an Activity that implements BoomerangStoreHost to its content.
 * This function retrieves the BoomerangStore from the hosting Activity and makes it available to the content via LocalBoomerangStore.
 * This function should be used when your app uses both Composables and Fragments for navigation. For more info check [BoomerangStoreHost].
 *
 * @param content The content that will have access to the BoomerangStore
 * @throws IllegalStateException if the hosting Activity does not implement BoomerangStoreHost or if the BoomerangStore is not initialized
 */
@Composable
fun ActivityHostedBoomerangStoreScope(content: @Composable () -> Unit) {
    val storeHost = (LocalActivity.current as? BoomerangStoreHost)
        ?: error("Activity this Composable is hosted in does not implement BoomerangStoreHost.")
    val store = storeHost.boomerangStore
        ?: error("BoomerangStore was not initialized yet. Maybe you forgot to use restoreOrCreateDefaultBoomerangStore?")
    CompositionLocalProvider(LocalBoomerangStoreInternal provides store, content)
}
