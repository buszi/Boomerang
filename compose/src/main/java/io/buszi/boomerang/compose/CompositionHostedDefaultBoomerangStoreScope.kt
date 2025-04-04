package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.rememberSaveable
import io.buszi.boomerang.compose.LocalBoomerangStore.LocalBoomerangStoreInternal
import io.buszi.boomerang.core.DefaultBoomerangStore

/**
 * A Composable function that creates a DefaultBoomerangStore and provides it to its content via LocalBoomerangStore.
 * The store is saved and restored across configuration changes using rememberSaveable with a custom saver.
 * This function is useful when you have fully Composable navigation. It should be used on the top-level of your Compose application before creating Navigation components (f.e. before calling NavHost in Jetpack Navigation).
 * [Example usage](https://github.com/buszi/Boomerang/blob/main/app/src/main/java/io/buszi/boomerang/FullComposePreviewActivity.kt)
 *
 * @param content The content that will have access to the BoomerangStore
 */
@Composable
fun CompositionHostedDefaultBoomerangStoreScope(content: @Composable () -> Unit) {
    val store = rememberSaveable(saver = DefaultBoomerangStoreSaver) { DefaultBoomerangStore() }
    CompositionLocalProvider(LocalBoomerangStoreInternal provides store, content)
}
