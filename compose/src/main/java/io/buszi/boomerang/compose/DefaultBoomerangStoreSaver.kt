package io.buszi.boomerang.compose

import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
import io.buszi.boomerang.core.DefaultBoomerangStore

val DefaultBoomerangStoreSaver = Saver<DefaultBoomerangStore, Bundle>(
    save = { store -> store.packState() },
    restore = { bundle -> DefaultBoomerangStore(bundle) },
)
