# Module core

A lightweight library providing the fundamental concepts and interfaces for handling navigation results in Android applications.

## Overview

The Core module of Boomerang contains the essential components that power both the Compose and Fragment modules. It defines the key interfaces and classes for storing, retrieving, and processing navigation results between screens without tight coupling between components.

## Installation

Add the following dependency to your app's `build.gradle.kts` file:

```kotlin
// For core functionality (required by all Boomerang modules)
implementation("io.github.buszi.boomerang:core:1.0.0-alpha01")
```

The Core module is required by both the Compose and Fragment modules, so you'll need to include it regardless of which integration you're using.

## Key Components

### BoomerangStore

The `BoomerangStore` interface defines a key-value store for navigation results:

```kotlin
interface BoomerangStore {
    fun getValue(key: String): Bundle?
    fun storeValue(key: String, value: Bundle)
    fun dropValue(key: String)
    fun tryCatch(key: String, catcher: BoomerangCatcher)
}
```

This interface provides methods for:
- Retrieving a value for a key (`getValue`)
- Storing a value with a key (`storeValue`)
- Removing a value for a key (`dropValue`)
- Trying to catch a value using a `BoomerangCatcher` (`tryCatch`)

### BoomerangCatcher

The `BoomerangCatcher` functional interface is used for catching and processing Bundle values:

```kotlin
fun interface BoomerangCatcher {
    fun tryCatch(value: Bundle): Boolean
}
```

Implementations of this interface determine whether a Bundle value should be "caught" (processed and removed from the store).

### DefaultBoomerangStore

The `DefaultBoomerangStore` class is the default implementation of the `BoomerangStore` interface:

```kotlin
class DefaultBoomerangStore() : BoomerangStore {
    // Implementation of BoomerangStore methods
}
```

The `DefaultBoomerangStore` also provides a `packState()` method that packs the store's state into a Bundle for saving, and a constructor that accepts a Bundle for restoring the state.

It uses a private mutable map to store key-value pairs and provides methods for packing and restoring its state.

### BoomerangStoreHost

The `BoomerangStoreHost` interface is for components that host a `BoomerangStore`:

```kotlin
interface BoomerangStoreHost {
    var boomerangStore: BoomerangStore?
}
```

This interface is typically implemented by Activities or other lifecycle-aware components that need to provide a `BoomerangStore` to their children.

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
// Create a bundle with your result data
val resultBundle = Bundle().apply {
    putString("selectedItem", "Item 1")
    putInt("quantity", 5)
}

// Store the result with a key
store.storeValue("home_screen_result", resultBundle)
```

### Retrieving a Value

```kotlin
// Get a value for a key
val bundle = store.getValue("home_screen_result")

// Extract data from the bundle
val selectedItem = bundle?.getString("selectedItem")
val quantity = bundle?.getInt("quantity")
```

### Catching a Value

```kotlin
// Try to catch a value using a BoomerangCatcher
store.tryCatch("home_screen_result") { bundle: Bundle ->
    // Process the bundle
    val selectedItem = bundle.getString("selectedItem")
    val quantity = bundle.getInt("quantity")

    // Return true to indicate the value was successfully caught and should be removed
    true
}
```

### Saving and Restoring State

```kotlin
// Pack the store's state into a Bundle
val stateBundle = store.packState()

// Later, restore the store from the saved state
val restoredStore = DefaultBoomerangStore(stateBundle)
```

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

## Requirements

- Android API level 21+
- Kotlin 1.5.0+

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
