# Module fragment

AndroidX Fragment integration for Boomerang. Provides lifecycle-aware extension functions for catching navigation results in Fragments.

Android only.

## Installation

```kotlin
implementation("io.github.buszi.boomerang:core:1.6.0")
implementation("io.github.buszi.boomerang:fragment:1.6.0")
```

## Setup

Make your Activity implement `BoomerangStoreHost` and initialize the store:

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

This creates a `DefaultBoomerangStore` and makes it available to all Fragments in the Activity. The store's state is preserved across configuration changes and process death.

## Usage

### Accessing the store

```kotlin
val store = findBoomerangStore()
```

### Storing a result

```kotlin
class DetailFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.selectButton).setOnClickListener {
            findBoomerangStore().storeValue("home_screen_result") {
                putString("selectedItem", "Item 1")
            }
            findNavController().popBackStack()
        }
    }
}
```

### Catching a result

`catchBoomerangWithLifecycleEvent` sets up a lifecycle observer that tries to catch a result when the Fragment reaches a lifecycle event (defaults to `ON_START`). Return `true` to consume it.

```kotlin
class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catchBoomerangWithLifecycleEvent("home_screen_result") { boomerang ->
            val selectedItem = boomerang.getString("selectedItem")
            true
        }
    }
}
```

To catch on a different lifecycle event:

```kotlin
catchBoomerangWithLifecycleEvent(
    key = "home_screen_result",
    lifecycleEvent = Lifecycle.Event.ON_RESUME
) { boomerang ->
    // ...
    true
}
```

### Event notifications

For signals without data:

```kotlin
// Sending fragment
findBoomerangStore().storeEvent("refresh_needed")

// Receiving fragment
catchEventBoomerangWithLifecycleEvent("refresh_needed") {
    // react to the event
}
```

### Manual catching

You can also catch results outside of lifecycle events:

```kotlin
val store = findBoomerangStore()
store.tryConsumeValue("some_key") { boomerang ->
    // process
    true
}
```

## API Reference

| Component | What it does |
|-----------|-------------|
| `findBoomerangStore()` | Finds the `BoomerangStore` from the hosting Activity |
| `catchBoomerangWithLifecycleEvent` | Catches a result at a lifecycle event |
| `catchEventBoomerangWithLifecycleEvent` | Catches an event notification at a lifecycle event |
| `createOrRestoreDefaultBoomerangStore` | Creates or restores a `DefaultBoomerangStore` in a `BoomerangStoreHost` |
| `saveDefaultBoomerangStoreState` | Saves store state to a `Bundle` for process death survival |

## Requirements

- Android API 21+
- Kotlin 1.5.0+
- AndroidX Fragment 1.3.0+
