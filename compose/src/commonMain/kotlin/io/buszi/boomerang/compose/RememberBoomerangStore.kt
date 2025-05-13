package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import io.buszi.boomerang.core.BoomerangStore

@Composable
expect fun <T : BoomerangStore> rememberBoomerangStore(init: () -> T): T
