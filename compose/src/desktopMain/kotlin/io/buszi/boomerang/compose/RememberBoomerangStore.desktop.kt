package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.buszi.boomerang.core.BoomerangStore

/**
 * Desktop-specific implementation of rememberBoomerangStore.
 * 
 * This implementation uses the standard remember function to keep the BoomerangStore instance
 * across recompositions. On desktop platforms, there are no configuration changes like on Android,
 * so we don't need the more complex state saving mechanism.
 *
 * @param T The type of BoomerangStore to remember
 * @param init A lambda that creates an initial instance of the BoomerangStore
 * @return The remembered BoomerangStore instance
 */
@Composable
actual fun <T : BoomerangStore> rememberBoomerangStore(init: () -> T): T = remember(init)
