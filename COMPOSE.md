# Module compose

A lightweight multiplatform library for handling navigation results in Compose applications.

## Overview

The Compose module of Boomerang provides integration with Compose Multiplatform, allowing you to pass data between screens in Compose navigation without tight coupling between components. It solves the common problem of returning results from one screen to another, similar to the old `setFragmentResultListener` pattern but designed specifically for modern Compose navigation patterns.

This module supports Android, iOS, and Desktop platforms, providing a consistent API across all platforms while using platform-specific implementations under the hood.

## Installation

Add the following dependencies to your app's `build.gradle.kts` file:

```kotlin
// For core functionality (required)
implementation("io.github.buszi.boomerang:core:1.2.0")

// For Jetpack Compose integration
implementation("io.github.buszi.boomerang:compose:1.2.0")
```

## Setup

There are two ways to set up Boomerang in a Compose application:

### 1. Pure Compose Setup

For applications that use only Jetpack Compose for navigation, wrap your app's content in a `CompositionHostedDefaultBoomerangStoreScope`:

```kotlin
@Composable
fun YourApplication() {
    CompositionHostedDefaultBoomerangStoreScope {
        // Your app content here
        AppNavigation()
    }
}
```

This creates a `DefaultBoomerangStore` and provides it to your composition. The store's state is automatically preserved across configuration changes.

### 2. Mixed Setup (Compose + Fragment)

For applications that use both Compose and Fragments, make your Activity implement `BoomerangStoreHost` and use `ActivityHostedBoomerangStoreScope`:

```kotlin
class MainActivity : AppCompatActivity(), BoomerangStoreHost {

    override var boomerangStore: BoomerangStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createOrRestoreDefaultBoomerangStore(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveDefaultBoomerangStoreState(outState)
    }
}
```

Then in your Compose code:

```kotlin
@Composable
fun YourComposeUI() {
    ActivityHostedBoomerangStoreScope {
        // Your Compose content here
    }
}
```

This retrieves the `BoomerangStore` from the hosting Activity and provides it to your composition.

## Usage

### Storing a Result

When you want to store a result to be consumed by another screen:

```kotlin
@Composable
fun DetailScreen(navController: NavController) {
    val store = LocalBoomerangStore.current

    Button(onClick = {
        // Store the result with a key using the builder pattern
        store.storeValue("home_screen_result") {
            putString("selectedItem", "Item 1")
        }

        // Navigate back
        navController.popBackStack()
    }) {
        Text("Select and Return")
    }
}
```

### Catching a Result

To catch and process a result when a Compose screen becomes visible:

```kotlin
@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    // Set up a catcher that runs when the screen starts
    CatchBoomerangLifecycleEffect("home_screen_result") { boomerang ->
        // Extract data from the boomerang
        selectedItem = boomerang.getString("selectedItem")
        true // Return true to indicate the result was successfully processed
    }

    // Display the result
    Text("Selected item: $selectedItem")
}
```

By default, `CatchBoomerangLifecycleEffect` triggers on the `ON_START` lifecycle event, but you can specify `ON_RESUME` if needed.

## Key Components

### LocalBoomerangStore

A CompositionLocal that provides access to the current `BoomerangStore`:

```kotlin
// Inside a @Composable function
val store = LocalBoomerangStore.current

// Check if a result exists
val hasResult = store.getValue("some_key") != null

// Manually drop a value
store.dropValue("some_key")
```

### CompositionHostedDefaultBoomerangStoreScope

A Composable function that creates a `DefaultBoomerangStore` and provides it to its content. The store is saved and restored across configuration changes using `rememberSaveable` with a custom saver.

### ActivityHostedBoomerangStoreScope

A Composable function that retrieves a `BoomerangStore` from an Activity that implements `BoomerangStoreHost` and provides it to its content. This is used in mixed environments where both Compose and Fragments are used.

### CatchBoomerangLifecycleEffect

A Composable effect that tries to catch a boomerang value from the `BoomerangStore` when a specific lifecycle event occurs. This is typically used to process data that was stored in the `BoomerangStore` when the UI becomes visible.

### rememberBoomerangStore

A Composable function that remembers a BoomerangStore across recompositions and configuration changes. This function is implemented differently on each platform:

- On Android, it uses `rememberSaveable` with a custom saver to preserve the store's state across configuration changes
- On iOS and Desktop, it uses `remember` to keep the store instance across recompositions

This ensures proper state saving and restoration on each platform while providing a consistent API.

## Advanced Usage

### Custom Lifecycle Events

You can specify when to catch results by providing a different lifecycle event:

```kotlin
CatchBoomerangLifecycleEffect(
    key = "home_screen_result",
    lifecycleEvent = Lifecycle.Event.ON_RESUME
) { boomerang ->
    // Process the result
    true
}
```

### Manual Catching

You can manually catch results without using the lifecycle effect:

```kotlin
@Composable
fun ManualCatchExample() {
    val store = LocalBoomerangStore.current

    Button(onClick = {
        store.tryCatch("some_key") { boomerang ->
            // Process the result
            true
        }
    }) {
        Text("Check for Result")
    }
}
```

## Requirements

- Kotlin 1.5.0+
- Compose Multiplatform 1.0.0+

### Platform-specific requirements:
- **Android**: API level 21+
- **iOS**: iOS 14+
- **Desktop**: JVM 11+

## Sample App

The repository includes a sample app that demonstrates how to use Boomerang in a real-world scenario. Check the `app` module for complete examples of both pure Compose navigation and mixed solutions where both Compose and Fragments are used.

## License

```
Copyright 2025 Buszi

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
