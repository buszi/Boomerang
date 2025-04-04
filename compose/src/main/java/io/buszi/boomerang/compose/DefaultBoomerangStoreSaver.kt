package io.buszi.boomerang.compose

import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
import io.buszi.boomerang.core.DefaultBoomerangStore

/**
 * A Saver for DefaultBoomerangStore that saves the store's state to a Bundle and restores it from a Bundle.
 * This is used with rememberSaveable to preserve the store's state across configuration changes.
 *
 * The save function packs the store's state into a Bundle using the packState method.
 * The restore function creates a new DefaultBoomerangStore initialized with the saved Bundle.
 */
val DefaultBoomerangStoreSaver = Saver<DefaultBoomerangStore, Bundle>(
    save = { store -> store.packState() },
    restore = { bundle -> DefaultBoomerangStore(bundle) },
)
