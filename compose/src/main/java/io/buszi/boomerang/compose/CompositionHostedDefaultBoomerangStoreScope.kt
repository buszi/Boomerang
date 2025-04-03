package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.rememberSaveable
import io.buszi.boomerang.compose.LocalBoomerangStore.LocalBoomerangStoreInternal
import io.buszi.boomerang.core.DefaultBoomerangStore

@Composable
fun CompositionHostedDefaultBoomerangStoreScope(content: @Composable () -> Unit) {
    val store = rememberSaveable(saver = DefaultBoomerangStoreSaver) { DefaultBoomerangStore() }
    CompositionLocalProvider(LocalBoomerangStoreInternal provides store, content)
}
