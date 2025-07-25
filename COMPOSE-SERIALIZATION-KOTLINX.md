# Module compose-serialization-kotlinx

A lightweight multiplatform library for handling serializable objects in Compose applications using Kotlinx Serialization.

## Overview

The Compose Serialization Kotlinx module of Boomerang provides integration between the Compose module and Kotlinx Serialization, allowing you to pass serializable objects between screens in Compose navigation without tight coupling between components. It simplifies the process of catching and processing serializable objects in Compose UI.

This module supports Android, iOS, and Desktop platforms, providing a consistent API across all platforms while using platform-specific implementations under the hood.

**Note:** The serialization feature currently only supports flat non-nested objects.

## Installation

Add the following dependencies to your app's `build.gradle.kts` file:

```kotlin
// For core functionality (required)
implementation("io.github.buszi.boomerang:core:1.3.0")

// For Jetpack Compose integration
implementation("io.github.buszi.boomerang:compose:1.3.0")

// For Kotlinx Serialization integration
implementation("io.github.buszi.boomerang:serialization-kotlinx:1.3.0")

// For Compose with Kotlinx Serialization integration
implementation("io.github.buszi.boomerang:compose-serialization-kotlinx:1.3.0")

// Kotlinx Serialization dependency
implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")
```

## Usage

### Defining a Serializable Object

First, define a serializable object using Kotlinx Serialization:

```kotlin
@Serializable
data class UserPreference(
    val theme: String,
    val notificationsEnabled: Boolean,
    val fontSize: Int
)
```

### Catching a Serializable Object

To catch and process a serializable object when a Compose screen becomes visible:

```kotlin
@Composable
fun PreferencesScreen() {
    var userPreference by remember { mutableStateOf<UserPreference?>(null) }

    // Set up a catcher that runs when the screen starts
    CatchSerializableLifecycleEffect<UserPreference>("user_preferences") { preference ->
        // Process the caught serializable object
        userPreference = preference
        true // Return true to indicate the object was successfully processed
    }

    // Display the user preference
    userPreference?.let {
        Column {
            Text("Theme: ${it.theme}")
            Text("Notifications: ${it.notificationsEnabled}")
            Text("Font Size: ${it.fontSize}")
        }
    }
}
```

### Using Type as Key

You can also use the type of the serializable object as the key:

```kotlin
@Composable
fun PreferencesScreen() {
    var userPreference by remember { mutableStateOf<UserPreference?>(null) }

    // Set up a catcher that uses the type as the key
    CatchSerializableLifecycleEffect<UserPreference> { preference ->
        // Process the caught serializable object
        userPreference = preference
        true // Return true to indicate the object was successfully processed
    }

    // Display the user preference
    userPreference?.let {
        Column {
            Text("Theme: ${it.theme}")
            Text("Notifications: ${it.notificationsEnabled}")
            Text("Font Size: ${it.fontSize}")
        }
    }
}
```

### Storing a Serializable Object

To store a serializable object that can be caught by another screen:

```kotlin
@Composable
fun SettingsScreen(navController: NavController) {
    val store = LocalBoomerangStore.current

    Button(onClick = {
        // Create a serializable object
        val userPreference = UserPreference(
            theme = "dark",
            notificationsEnabled = true,
            fontSize = 14
        )

        // Store the object with a specific key
        store.storeValue("user_preferences", userPreference)

        // Or store the object using its type as the key
        store.storeValue(userPreference)

        // Navigate back
        navController.popBackStack()
    }) {
        Text("Save Preferences")
    }
}
```

## Key Components

### CatchSerializableLifecycleEffect

```kotlin
@Composable
inline fun <reified T : @Serializable Any> CatchSerializableLifecycleEffect(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Boolean
)
```

A Composable effect that catches serializable objects of type `T` at a specific lifecycle event. This function uses Kotlinx Serialization to deserialize the caught object before passing it to the catcher function. The catcher function returns a boolean indicating whether the object was successfully handled.

### ConsumeSerializableLifecycleEffect

```kotlin
@Composable
inline fun <reified T : @Serializable Any> ConsumeSerializableLifecycleEffect(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Unit
)
```

A Composable effect that consumes serializable objects of type `T` at a specific lifecycle event. This function is similar to `CatchSerializableLifecycleEffect` but automatically returns true after the catcher function is called, indicating that the object was successfully handled.

### Type-Based Variants

Both `CatchSerializableLifecycleEffect` and `ConsumeSerializableLifecycleEffect` have overloads that automatically use the qualified name of type `T` as the key:

```kotlin
@Composable
inline fun <reified T : @Serializable Any> CatchSerializableLifecycleEffect(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Boolean
)

@Composable
inline fun <reified T : @Serializable Any> ConsumeSerializableLifecycleEffect(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Unit
)
```

## Requirements

- Kotlin 1.5.0+
- Compose Multiplatform 1.0.0+
- Kotlinx Serialization 1.5.0+

### Platform-specific requirements:
- **Android**: API level 21+
- **iOS**: iOS 14+
- **Desktop**: JVM 11+

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