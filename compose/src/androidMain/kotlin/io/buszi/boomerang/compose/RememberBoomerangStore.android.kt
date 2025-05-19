package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import io.buszi.boomerang.core.BoomerangStore
import io.buszi.boomerang.core.toBoomerang
import io.buszi.boomerang.core.toBundle

/**
 * Android-specific implementation of rememberBoomerangStore.
 * 
 * This implementation uses rememberSaveable with a custom Saver to preserve the BoomerangStore's state
 * across configuration changes.
 * 
 * The Saver converts the BoomerangStore's state to an Android Bundle for saving and restores it
 * by creating a new BoomerangStore instance and applying the saved state.
 *
 * @param T The type of BoomerangStore to remember
 * @param init A lambda that creates an initial instance of the BoomerangStore
 * @return The remembered BoomerangStore instance that persists across configuration changes
 */
@Composable
actual fun <T : BoomerangStore> rememberBoomerangStore(init: () -> T): T =
    rememberSaveable(
        saver = Saver(
            save = { it.packState().toBundle() },
            restore = { init().apply { restoreState(it.toBoomerang()) } },
        ),
        init = init,
    )
