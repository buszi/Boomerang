package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.buszi.boomerang.core.BoomerangStore

@Composable
actual fun <T : BoomerangStore> rememberBoomerangStore(init: () -> T): T = remember(init)
