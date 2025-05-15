package io.buszi.boomerang

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.buszi.boomerang.compose.CatchBoomerangLifecycleEffect
import io.buszi.boomerang.compose.CompositionHostedDefaultBoomerangStoreScope
import io.buszi.boomerang.compose.LocalBoomerangStore
import io.buszi.boomerang.core.boomerangOf

@Composable
fun ComposePreviewApp(recreateApp: () -> Unit) {
    CompositionHostedDefaultBoomerangStoreScope {
        MaterialTheme {
            Surface {
                Navigation(recreateApp)
            }
        }
    }
}

@Composable
private fun Navigation(recreateApp: () -> Unit) {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            // Using rememberSaveable to persist the result across recompositions
            var result by rememberSaveable { mutableStateOf<String?>(null) }

            // TEST CASE 1: Test navigation result is passed correctly
            // CatchBoomerangLifecycleEffect will try to catch a value with the key "home"
            // when the composable enters the ON_START lifecycle event
            CatchBoomerangLifecycleEffect("home") { bundle ->
                // Extract the result from the bundle and update the state
                bundle.getString("result")?.let { result = it }
                // Return true to indicate that the value was caught and should be removed from the store
                true
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Home Screen",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Current result: ${result ?: "No result yet"}")

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Test Cases:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Navigate to intermediate screen
                Button(onClick = { navController.navigate("intermediate") }) {
                    Text(text = "Navigate to Intermediate Screen")
                }

                // Clear the result
                Button(onClick = { result = null }) {
                    Text(text = "Clear Result")
                }
            }
        }

        composable("intermediate") {
            val store = LocalBoomerangStore.current
            // State to hold the proxied/consumed value
            var proxiedValue by rememberSaveable { mutableStateOf<String?>(null) }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Intermediate Screen",
                    style = MaterialTheme.typography.headlineMedium
                )

                // Display the proxied/consumed value
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Proxied value: ${proxiedValue ?: "No value"}")

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Test Cases:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // TEST CASE 1: Continue navigation to result screen
                Button(onClick = { navController.navigate("result") }) {
                    Text(text = "Continue to Result Screen")
                }

                // TEST CASE 3: Test value can be dropped
                Button(onClick = {
                    // Explicitly drop the value from the store
                    store.dropValue("home")
                }) {
                    Text(text = "Drop Home Result")
                }

                // TEST CASE 2: Test navigation result is persisted correctly across configuration changes
                Button(onClick = recreateApp) {
                    Text(text = "Recreate Activity (Config Change)")
                }

                // TEST CASE 4: Test value can be proxied by another catcher in between
                Button(onClick = {
                    // Immediately catch and consume the value
                    store.tryConsumeValue("home") { bundle ->
                        // Get the value for display or processing in this screen
                        val consumedValue = bundle.getString("result")
                        // Store the consumed value in the state variable for display
                        proxiedValue = consumedValue
                        // Return true to remove the value so the home screen won't get it
                        true
                    }
                }) {
                    Text(text = "Consume Value")
                }

                // Button to clear the proxied value
                Button(onClick = {
                    proxiedValue = null
                }) {
                    Text(text = "Clear Proxied Value")
                }
            }
        }

        composable("result") {
            val store = LocalBoomerangStore.current

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Result Screen",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // TEST CASE 1: Test navigation result is passed correctly
                Button(onClick = {
                    // Store a result value with the key "home"
                    store.storeValue("home", boomerangOf("result" to "Result from result screen"))
                    // Navigate back to home
                    navController.popBackStack()
                }) {
                    Text(text = "Store Result and Return")
                }
            }
        }
    }
}
