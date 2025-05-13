package io.buszi.boomerang.fragment

import android.os.Bundle
import io.buszi.boomerang.core.BoomerangStoreHost
import io.buszi.boomerang.core.DefaultBoomerangStore
import io.buszi.boomerang.core.toBoomerang
import io.buszi.boomerang.core.toBundle

/**
 * Key used to store and retrieve the DefaultBoomerangStore state in a Bundle.
 */
private const val DEFAULT_BOOMERANG_STORE_STATE_KEY = "default_boomerang_store_state_key"

/**
 * Extension function for BoomerangStoreHost that creates a new DefaultBoomerangStore or restores one from a saved state.
 * This function is typically called in a lifecycle-aware component's onCreate method (f.e. Activity) to initialize or restore the DefaultBoomerangStore.
 * [Example usage](https://github.com/buszi/Boomerang/blob/main/app/src/main/java/io/buszi/boomerang/FullFragmentPreviewActivity.kt)
 *
 * @param savedInstanceState The Bundle containing the saved state, or null if there is no saved state
 */
fun BoomerangStoreHost.createOrRestoreDefaultBoomerangStore(savedInstanceState: Bundle?) {
    boomerangStore = savedInstanceState
        ?.getBundle(DEFAULT_BOOMERANG_STORE_STATE_KEY)
        ?.let { savedDefaultBoomerangStoreState ->
            DefaultBoomerangStore().apply { restoreState(savedDefaultBoomerangStoreState.toBoomerang()) }
        }
        ?: DefaultBoomerangStore()
}

/**
 * Extension function for BoomerangStoreHost that saves the state of a DefaultBoomerangStore to a Bundle.
 * This function is typically called in a lifecycle-aware component's onSaveInstanceState method (f.e. Activity) to save the DefaultBoomerangStore's state.
 * [Example usage](https://github.com/buszi/Boomerang/blob/main/app/src/main/java/io/buszi/boomerang/FullFragmentPreviewActivity.kt)
 *
 * @param outState The Bundle to save the state to
 * @throws IllegalStateException if the BoomerangStore is not a DefaultBoomerangStore
 */
fun BoomerangStoreHost.saveDefaultBoomerangStoreState(outState: Bundle) {
    val defaultBoomerangStore = (boomerangStore as? DefaultBoomerangStore)
        ?: error("Saving custom BoomerangStore implementation state is not supported by saveDefaultBoomerangStoreState function.")

    outState.putBundle(DEFAULT_BOOMERANG_STORE_STATE_KEY, defaultBoomerangStore.packState().toBundle())
}
