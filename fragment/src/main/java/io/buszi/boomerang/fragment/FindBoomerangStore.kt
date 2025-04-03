package io.buszi.boomerang.fragment

import androidx.fragment.app.Fragment
import io.buszi.boomerang.core.BoomerangStore
import io.buszi.boomerang.core.BoomerangStoreHost

fun Fragment.findBoomerangStore(): BoomerangStore {
    val currentActivity = activity ?: error("This Fragment was not attached to an Activity yet.")
    val storeHost = (currentActivity as? BoomerangStoreHost)
        ?: error("Activity this Fragment is hosted in does not implement BoomerangStoreHost.")
    val store = storeHost.boomerangStore
        ?: error("BoomerangStore not initialized inside the host. Either the function was used after activity got destroyed or you forgot to initialize the store with f.e. restoreOrCreateDefaultBoomerangStore.")
    return store
}
