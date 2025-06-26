# Module core

A lightweight multiplatform library providing the fundamental concepts and interfaces for handling navigation results in applications.

## Overview

The Core module of Boomerang contains the essential components that power both the Compose and Fragment modules. It defines the key interfaces and classes for storing, retrieving, and processing navigation results between screens without tight coupling between components.

This module supports Android, iOS, and Desktop platforms, providing a consistent API across all platforms while using platform-specific implementations under the hood.

## Installation

Add the following dependency to your app's `build.gradle.kts` file:

```kotlin
// For core functionality (required by all Boomerang modules)
implementation("io.github.buszi.boomerang:core:1.2.0")
```

The Core module is required by both the Compose and Fragment modules, so you'll need to include it regardless of which integration you're using.

## Key Components

### BoomerangStore

The `BoomerangStore` interface defines a key-value store for navigation results:

```kotlin
interface BoomerangStore {
    fun getValue(key: String): Boomerang?
    fun storeValue(key: String, value: Boomerang)
    fun storeEvent(key: String)
    fun dropValue(key: String)
    fun tryConsumeValue(key: String, catcher: BoomerangCatcher)
    fun packState(): Boomerang
    fun restoreState(boomerang: Boomerang)
}
```

This interface provides methods for:
- Retrieving a value for a key (`getValue`)
- Storing a value with a key (`storeValue`)
- Storing an event notification with a key (`storeEvent`)
- Removing a value for a key (`dropValue`)
- Trying to catch a value using a `BoomerangCatcher` (`tryConsumeValue`)
- Packing the store's state into a Boomerang object (`packState`)
- Restoring the store's state from a Boomerang object (`restoreState`)

### BoomerangCatcher

The `BoomerangCatcher` functional interface is used for catching and processing Boomerang values:

```kotlin
fun interface BoomerangCatcher {
    fun tryCatch(value: Boomerang): Boolean
}
```

Implementations of this interface determine whether a Boomerang value should be "caught" (processed and removed from the store).

### EventBoomerangCatcher

The `eventBoomerangCatcher` function creates a specialized `BoomerangCatcher` for handling event notifications:

```kotlin
inline fun eventBoomerangCatcher(
    key: String,
    crossinline onEvent: () -> Unit,
): BoomerangCatcher
```

This function is useful when you only need to be notified that something happened, without needing to pass any additional data. It checks if the Boomerang contains an event with the specified key and calls the provided callback when the event is caught.

### DefaultBoomerangStore

The `DefaultBoomerangStore` class is the default implementation of the `BoomerangStore` interface:

```kotlin
class DefaultBoomerangStore : BoomerangStore {
    // Implementation of BoomerangStore methods
}
```

The `DefaultBoomerangStore` uses a private mutable map to store key-value pairs and provides methods for packing and restoring its state. It's designed to work across all supported platforms (Android, iOS, Desktop).

### BoomerangStoreHost

The `BoomerangStoreHost` interface is for components that host a `BoomerangStore`:

```kotlin
interface BoomerangStoreHost {
    var boomerangStore: BoomerangStore?
}
```

This interface is typically implemented by Activities or other lifecycle-aware components that need to provide a `BoomerangStore` to their children.

### Logging Components

Boomerang provides optional logging capabilities to help with debugging:

#### BoomerangLogger

The `BoomerangLogger` interface defines a simple logging mechanism:

```kotlin
interface BoomerangLogger {
    fun log(tag: String, message: String)
}
```

This interface can be implemented by applications to integrate with their preferred logging system.

#### BoomerangConfig

The `BoomerangConfig` object provides global configuration options:

```kotlin
object BoomerangConfig {
    var logger: BoomerangLogger? = null
}
```

By default, `logger` is `null`, which means logging is disabled. To enable logging, set this property to an instance of `BoomerangLogger`.

#### AndroidBoomerangLogger

For Android applications, Boomerang provides an Android-specific logger that integrates with Android's Log utility:

