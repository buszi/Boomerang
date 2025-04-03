package io.buszi.boomerang.fragment

import android.os.Bundle
import io.buszi.boomerang.core.BoomerangStoreHost
import io.buszi.boomerang.core.DefaultBoomerangStore

private const val DEFAULT_BOOMERANG_STORE_STATE_KEY = "default_boomerang_store_state_key"

fun BoomerangStoreHost.createOrRestoreDefaultBoomerangStore(savedInstanceState: Bundle?) {
    boomerangStore = savedInstanceState
        ?.getBundle(DEFAULT_BOOMERANG_STORE_STATE_KEY)
        ?.let { savedDefaultBoomerangStoreState ->
            DefaultBoomerangStore(savedDefaultBoomerangStoreState)
        }
        ?: DefaultBoomerangStore()
}

fun BoomerangStoreHost.saveDefaultBoomerangStoreState(outState: Bundle) {
    val defaultBoomerangStore = (boomerangStore as? DefaultBoomerangStore)
        ?: error("Saving custom BoomerangStore implementation state is not supported by saveDefaultBoomerangStoreState function.")

    outState.putBundle(DEFAULT_BOOMERANG_STORE_STATE_KEY, defaultBoomerangStore.packState())
}
