# Module serialization-kotlinx

A lightweight multiplatform library for serializing and deserializing objects with Kotlinx Serialization in Boomerang applications.

## Overview

The Serialization Kotlinx module of Boomerang provides integration with Kotlinx Serialization, allowing you to store and retrieve serializable objects in the Boomerang store. This simplifies the process of passing complex data between screens in your application.

This module supports Android, iOS, and Desktop platforms, providing a consistent API across all platforms while using platform-specific implementations under the hood.

**Note:** The serialization feature currently only supports flat non-nested objects.

## Installation

Add the following dependencies to your app's `build.gradle.kts` file:

```kotlin
// For core functionality (required)
implementation("io.github.buszi.boomerang:core:1.4.0")

// For Kotlinx Serialization integration
implementation("io.github.buszi.boomerang:serialization-kotlinx:1.4.0")

// Kotlinx Serialization dependency
implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.0")
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

### Storing a Serializable Object

When you want to store a serializable object:

```kotlin
// Get the store
val store = findBoomerangStore() // or LocalBoomerangStore.current in Compose

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
```

### Retrieving a Serializable Object

To retrieve and deserialize an object:

```kotlin
// Get the store
val store = findBoomerangStore() // or LocalBoomerangStore.current in Compose

// Retrieve the object with a specific key
val userPreference: UserPreference? = store.getSerializable("user_preferences")

// Or retrieve the object using its type as the key
val userPreference: UserPreference? = store.getSerializable()
```

### Adding a Serializable Object to a Boomerang

You can also add a serializable object to an existing Boomerang:

```kotlin
// Create a boomerang
val boomerang = emptyBoomerang()

// Add a serializable object to the boomerang
boomerang.putSerializable("user_preferences", userPreference)
```

### Retrieving a Serializable Object from a Boomerang

To retrieve a serializable object from a Boomerang:

```kotlin
// Get the serializable object from the boomerang
val userPreference: UserPreference? = boomerang.getSerializable("user_preferences")
```

### Creating a Serialization Boomerang Catcher

To create a catcher that automatically deserializes objects:

```kotlin
// Create a catcher for a specific type
val catcher = kotlinxSerializationBoomerangCatcher<UserPreference> { userPreference ->
    // Process the deserialized object
    println("Theme: ${userPreference.theme}")
    println("Notifications: ${userPreference.notificationsEnabled}")
    println("Font Size: ${userPreference.fontSize}")
    
    true // Return true to indicate the object was successfully processed
}

// Use the catcher
store.tryConsumeValue("user_preferences", catcher)
```

## Key Components

### Extension Functions for BoomerangStore

#### storeValue

```kotlin
inline fun <reified T : @Serializable Any> BoomerangStore.storeValue(key: String, value: T)
inline fun <reified T : @Serializable Any> BoomerangStore.storeValue(value: T)
```

These extension functions store a serializable object in the BoomerangStore. The first version uses a specified key, while the second version automatically uses the qualified name of the type as the key.

#### getSerializable

```kotlin
inline fun <reified T : @Serializable Any> BoomerangStore.getSerializable(key: String): T?
inline fun <reified T : @Serializable Any> BoomerangStore.getSerializable(): T?
```

These extension functions retrieve and deserialize an object from the BoomerangStore. The first version uses a specified key, while the second version automatically uses the qualified name of the type as the key.

### Extension Functions for Boomerang

#### putSerializable

```kotlin
inline fun <reified T : @Serializable Any> Boomerang.putSerializable(key: String, value: T)
```

This extension function adds a serializable object to a Boomerang with the specified key.

#### getSerializable

```kotlin
inline fun <reified T : @Serializable Any> Boomerang.getSerializable(key: String): T?
```

This extension function retrieves and deserializes an object from a Boomerang with the specified key.

### BoomerangFormat

```kotlin
open class BoomerangFormat {
    inline fun <reified T : Any> serialize(value: T): Boomerang
    inline fun <reified T : Any> deserialize(boomerang: Boomerang): T
}
```

The `BoomerangFormat` class provides methods for serializing objects to Boomerang instances and deserializing Boomerang instances back to objects.

### kotlinxSerializationBoomerangCatcher

```kotlin
inline fun <reified T : @Serializable Any> kotlinxSerializationBoomerangCatcher(
    crossinline catcher: (T) -> Boolean
): BoomerangCatcher
```

This function creates a `BoomerangCatcher` that deserializes the caught boomerang into an object of type `T` using Kotlinx Serialization.

## Requirements

- Kotlin 1.5.0+
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