```kotlin
class AndroidBoomerangLogger(
    private val level: LogLevel = LogLevel.DEBUG
) : BoomerangLogger
```

The `LogLevel` enum defines the available log levels (VERBOSE, DEBUG, INFO, WARN, ERROR).

## Usage

The Core module is not typically used directly in your application code. Instead, you'll use either the Compose or Fragment modules, which provide integration with their respective UI frameworks.

However, if you're building a custom integration or need direct access to the core functionality, you can use the Core module as follows:

### Creating a Store

```kotlin
// Create a new DefaultBoomerangStore
val store = DefaultBoomerangStore()
```

### Storing a Value

```kotlin
// Store the result with a key using the builder pattern
store.storeValue("home_screen_result") {
    putString("selectedItem", "Item 1")
    putInt("quantity", 5)
}
```

### Retrieving a Value

```kotlin
// Get a value for a key
val boomerang = store.getValue("home_screen_result")

// Extract data from the boomerang
val selectedItem = boomerang?.getString("selectedItem")
val quantity = boomerang?.getInt("quantity")
```

### Catching a Value

```kotlin
// Try to catch a value using a BoomerangCatcher
store.tryConsumeValue("home_screen_result") { boomerang ->
    // Process the boomerang
    val selectedItem = boomerang.getString("selectedItem")
    val quantity = boomerang.getInt("quantity")

    // Return true to indicate the value was successfully caught and should be removed
    true
}
```

### Storing and Catching Events

```kotlin
// Store an event notification
store.storeEvent("notification_event")

// Create an event catcher
val eventCatcher = eventBoomerangCatcher("notification_event") {
    // This callback is executed when the event is caught
    println("Event received!")
}

// Try to catch the event
store.tryConsumeValue("notification_event", eventCatcher)
```

### Saving and Restoring State

```kotlin
// Pack the store's state into a Boomerang object
val stateBoomerang = store.packState()

// Later, restore the store from the saved state
val restoredStore = DefaultBoomerangStore().apply {
    restoreState(stateBoomerang)
}
```

### Configuring Logging

```kotlin
// For Android applications
BoomerangConfig.logger = AndroidBoomerangLogger(LogLevel.DEBUG)

// For other platforms or simple console logging
BoomerangConfig.logger = BoomerangLogger.PRINT_LOGGER

// Create a custom logger
val customLogger = object : BoomerangLogger {
    override fun log(tag: String, message: String) {
        // Integrate with your preferred logging system
        YourLoggingSystem.log("$tag: $message")
    }
}
BoomerangConfig.logger = customLogger

// Disable logging
BoomerangConfig.logger = null
```

When logging is enabled, Boomerang will log operations like storing and retrieving values, which can be helpful for debugging navigation flows and understanding how data is being passed between screens.

## Advanced Usage

### Custom BoomerangStore Implementation

You can create your own implementation of the `BoomerangStore` interface if you need custom behavior:

```kotlin
class MyCustomBoomerangStore : BoomerangStore {
    // Custom implementation of BoomerangStore methods
}
```

### Custom BoomerangCatcher Implementation

You can create a reusable `BoomerangCatcher` implementation:

```kotlin
val myCatcher = BoomerangCatcher { bundle: Bundle ->
    // Process the bundle
    true // Return true to indicate the value was successfully caught
}

// Use the catcher
store.tryCatch("some_key", myCatcher)
```

## Platform-Specific Implementations

The Core module provides platform-specific implementations of the `Boomerang` interface:

### Android

On Android, the `AndroidBoomerang` class implements the `Boomerang` interface using Android's Bundle for storage. This provides efficient integration with Android's saved instance state mechanism.

### iOS and Desktop

On iOS and Desktop, the `MapBoomerang` class implements the `Boomerang` interface using a MutableMap for storage. This provides a lightweight and efficient storage mechanism for these platforms.

The `BoomerangFactory` object provides a platform-specific factory for creating the appropriate `Boomerang` implementation for the current platform.

## Requirements

- Kotlin 1.5.0+

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
