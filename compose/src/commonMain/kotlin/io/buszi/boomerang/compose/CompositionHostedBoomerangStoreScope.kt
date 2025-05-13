package io.buszi.boomerang.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.buszi.boomerang.compose.LocalBoomerangStore.LocalBoomerangStoreInternal
import io.buszi.boomerang.core.BoomerangStore
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
    CompositionHostedBoomerangStoreScope(
        init = { DefaultBoomerangStore() },
        content = content,
    )
}

/**
 * A Composable function that creates a custom BoomerangStore and provides it to its content via LocalBoomerangStore.
 * The store is saved and restored across configuration changes using rememberSaveable with a custom saver.
 * This function is useful when you want to use a custom implementation of BoomerangStore instead of the default one.
 * It should be used on the top-level of your Compose application before creating Navigation components.
 *
 * @param T The type of BoomerangStore to be created
 * @param S The type of the saved state for the BoomerangStore, typically a Bundle
 * @param init A lambda that creates an initial instance of the BoomerangStore
 * @param saver A Saver that defines how to save and restore the BoomerangStore's state, check [Saver] for more info
 * @param content The content that will have access to the BoomerangStore
 */
@Composable
fun <T : BoomerangStore> CompositionHostedBoomerangStoreScope(
    init: () -> T,
    content: @Composable () -> Unit,
) {
    val store = rememberBoomerangStore(init)
    CompositionLocalProvider(LocalBoomerangStoreInternal provides store, content)
}
