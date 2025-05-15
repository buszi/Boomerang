package io.buszi.boomerang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * This activity demonstrates the usage of Boomerang with Jetpack Compose.
 * It showcases four key scenarios:
 * 1. Test navigation result is passed correctly
 * 2. Test navigation result is persisted correctly across configuration changes
 * 3. Test value can be dropped
 * 4. Test value can be proxied by another catcher in between
 */
class FullComposePreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ComposePreviewApp(recreateApp = { recreate() })
        }
    }
}
