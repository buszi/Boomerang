package io.buszi.boomerang

import androidx.compose.ui.window.ComposeUIViewController

fun AppViewController() = ComposeUIViewController {
    ComposePreviewApp(recreateApp = {
        // For now, we do not support process death on iOS
    })
}
