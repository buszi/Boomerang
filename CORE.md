# Module core

The foundation of Boomerang. Defines the core interfaces and platform implementations for storing, retrieving, and processing navigation results between screens.

This module is a dependency of every other Boomerang module. You rarely use it directly -- the Compose and Fragment modules build on top of it -- but it's the right starting point if you're building a custom integration or just want to understand how things work under the hood.

Targets Android, iOS, and Desktop.

## Installation

```kotlin
implementation("io.github.buszi.boomerang:core:1.5.1")
```

## Key Concepts

### Boomerang

A typed key-value container for navigation results. Think of it like a `Bundle` that works on every platform. You put values in, pass it through the store, and pull values out on the other side.

Platform implementations:
- **Android** -- `AndroidBoomerang`, backed by `Bundle`
- **iOS / Desktop** -- `MapBoomerang`, backed by `MutableMap`

You create instances through factory functions:

```kotlin
// From key-value pairs
val boomerang = boomerangOf("name" to "Alice", "age" to 30)

// With a builder
val boomerang = buildBoomerang {
    putString("name", "Alice")
    putInt("age", 30)
}

// Empty
val boomerang = emptyBoomerang()
```

### BoomerangStore

The central store that holds navigation results. One screen writes to it, another reads from it.

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

`DefaultBoomerangStore` is the built-in implementation. It stores entries in a `MutableMap` and supports packing/restoring its state for surviving configuration changes and process death.

### BoomerangCatcher

A functional interface that processes a `Boomerang` and returns `true` if it was successfully handled (which removes it from the store):

```kotlin
fun interface BoomerangCatcher {
    fun tryCatch(value: Boomerang): Boolean
}
```

### BoomerangStoreHost

An interface for components that own a `BoomerangStore` -- typically your Activity. Used by the Fragment module and mixed Compose+Fragment setups.

```kotlin
interface BoomerangStoreHost {
    var boomerangStore: BoomerangStore?
}
```

## Usage

Most of the time you'll interact with the store through the Compose or Fragment modules. But here's how the core API works directly.

### Storing and retrieving values

```kotlin
val store = DefaultBoomerangStore()

// Store a result
store.storeValue("result_key") {
    putString("selectedItem", "Item 1")
    putInt("quantity", 5)
}

// Retrieve it
val boomerang = store.getValue("result_key")
val selectedItem = boomerang?.getString("selectedItem")
```

### Catching values

Catching retrieves a value and removes it from the store in one step:

```kotlin
store.tryConsumeValue("result_key") { boomerang ->
    val selectedItem = boomerang.getString("selectedItem")
    true // consumed -- remove from store
}
```

### Events

For simple notifications without data:

```kotlin
store.storeEvent("refresh_needed")

val catcher = eventBoomerangCatcher("refresh_needed") {
    println("Event received!")
}
store.tryConsumeValue("refresh_needed", catcher)
```

### State preservation

Pack the store's state into a `Boomerang` for saving, and restore it later:

```kotlin
val packed = store.packState()

// Later...
val restored = DefaultBoomerangStore()
restored.restoreState(packed)
```

## Logging

Optional logging for debugging store operations:

```kotlin
// Android LogCat
BoomerangConfig.logger = AndroidBoomerangLogger(LogLevel.DEBUG)

// Console
BoomerangConfig.logger = BoomerangLogger.PRINT_LOGGER

// Custom
BoomerangConfig.logger = object : BoomerangLogger {
    override fun log(tag: String, message: String) {
        YourLoggingSystem.log("$tag: $message")
    }
}

// Disable (default)
BoomerangConfig.logger = null
```

## Requirements

- Kotlin 1.5.0+
- Android API 21+
- iOS 14+
- Desktop: JVM 11+
