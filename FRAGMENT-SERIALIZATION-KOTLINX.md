# Module fragment-serialization-kotlinx

A lightweight library for handling serializable objects in AndroidX Fragment applications using Kotlinx Serialization.

## Overview

The Fragment Serialization Kotlinx module of Boomerang provides integration between the Fragment module and Kotlinx Serialization, allowing you to pass serializable objects between screens in Fragment navigation without tight coupling between components. It simplifies the process of catching and processing serializable objects in Fragment UI.

Supports primitives, enums, nested objects, and lists. Configure via `BoomerangFormat` and `SerializersModule`.

## Installation

Add the following dependencies to your app's `build.gradle.kts` file:

```kotlin
// For core functionality (required)
implementation("io.github.buszi.boomerang:core:1.5.0")

// For AndroidX Fragment integration
implementation("io.github.buszi.boomerang:fragment:1.5.0")

// For Kotlinx Serialization integration
implementation("io.github.buszi.boomerang:serialization-kotlinx:1.5.0")

// For Fragment with Kotlinx Serialization integration
implementation("io.github.buszi.boomerang:fragment-serialization-kotlinx:1.5.0")

// Kotlinx Serialization dependency
implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
```

## Configuration

Set a global `BoomerangFormat` to configure serializers used by helpers in this module:

```kotlin
BoomerangConfig.format = BoomerangFormat {
    serializersModule = SerializersModule {
        // polymorphic {}, contextual {}, etc.
    }
}
```

This affects `storeValue(value)`, `getSerializable()`, and related helpers.

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

To catch and process a serializable object when a Fragment becomes visible:

```kotlin
class PreferencesFragment : Fragment() {

    private var userPreference: UserPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up a catcher that runs when the fragment starts
        catchSerializableWithLifecycleEvent<UserPreference>("user_preferences") { preference ->
            // Process the caught serializable object
            userPreference = preference
            updateUI()
            true // Return true to indicate the object was successfully processed
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        // Update UI with the user preference
        userPreference?.let {
            view?.findViewById<TextView>(R.id.themeText)?.text = "Theme: ${it.theme}"
            view?.findViewById<TextView>(R.id.notificationsText)?.text = "Notifications: ${it.notificationsEnabled}"
            view?.findViewById<TextView>(R.id.fontSizeText)?.text = "Font Size: ${it.fontSize}"
        }
    }
}
```

### Using Type as Key

You can also use the type of the serializable object as the key:

```kotlin
class PreferencesFragment : Fragment() {

    private var userPreference: UserPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up a catcher that uses the type as the key
        catchSerializableWithLifecycleEvent<UserPreference> { preference ->
            // Process the caught serializable object
            userPreference = preference
            updateUI()
            true // Return true to indicate the object was successfully processed
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        // Update UI with the user preference
        userPreference?.let {
            view?.findViewById<TextView>(R.id.themeText)?.text = "Theme: ${it.theme}"
            view?.findViewById<TextView>(R.id.notificationsText)?.text = "Notifications: ${it.notificationsEnabled}"
            view?.findViewById<TextView>(R.id.fontSizeText)?.text = "Font Size: ${it.fontSize}"
        }
    }
}
```

### Storing a Serializable Object

To store a serializable object that can be caught by another screen:

```kotlin
class SettingsFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
            // Create a serializable object
            val userPreference = UserPreference(
                theme = "dark",
                notificationsEnabled = true,
                fontSize = 14
            )

            // Find the store
            val store = findBoomerangStore()

            // Store the object with a specific key
            store.storeValue("user_preferences", userPreference)

            // Or store the object using its type as the key
            store.storeValue(userPreference)

            // Navigate back
            findNavController().popBackStack()
        }
    }
}
```

## Key Components

### catchSerializableWithLifecycleEvent

```kotlin
inline fun <reified T : @Serializable Any> Fragment.catchSerializableWithLifecycleEvent(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Boolean
)
```

An extension function for Fragment that catches serializable objects of type `T` at a specific lifecycle event. This function uses Kotlinx Serialization to deserialize the caught object before passing it to the catcher function. The catcher function returns a boolean indicating whether the object was successfully handled.

### consumeSerializableWithLifecycleEvent

```kotlin
inline fun <reified T : @Serializable Any> Fragment.consumeSerializableWithLifecycleEvent(
    key: String,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Unit
)
```

An extension function for Fragment that consumes serializable objects of type `T` at a specific lifecycle event. This function is similar to `catchSerializableWithLifecycleEvent` but automatically returns true after the catcher function is called, indicating that the object was successfully handled.

### Type-Based Variants

Both `catchSerializableWithLifecycleEvent` and `consumeSerializableWithLifecycleEvent` have overloads that automatically use the qualified name of type `T` as the key:

```kotlin
inline fun <reified T : @Serializable Any> Fragment.catchSerializableWithLifecycleEvent(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Boolean
)

inline fun <reified T : @Serializable Any> Fragment.consumeSerializableWithLifecycleEvent(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_START,
    crossinline catcher: (T) -> Unit
)
```

## Requirements

- Android API level 21+
- Kotlin 1.5.0+
- AndroidX Fragment 1.3.0+
- Kotlinx Serialization 1.5.0+

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