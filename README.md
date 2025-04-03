# Boomerang

A lightweight library for handling navigation results in Jetpack Compose applications.

## Overview

Boomerang provides a clean and efficient way to pass data between screens in Jetpack Compose navigation without tight coupling between components. It solves the common problem of returning results from one screen to another, similar to the old `startActivityForResult` pattern but designed specifically for modern Compose navigation.

The library consists of two main modules:
- **Core**: Contains the fundamental concepts and interfaces
- **Compose**: Provides Jetpack Compose integration

## Features

- 🔄 Pass data between screens without tight coupling
- 💾 Preserve navigation results across configuration changes and process death
- 🧩 Modular design with separate core and compose modules
- 🔌 Easy integration with any Jetpack Compose navigation library
- 🧪 Lightweight with minimal dependencies

## Installation

Add the following dependencies to your app's `build.gradle.kts` file:

```kotlin
// For core functionality only
implementation("io.github.buszi.boomerang:core:1.0.0")

// For Jetpack Compose integration
implementation("io.github.buszi.boomerang:compose:1.0.0")
```

## Usage

### Basic Setup

1. Wrap your app's content in a `DefaultBoomerangStoreScope`:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultBoomerangStoreScope {
                // Your app content here
                AppNavigation()
            }
        }
    }
}
```

### Storing a Result

When you want to store a result to be consumed by another screen:

```kotlin
@Composable
fun DetailScreen(navController: NavController) {
    val store = LocalBoomerangStore.current

    Button(onClick = {
        // Create a bundle with your result data
        val resultBundle = bundleOf("selectedItem" to "Item 1")

        // Store the result with a key
        store.storeValue("home_screen_result", resultBundle)

        // Navigate back
        navController.popBackStack()
    }) {
        Text("Select and Return")
    }
}
```

### Catching a Result

To catch and process a result when a screen becomes visible:

```kotlin
@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    // Set up a catcher that runs when the screen starts
    CatchBoomerangLifecycleEffect("home_screen_result") { bundle ->
        // Extract data from the bundle
        selectedItem = bundle.getString("selectedItem")
        true // Return true to indicate the result was successfully processed
    }

    // Display the result
    Text("Selected item: $selectedItem")
}
```

### Advanced Usage

You can directly access the store to perform operations:

```kotlin
// Inside a @Composable function
val store = LocalBoomerangStore.current

// Check if a result exists
val hasResult = store.getValue("some_key") != null

// Manually drop a value
store.dropValue("some_key")
```

## How It Works

Boomerang uses a simple but effective pattern:

1. **Store**: A central repository that holds navigation results as key-value pairs
2. **Catcher**: A functional interface that processes results when they become available
3. **Lifecycle Integration**: Results are caught when a screen becomes visible

The library decouples the component that produces a result from the component that consumes it, allowing for a more flexible and maintainable navigation flow.

### Core Components

- **BoomerangStore**: Interface for storing and retrieving navigation results
- **BoomerangCatcher**: Functional interface for processing navigation results
- **DefaultBoomerangStore**: Default implementation of BoomerangStore using a MutableMap

### Compose Components

- **LocalBoomerangStore**: CompositionLocal for accessing the BoomerangStore
- **DefaultBoomerangStoreScope**: Composable function that provides a default BoomerangStore
- **CatchBoomerangLifecycleEffect**: Composable function that catches results at specific lifecycle events

## Requirements

- Android API level 21+
- Jetpack Compose 1.0.0+
- Kotlin 1.5.0+

## Sample App

The repository includes a sample app that demonstrates how to use Boomerang in a real-world scenario. Check the `app` module for a complete example.

## License

```
Copyright 2023 Buszi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
