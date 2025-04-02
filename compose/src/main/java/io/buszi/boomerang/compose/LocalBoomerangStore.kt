package io.buszi.boomerang.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.buszi.boomerang.core.BaseBoomerangStore
import io.buszi.boomerang.core.BoomerangStore

val LocalBoomerangStore: ProvidableCompositionLocal<BoomerangStore> =
    staticCompositionLocalOf { BaseBoomerangStore.DEFAULT_INSTANCE }
