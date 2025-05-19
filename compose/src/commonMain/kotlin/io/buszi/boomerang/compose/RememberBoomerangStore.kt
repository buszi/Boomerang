package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import io.buszi.boomerang.core.BoomerangStore

/**
 * Remembers a BoomerangStore across recompositions and configuration changes.
 * This function is implemented differently on each platform to ensure proper state saving and restoration.
 * 
 * On Android, it uses rememberSaveable with a custom saver to preserve the store's state across configuration changes.
 * On other platforms, it uses remember to keep the store instance across recompositions.
 *
 * @param T The type of BoomerangStore to remember
 * @param init A lambda that creates an initial instance of the BoomerangStore
 * @return The remembered BoomerangStore instance
 */
@Composable
expect fun <T : BoomerangStore> rememberBoomerangStore(init: () -> T): T
