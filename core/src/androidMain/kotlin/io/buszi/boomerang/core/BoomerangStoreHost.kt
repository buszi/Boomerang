package io.buszi.boomerang.core

/**
 * Interface for a component that hosts a BoomerangStore.
 * This interface is typically implemented by Activities or other lifecycle-aware components that need to provide a BoomerangStore to their children Fragments.
 * Implementation of this interface is required for Fragments and mixed navigation result to work.
 * In order to create instance and save instance state of [DefaultBoomerangStore] check createOrRestoreDefaultBoomerangStore and saveDefaultBoomerangStoreState in fragment module.
 * [Example implementation](https://github.com/buszi/Boomerang/blob/main/app/src/main/java/io/buszi/boomerang/FullFragmentPreviewActivity.kt)
 */
interface BoomerangStoreHost {
    /**
     * The BoomerangStore hosted by this component. Should be null by default.
     * This property can be null if the store has not been initialized yet or has been destroyed.
     */
    var boomerangStore: BoomerangStore?
}
