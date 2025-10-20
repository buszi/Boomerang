# Boomerang

[![UI Tests](https://github.com/buszi/Boomerang/actions/workflows/android-ui-tests.yml/badge.svg)](https://github.com/buszi/Boomerang/actions/workflows/android-ui-tests.yml)
[![Version](https://img.shields.io/maven-central/v/io.github.buszi.boomerang/core)](https://central.sonatype.com/artifact/io.github.buszi.boomerang/core)

A lightweight library for handling navigation results in Jetpack Compose/Compose Multiplatform and AndroidX Fragment applications targeting Android, iOS and Desktop/JVM.

## Overview

Boomerang provides a clean and efficient way
to pass data between screens in Jetpack Compose and AndroidX Fragment navigation without tight coupling between components.
It solves the common problem of returning results from one screen to another,
similar to the `setFragmentResultListener` pattern but designed to be lightweight and flexible.

The library consists of several modules:
- **Core**: Contains the fundamental concepts and interfaces
- **Compose**: Provides Jetpack Compose integration
- **Fragment**: Provides AndroidX Fragment integration (Android only)
- **Serialization-Kotlinx** (since 1.4.0): Kotlinx Serialization integration. Since last release: supports nested objects and lists; configurable via `BoomerangFormat` and `SerializersModule`.
- **Compose-Serialization-Kotlinx** (since 1.4.0): Kotlinx Serialization integration for Compose.
- **Fragment-Serialization-Kotlinx** (since 1.4.0): Kotlinx Serialization integration for Fragments.

## Features

- 🔄 Pass data between screens without tight coupling
- 💾 Preserve navigation results across configuration changes and process death
- 🧩 Modular design with separate core, compose, and fragment modules
- 🔌 Easy integration with any Jetpack Compose or AndroidX Fragment navigation library
- 🔀 Support for mixed projects using both Compose and Fragments
- 📢 Simple event handling for notifications without data
- 🧪 Lightweight with minimal dependencies

## Installation

Add the following dependencies to your `build.gradle.kts` file:

```kotlin
// For core functionality only
implementation("io.github.buszi.boomerang:core:1.5.0")

// For Jetpack Compose integration
implementation("io.github.buszi.boomerang:compose:1.5.0")

// For AndroidX Fragment integration (Android only)
implementation("io.github.buszi.boomerang:fragment:1.5.0")

// For Kotlinx Serialization integration (Experimental)
implementation("io.github.buszi.boomerang:serialization-kotlinx:1.5.0")

// For Compose with Kotlinx Serialization integration
implementation("io.github.buszi.boomerang:compose-serialization-kotlinx:1.5.0")

// For Fragment with Kotlinx Serialization integration (Android only)
implementation("io.github.buszi.boomerang:fragment-serialization-kotlinx:1.5.0")
```

Choose the modules that fit your project's needs. For example, if you're only using Fragments, you only need the core and fragment modules. If you want to use serialization features, you'll need the appropriate serialization module along with Kotlinx Serialization dependency.

## Usage

### Compose Setup

1. For the default and recommended behavior of Boomerang, wrap your app's content in a `CompositionHostedDefaultBoomerangStoreScope`:

```kotlin
@Composable
fun YourApplication() {
    CompositionHostedDefaultBoomerangStoreScope {
        // Your app content here
        AppNavigation()
    }
}
```

### Fragment Setup

1. Make your Activity implement `BoomerangStoreHost` and initialize the store:

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

### Storing a Result

When you want to store a result to be consumed by another screen:

```kotlin
@Composable
fun DetailScreen(navController: NavController) {
    val store = LocalBoomerangStore.current

    Button(onClick = {
        // Store the result with a key
        store.storeValue("home_screen_result", boomerangOf("selectedItem" to "Item 1"))

        // Navigate back
        navController.popBackStack()
    }) {
        Text("Select and Return")
    }
}
```

### Catching a Result in Compose

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

### Catching a Result in Fragments

To catch and process a result when a Fragment becomes visible:

```kotlin
class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up a catcher that runs when the fragment starts
        catchBoomerangWithLifecycleEvent("home_screen_result") { boomerang ->
            // Extract data from the boomerang and process it
            val selectedItem = boomerang.getString("selectedItem")
            // Do something with the result
            true // Return true to indicate the result was successfully processed
        }
    }
}
```

### Event Handling

For simple notifications without data, you can use the event handling feature:

#### Storing an Event

```kotlin
// In Compose
val store = LocalBoomerangStore.current
store.storeEvent("notification_event")

// In Fragment
findBoomerangStore().storeEvent("notification_event")
```

#### Catching an Event in Compose

```kotlin
@Composable
fun NotificationScreen() {
    var eventReceived by remember { mutableStateOf(false) }

    // Set up an event catcher that runs when the screen starts
    CatchEventBoomerangLifecycleEffect("notification_event") {
        // Update state when the event is received
        eventReceived = true
    }

    // Display the event status
    Text("Event status: ${if (eventReceived) "Event received" else "No event received"}")
}
```

#### Catching an Event in Fragments

```kotlin
class NotificationFragment : Fragment() {

    private var eventReceived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up an event catcher that runs when the fragment starts
        catchEventBoomerangWithLifecycleEvent("notification_event") {
            // Update state when the event is received
            eventReceived = true
            updateUI()
        }
    }

    private fun updateUI() {
        // Update UI based on event status
    }
}
```

### Serialization (Experimental)

Boomerang provides integration with Kotlinx Serialization, allowing you to store and retrieve serializable objects.
Supports objects, nested objects, collections, and enums.

#### Defining a Serializable Object

```kotlin
@Serializable
data class UserPreference(
    val theme: String,
    val notificationsEnabled: Boolean,
    val fontSize: Int
)
```

#### Storing a Serializable Object

```kotlin
// In Compose
val store = LocalBoomerangStore.current
val userPreference = UserPreference("dark", true, 14)
store.storeValue("user_preferences", userPreference)

// Or store using the type as the key
store.storeValue(userPreference)

// In Fragment
val store = findBoomerangStore()
val userPreference = UserPreference("dark", true, 14)
store.storeValue("user_preferences", userPreference)
```

#### Retrieving a Serializable Object

```kotlin
// In Compose
val store = LocalBoomerangStore.current
val userPreference: UserPreference? = store.getSerializable("user_preferences")

// Or retrieve using the type as the key
val userPreference: UserPreference? = store.getSerializable()

// In Fragment
val store = findBoomerangStore()
val userPreference: UserPreference? = store.getSerializable("user_preferences")
```

#### Catching a Serializable Object in Compose

```kotlin
@Composable
fun PreferencesScreen() {
    var userPreference by remember { mutableStateOf<UserPreference?>(null) }

    // Catch a serializable object with a specific key
    CatchSerializableLifecycleEffect<UserPreference>("user_preferences") { preference ->
        userPreference = preference
        true // Return true to indicate the object was successfully processed
    }

    // Or catch using the type as the key
    CatchSerializableLifecycleEffect<UserPreference> { preference ->
        userPreference = preference
        true
    }
}
```

#### Catching a Serializable Object in Fragment

```kotlin
class PreferencesFragment : Fragment() {
    private var userPreference: UserPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Catch a serializable object with a specific key
        catchSerializableWithLifecycleEvent<UserPreference>("user_preferences") { preference ->
            userPreference = preference
            true // Return true to indicate the object was successfully processed
        }

        // Or catch using the type as the key
        catchSerializableWithLifecycleEvent<UserPreference> { preference ->
            userPreference = preference
            true
        }
    }
}
```

### Advanced Usage

#### In Compose

You can directly access the store to perform operations in Compose:

```kotlin
// Inside a @Composable function
val store = LocalBoomerangStore.current

// Check if a result exists
val hasResult = store.getValue("some_key") != null

// Manually drop a value
store.dropValue("some_key")
```

#### In Fragments

You can directly access the store to perform operations in Fragments:

```kotlin
// Inside a Fragment
val store = findBoomerangStore()

// Check if a result exists
val hasResult = store.getValue("some_key") != null

// Manually drop a value
store.dropValue("some_key")

// Store a value
store.storeValue("some_key", boomerangOf("result" to "Success"))
```

#### Logging

Boomerang provides optional logging capabilities to help with debugging:

```kotlin
// Enable logging with Android's LogCat (Android only)
BoomerangConfig.logger = AndroidBoomerangLogger(LogLevel.DEBUG)

// For other platforms or simple console logging
BoomerangConfig.logger = BoomerangLogger.PRINT_LOGGER

// Disable logging
BoomerangConfig.logger = null

// You can also implement BoomerangLogger interface for your own loggers
class MyBoomerangLogger : BoomerangLogger
```

When logging is enabled, Boomerang will log operations like storing and retrieving values, which can be helpful for debugging navigation flows.

## How It Works

Boomerang uses a simple but effective pattern:

1. **Store**: A central repository that holds navigation results as key-value pairs
2. **Catcher**: A functional interface that processes results when they become available
3. **Lifecycle Integration**: Results are caught when a screen becomes visible
4. **State Restoration**: Results are saved and restored during configuration changes and process death

The library decouples the component that produces a result from the component that consumes it, allowing for a more flexible and maintainable navigation flow.
The solution does not depend on any specific navigation library, and it can be used with any Jetpack Compose or AndroidX Fragment navigation library.
The library does not store reference to the producer/consumer components, so it is completely memory-leak-free.
The only requirement is that the producer and consumer components are not part of the same lifecycle (you cannot pass a result from one part of the screen to another on the same screen, for such cases use f.e. ViewModel).

### Core Components

- **BoomerangStore**: Interface for storing and retrieving navigation results
- **BoomerangCatcher**: Functional interface for processing navigation results
- **DefaultBoomerangStore**: Default implementation of BoomerangStore using a MutableMap
- **BoomerangStoreHost**: Interface for components that host a BoomerangStore (only for Fragment and mixed setup)
- **eventBoomerangCatcher**: Function that creates a BoomerangCatcher specifically for handling event notifications

### Compose Components

- **LocalBoomerangStore**: CompositionLocal for accessing the BoomerangStore
- **CompositionHostedBoomerangStoreScope**: Composable function that provides your custom implementation of BoomerangStore
- **CompositionHostedDefaultBoomerangStoreScope**: Composable function that provides a default BoomerangStore
- **CatchBoomerangLifecycleEffect**: Composable function that catches results at specific lifecycle events
- **CatchEventBoomerangLifecycleEffect**: Specialized Composable function for catching event notifications

### Fragment Components

- **catchBoomerangWithLifecycleEvent**: Extension function for Fragment to catch results at specific lifecycle events
- **catchEventBoomerangWithLifecycleEvent**: Extension function for Fragment to catch event notifications
- **findBoomerangStore**: Extension function for Fragment to find the BoomerangStore from the hosting Activity
- **createOrRestoreDefaultBoomerangStore**: Extension function for BoomerangStoreHost to create or restore a DefaultBoomerangStore
- **saveDefaultBoomerangStoreState**: Extension function for BoomerangStoreHost to save the state of a DefaultBoomerangStore

### Mixed Components

- **ActivityHostedBoomerangStoreScope**: Composable function that provides BoomerangStore hosted by activity with BoomerangStoreHost

### Serialization Components (since 1.4.0)

- **BoomerangFormat**: Class that provides methods for serializing and deserializing objects
- **kotlinxSerializationBoomerangCatcher**: Function that creates a BoomerangCatcher for serializable objects
- **storeValue/getSerializable**: Extension functions for BoomerangStore to store and retrieve serializable objects
- **putSerializable/getSerializable**: Extension functions for Boomerang to add and retrieve serializable objects

### Compose Serialization Components (since 1.4.0)

- **CatchSerializableLifecycleEffect**: Composable function that catches serializable objects at specific lifecycle events
- **ConsumeSerializableLifecycleEffect**: Specialized Composable function for consuming serializable objects

### Fragment Serialization Components (since 1.4.0)

- **catchSerializableWithLifecycleEvent**: Extension function for Fragment to catch serializable objects at specific lifecycle events
- **consumeSerializableWithLifecycleEvent**: Extension function for Fragment to consume serializable objects

## Requirements

- Android API level 21+
- Kotlin 1.5.0+
- For Compose module: Jetpack Compose 1.0.0+
- For Fragment module: AndroidX Fragment 1.3.0+
- For Serialization modules: Kotlinx Serialization 1.5.0+

## Kotlin/Compose Multiplatform

The library now supports Kotlin/Compose Multiplatform! You can use Boomerang on Android, iOS, and Desktop platforms.

- **Android**: Full support with all features (Compose and Fragment)
- **iOS**: Support for core functionality and Compose Multiplatform integration
- **Desktop**: Support for core functionality and Compose integration

The multiplatform implementation uses platform-specific storage mechanisms:
- On Android, it uses Android's Bundle for storage
- On iOS and Desktop, it uses a MutableMap for storage

This ensures optimal performance and integration with each platform while maintaining a consistent API across all platforms.

#### Wasm/JS support coming soon!

## Sample App

The repository includes a sample app that demonstrates how to use Boomerang in a real-world scenario. The app includes examples of:

- Compose navigation with Boomerang (Android, Desktop, and iOS)
- Fragment navigation with Boomerang (Android only)

Check the `app` module for complete examples of all these scenarios.

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
