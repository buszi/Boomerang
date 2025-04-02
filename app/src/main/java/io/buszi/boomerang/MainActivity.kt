package io.buszi.boomerang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.buszi.boomerang.compose.CatchBoomerangLifecycleEffect
import io.buszi.boomerang.compose.DefaultBoomerangStoreScope
import io.buszi.boomerang.compose.LocalBoomerangStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DefaultBoomerangStoreScope {
                Navigation()
            }
        }
    }
}

@Composable
private fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            var result by rememberSaveable { mutableStateOf<String?>(null) }
            CatchBoomerangLifecycleEffect("home") { bundle ->
                bundle.getString("result")?.let { result = it }
                true
            }
            Column {
                Text(text = "Some result = $result")
                Button({ navController.navigate("in_between") }) {
                    Text(text = "Navigate")
                }
                Button({ result = null }) {
                    Text(text = "Clear")
                }
            }
        }
        composable("in_between") {
            Column {
                Button({ navController.navigate("not_home") }) {
                    Text(text = "Nav")
                }
                val store = LocalBoomerangStore.current
                Button({ store.dropValue("home") }) {
                    Text(text = "Drop")
                }
                val activity = LocalActivity.current
                Button({ activity?.recreate() }) {
                    Text(text = "Recreate")
                }
            }
        }
        composable("not_home") {
            val store = LocalBoomerangStore.current
            Column {
                Text(text = "Not home")
                Button({
                    store.storeValue("home", bundleOf("result" to "yeah"))
                    navController.popBackStack()
                }) {
                    Text(text = "Store result and nav back")
                }
            }
        }
    }
}
