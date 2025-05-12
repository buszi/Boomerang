package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.buszi.boomerang.core.BoomerangStore

/**
 * Object that provides access to a BoomerangStore in Jetpack Compose.
 * This is the main entry point for accessing a BoomerangStore in a Compose UI.
 */
object LocalBoomerangStore {

    /**
     * Internal CompositionLocal that holds the current BoomerangStore.
     * This is internal to prevent direct access and modification.
     */
    internal val LocalBoomerangStoreInternal: ProvidableCompositionLocal<BoomerangStore?> =
        staticCompositionLocalOf { null }

    /**
     * The current BoomerangStore.
     * This property will throw an exception if accessed when no BoomerangStore is available.
     *
     * @throws IllegalArgumentException if no BoomerangStore is available
     */
    val current: BoomerangStore
        @Composable get() = requireNotNull(LocalBoomerangStoreInternal.current) {
            "BoomerangStore was not provided to the composition. Use CompositionHostedBoomerangStoreScope (or provide CompositionHostedDefaultBoomerangStoreScope for default implementation) or ActivityHostedBoomerangStoreScope."
        }
}
