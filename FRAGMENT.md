# Module fragment

A lightweight library for handling navigation results in AndroidX Fragment applications.

## Overview

The Fragment module of Boomerang provides integration with AndroidX Fragments, allowing you to pass data between screens in Fragment navigation without tight coupling between components. It solves the common problem of returning results from one screen to another, similar to the old `setFragmentResultListener` pattern but designed specifically for modern Fragment navigation patterns.

## Installation

Add the following dependencies to your app's `build.gradle.kts` file:

```kotlin
// For core functionality (required)
implementation("io.github.buszi.boomerang:core:1.4.0")

// For AndroidX Fragment integration
implementation("io.github.buszi.boomerang:fragment:1.4.0")
```

## Setup

To set up Boomerang in a Fragment application, make your Activity implement `BoomerangStoreHost` and initialize the store:

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

This creates a `DefaultBoomerangStore` and makes it available to all Fragments hosted by this Activity. The store's state is automatically preserved across configuration changes.

## Usage

### Storing a Result

When you want to store a result to be consumed by another screen:

```kotlin
class DetailFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.selectButton).setOnClickListener {
            // Find the store and store the result with a key using the builder pattern
            findBoomerangStore().storeValue("home_screen_result") {
                putString("selectedItem", "Item 1")
            }

            // Navigate back
            findNavController().popBackStack()
        }
    }
}
```

### Catching a Result

To catch and process a result when a Fragment becomes visible:

```kotlin
class HomeFragment : Fragment() {

    private var selectedItem: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up a catcher that runs when the fragment starts
        catchBoomerangWithLifecycleEvent("home_screen_result") { boomerang ->
            // Extract data from the boomerang and process it
            selectedItem = boomerang.getString("selectedItem")
            // Update UI or perform other actions with the result
            true // Return true to indicate the result was successfully processed
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display the result
        updateUI()
    }

    private fun updateUI() {
        // Update UI with the selected item
        // For example: textView.text = "Selected item: " + selectedItem
    }
}
```

By default, `catchBoomerangWithLifecycleEvent` triggers on the `ON_START` lifecycle event, but you can specify `ON_RESUME` if needed.

### Catching an Event

For simple notifications without data, you can use the event handling feature:

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display the event status
        updateUI()

        // Button to clear the event status
        view.findViewById<Button>(R.id.clearEventButton).setOnClickListener {
            eventReceived = false
            updateUI()
        }
    }

    private fun updateUI() {
        // Update UI with the event status
        view?.let {
            val statusText = "Event status: ${if (eventReceived) "Event received" else "No event received"}"
            it.findViewById<TextView>(R.id.eventStatusText).text = statusText
        }
    }
}
```

To store an event:

```kotlin
class EventTriggerFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.triggerEventButton).setOnClickListener {
            // Store an event notification
            findBoomerangStore().storeEvent("notification_event")

            // Navigate back
            findNavController().popBackStack()
        }
    }
}
```

## Key Components

### findBoomerangStore

An extension function for Fragment that finds a BoomerangStore by looking for a BoomerangStoreHost in the Fragment's activity:

```kotlin
// Inside a Fragment
val store = findBoomerangStore()

// Check if a result exists
val hasResult = store.getValue("some_key") != null

// Manually drop a value
store.dropValue("some_key")
```

### catchBoomerangWithLifecycleEvent

An extension function for Fragment that sets up a lifecycle observer to catch a boomerang value when a specific lifecycle event occurs:

```kotlin
// Inside a Fragment's onCreate method
catchBoomerangWithLifecycleEvent(
    key = "home_screen_result",
    lifecycleEvent = Lifecycle.Event.ON_START
) { bundle: Bundle ->
    // Process the result
    true // Return true to indicate the result was successfully processed
}
```

### catchEventBoomerangWithLifecycleEvent

An extension function for Fragment that simplifies catching event notifications from the BoomerangStore. This is a specialized version of catchBoomerangWithLifecycleEvent specifically for handling simple event notifications without additional data:

```kotlin
// Inside a Fragment's onCreate method
catchEventBoomerangWithLifecycleEvent(
    key = "notification_event",
    lifecycleEvent = Lifecycle.Event.ON_START
) {
    // This callback is executed when the event is caught
    // Update UI or perform other actions
}
```

### createOrRestoreDefaultBoomerangStore

An extension function for BoomerangStoreHost that creates a new DefaultBoomerangStore or restores one from a saved state:

```kotlin
// Example of using createOrRestoreDefaultBoomerangStore in an Activity
class MainActivity : AppCompatActivity(), BoomerangStoreHost {
    override var boomerangStore: BoomerangStore? = null

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createOrRestoreDefaultBoomerangStore(savedInstanceState)
        // ...
    }
}
```

### saveDefaultBoomerangStoreState

An extension function for BoomerangStoreHost that saves the state of a DefaultBoomerangStore to a Bundle:

```kotlin
// Example of using saveDefaultBoomerangStoreState in an Activity
class MainActivity : AppCompatActivity(), BoomerangStoreHost {
    override var boomerangStore: BoomerangStore? = null

    fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveDefaultBoomerangStoreState(outState)
    }
}
```

## Advanced Usage

### Custom Lifecycle Events

You can specify when to catch results by providing a different lifecycle event:

```kotlin
catchBoomerangWithLifecycleEvent(
    key = "home_screen_result",
    lifecycleEvent = Lifecycle.Event.ON_RESUME
) { boomerang ->
    // Process the result
    true
}
```

### Manual Catching

You can manually catch results without using the lifecycle event:

```kotlin
class ManualCatchFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.checkButton).setOnClickListener {
            val store = findBoomerangStore()
            store.tryConsumeValue("some_key") { boomerang ->
                // Process the result
                true
            }
        }
    }
}
```

## Requirements

- Android API level 21+
- Kotlin 1.5.0+
- AndroidX Fragment 1.3.0+

## Sample App

The repository includes a sample app that demonstrates how to use Boomerang in a real-world scenario. Check the `app` module for complete examples of Fragment navigation with Boomerang.

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
