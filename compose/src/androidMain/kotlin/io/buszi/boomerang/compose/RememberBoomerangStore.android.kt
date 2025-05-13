package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import io.buszi.boomerang.core.BoomerangStore
import io.buszi.boomerang.core.toBoomerang
import io.buszi.boomerang.core.toBundle

@Composable
actual fun <T : BoomerangStore> rememberBoomerangStore(init: () -> T): T =
    rememberSaveable(
        saver = Saver(
            save = { it.packState().toBundle() },
            restore = { init().apply { restoreState(it.toBoomerang()) } },
        ),
        init = init,
    )
