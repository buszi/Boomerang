package io.buszi.boomerang

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Boomerang Desktop Preview",
    ) {
        ComposePreviewApp(
            recreateApp = {
                // For desktop, we don't need to recreate the app, so this is a no-op
            }
        )
    }
}
