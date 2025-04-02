package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.buszi.boomerang.core.BoomerangStore

object LocalBoomerangStore {

    internal val LocalBoomerangStoreInternal: ProvidableCompositionLocal<BoomerangStore?> =
        staticCompositionLocalOf { null }

    val current: BoomerangStore
        @Composable get() = requireNotNull(LocalBoomerangStoreInternal.current) {
            "Use DefaultBoomerangStoreScope or provide your own store implementation via LocalBoomerangStore"
        }
}
