package io.buszi.boomerang.fragment

import androidx.fragment.app.Fragment
import io.buszi.boomerang.core.BoomerangStore
import io.buszi.boomerang.core.BoomerangStoreHost

/**
 * Extension function for Fragment that finds a BoomerangStore by looking for a BoomerangStoreHost in the Fragment's activity.
 * This function is used to access the BoomerangStore from a Fragment.
 * [Example usage](https://github.com/buszi/Boomerang/blob/main/app/src/main/java/io/buszi/boomerang/FullFragmentPreviewActivity.kt)
 *
 * @return The BoomerangStore from the hosting Activity
 * @throws IllegalStateException if the Fragment is not attached to an Activity, if the Activity does not implement BoomerangStoreHost, or if the BoomerangStore is not initialized
 */
fun Fragment.findBoomerangStore(): BoomerangStore {
    val currentActivity = activity ?: error("This Fragment was not attached to an Activity yet.")
    val storeHost = (currentActivity as? BoomerangStoreHost)
        ?: error("Activity this Fragment is hosted in does not implement BoomerangStoreHost.")
    val store = storeHost.boomerangStore
        ?: error("BoomerangStore not initialized inside the host. Either the function was used after activity got destroyed or you forgot to initialize the store with f.e. restoreOrCreateDefaultBoomerangStore.")
    return store
}
