# Boomerang

[![UI Tests](https://github.com/buszi/Boomerang/actions/workflows/android-ui-tests.yml/badge.svg)](https://github.com/buszi/Boomerang/actions/workflows/android-ui-tests.yml)
[![Version](https://img.shields.io/maven-central/v/io.github.buszi.boomerang/core)](https://central.sonatype.com/artifact/io.github.buszi.boomerang/core)

Type-safe navigation results for Compose Multiplatform and AndroidX Fragment apps. Pass `@Serializable` objects between screens -- across Android, iOS, and Desktop.

## Quick Start

Define a serializable result, store it when leaving a screen, and catch it when arriving back:

```kotlin
@Serializable
data class UserSelection(val itemId: String, val quantity: Int)

// Screen B: store the result before navigating back
val store = LocalBoomerangStore.current
store.storeValue(UserSelection(itemId = "abc", quantity = 2))
navController.popBackStack()

// Screen A: catch the result when this screen becomes visible again
@Composable
fun ScreenA() {
    var selection by remember { mutableStateOf<UserSelection?>(null) }

    ConsumeSerializableLifecycleEffect<UserSelection> { result ->
        selection = result
    }
}
```

No manual key-value packing or type casting. The object goes in, the same object comes out.

## Why Boomerang?

Passing results between screens is a solved problem -- until you try to do it cleanly. Most approaches either couple your screens together, lose data on configuration changes, or force you to serialize everything by hand.

Boomerang gives you a shared store that sits outside your navigation graph. One screen writes to it, another reads from it, and neither needs to know the other exists. The store survives configuration changes and process death. And with the Kotlinx Serialization integration, you can pass rich objects (nested data classes, lists, maps, enums) without writing a single `putString`/`getString` pair.

It works with any navigation library -- Jetpack Navigation, Voyager, Decompose, or your own -- because it doesn't depend on one. It also doesn't hold references to your screens, so there are no memory leaks to worry about.

## Features

- Pass `@Serializable` objects between screens with full type safety
- Supports primitives, nested objects, lists, maps, and enums
- Works with any Compose or Fragment navigation library
- Kotlin Multiplatform -- Android, iOS, and Desktop from a single API
- Survives configuration changes and process death
- Modular -- use only the parts you need
- Lightweight with minimal dependencies
- Mixed Compose + Fragment projects supported
- Simple event notifications for cases where you don't need data

## Platform Support

Boomerang targets all Kotlin Multiplatform Compose platforms:

| Platform | Core | Compose | Fragment | Serialization |
|----------|------|---------|----------|---------------|
| Android  | Yes  | Yes     | Yes      | Yes           |
| iOS      | Yes  | Yes     | --       | Yes           |
| Desktop  | Yes  | Yes     | --       | Yes           |

On Android, storage is backed by `Bundle` for native integration with saved instance state. On iOS and Desktop, a `MutableMap` is used internally.

## Installation

Add the modules you need to your `build.gradle.kts`:

```kotlin
// Core (required by all modules)
implementation("io.github.buszi.boomerang:core:1.6.0")

// Compose integration
implementation("io.github.buszi.boomerang:compose:1.6.0")

// Kotlinx Serialization integration
implementation("io.github.buszi.boomerang:serialization-kotlinx:1.6.0")

// Compose + Serialization (lifecycle-aware catching of @Serializable objects)
implementation("io.github.buszi.boomerang:compose-serialization-kotlinx:1.6.0")
```

**Fragment users (Android only):**

```kotlin
implementation("io.github.buszi.boomerang:fragment:1.6.0")
implementation("io.github.buszi.boomerang:fragment-serialization-kotlinx:1.6.0")
```

Pick what fits your project. A Compose-only app typically needs `core`, `compose`, `serialization-kotlinx`, and `compose-serialization-kotlinx`. A Fragment-only app needs `core`, `fragment`, `serialization-kotlinx`, and `fragment-serialization-kotlinx`.

## Usage

### Setup

**Compose** -- wrap your app content in a `CompositionHostedDefaultBoomerangStoreScope`:

```kotlin
@Composable
fun YourApplication() {
    CompositionHostedDefaultBoomerangStoreScope {
        AppNavigation()
    }
}
```

**Fragment** -- make your Activity implement `BoomerangStoreHost`:

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

**Mixed (Compose + Fragment)** -- set up the Activity as above, then use `ActivityHostedBoomerangStoreScope` in your Compose code to share the same store.

### Passing Serializable Objects

This is the recommended way to pass data between screens. Define your data as a `@Serializable` class and let Boomerang handle the rest.

#### Storing

```kotlin
@Serializable
data class UserPreference(
    val theme: String,
    val notificationsEnabled: Boolean,
    val fontSize: Int
)

// In Compose
val store = LocalBoomerangStore.current
store.storeValue(UserPreference("dark", true, 14))

// In Fragment
findBoomerangStore().storeValue(UserPreference("dark", true, 14))
```

You can also store with an explicit key if you prefer: `store.storeValue("my_key", userPreference)`.

#### Catching in Compose

```kotlin
@Composable
fun PreferencesScreen() {
    var pref by remember { mutableStateOf<UserPreference?>(null) }

    CatchSerializableLifecycleEffect<UserPreference> { preference ->
        pref = preference
        // true = consumed, remove from store
        // (or use Consume* functions to auto-pass true and always remove entry from store)
        true
    }
}
```

#### Catching in Fragment

```kotlin
class PreferencesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catchSerializableWithLifecycleEvent<UserPreference> { preference ->
            // use preference
            true
        }
    }
}
```

#### Maps, Lists, and Nested Objects

All of these work out of the box:

```kotlin
@Serializable
data class AppConfig(
    val settings: Map<String, String>,
    val featureFlags: Map<String, Boolean>,
    val recentSearches: List<String>
)

store.storeValue(AppConfig(
    settings = mapOf("theme" to "dark", "lang" to "en"),
    featureFlags = mapOf("newUI" to true, "beta" to false),
    recentSearches = listOf("kotlin", "compose")
))
```

Maps with non-string keys (e.g., `Map<Int, String>`, `Map<MyEnum, Boolean>`) are supported too.

#### Custom Serialization Configuration

If you need polymorphism or custom serializers, configure `BoomerangFormat` globally:

```kotlin
BoomerangConfig.format = BoomerangFormat {
    serializersModule = SerializersModule {
        // polymorphic {}, contextual {}, etc.
    }
}
```

Or create a local instance for one-off use:

```kotlin
val format = BoomerangFormat { /* ... */ }
val boomerang: Boomerang = format.serialize(myObject)
val back: MyType = format.deserialize(boomerang)
```

### Passing Simple Values

For cases where you just need to pass a couple of primitives and don't want to define a data class:

```kotlin
// Store
store.storeValue("home_screen_result", boomerangOf("selectedItem" to "Item 1"))

// Catch in Compose
CatchBoomerangLifecycleEffect("home_screen_result") { boomerang ->
    val selectedItem = boomerang.getString("selectedItem")
    true
}

// Catch in Fragment
catchBoomerangWithLifecycleEvent("home_screen_result") { boomerang ->
    val selectedItem = boomerang.getString("selectedItem")
    true
}
```

### Event Notifications

When you only need to signal that something happened, without passing any data:

```kotlin
// Store an event
store.storeEvent("refresh_needed")

// Catch in Compose
CatchEventBoomerangLifecycleEffect("refresh_needed") {
    // react to the event
}

// Catch in Fragment
catchEventBoomerangWithLifecycleEvent("refresh_needed") {
    // react to the event
}
```

### Logging

Boomerang can log store operations to help with debugging:

```kotlin
// Android LogCat
BoomerangConfig.logger = AndroidBoomerangLogger(LogLevel.DEBUG)

// Console
BoomerangConfig.logger = BoomerangLogger.PRINT_LOGGER

// Disable (default)
BoomerangConfig.logger = null
```

## Modules

| Module | Scope | Purpose                                                     |
|--------|-------|-------------------------------------------------------------|
| `core` | KMP | Core concepts, interfaces and platform implementations      |
| `compose` | KMP | `LocalBoomerangStore`, lifecycle-aware composables          |
| `fragment` | Android | Fragment extensions, `BoomerangStoreHost`                   |
| `serialization-kotlinx` | KMP | `BoomerangFormat`, `storeValue<T>()`/`getSerializable<T>()` |
| `compose-serialization-kotlinx` | KMP | `Catch/ConsumeSerializableLifecycleEffect` and friends      |
| `fragment-serialization-kotlinx` | Android | `catch/consumeSerializableWithLifecycleEvent` and friends   |

For detailed API docs, see the module-specific documentation: [Core](CORE.md), [Compose](COMPOSE.md), [Fragment](FRAGMENT.md), [Serialization](SERIALIZATION-KOTLINX.md), [Compose Serialization](COMPOSE-SERIALIZATION-KOTLINX.md), [Fragment Serialization](FRAGMENT-SERIALIZATION-KOTLINX.md).

## Sample App

The `app` module contains a working sample covering Compose navigation (Android, Desktop, iOS) and Fragment navigation (Android).

## Requirements

- Android API 21+
- Kotlin 1.5.0+
- Compose Multiplatform 1.0.0+ (for Compose modules)
- AndroidX Fragment 1.3.0+ (for Fragment modules)
- Kotlinx Serialization 1.5.0+ (for Serialization modules)

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
