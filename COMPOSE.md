# Module compose

Compose Multiplatform integration for Boomerang. Provides lifecycle-aware composables for catching navigation results and a `CompositionLocal` for accessing the store.

Targets Android, iOS, and Desktop.

## Installation

```kotlin
implementation("io.github.buszi.boomerang:core:1.6.0")
implementation("io.github.buszi.boomerang:compose:1.6.0")
```

## Setup

### Pure Compose

Wrap your app content in a `CompositionHostedDefaultBoomerangStoreScope`. This creates a `DefaultBoomerangStore`, provides it via `LocalBoomerangStore`, and automatically preserves its state across configuration changes.

```kotlin
@Composable
fun YourApplication() {
    CompositionHostedDefaultBoomerangStoreScope {
        AppNavigation()
    }
}
```

### Mixed (Compose + Fragment)

If your app uses both Compose and Fragments, set up the store in your Activity (see the [Fragment module docs](FRAGMENT.md)) and use `ActivityHostedBoomerangStoreScope` to share it with Compose:

```kotlin
@Composable
fun YourComposeUI() {
    ActivityHostedBoomerangStoreScope {
        // Compose content -- same store as your Fragments
    }
}
```

### Custom store

If you have your own `BoomerangStore` implementation, use `CompositionHostedBoomerangStoreScope` to provide it directly.

## Usage

### Accessing the store

```kotlin
val store = LocalBoomerangStore.current
```

### Storing a result

```kotlin
@Composable
fun DetailScreen(navController: NavController) {
    val store = LocalBoomerangStore.current

    Button(onClick = {
        store.storeValue("home_screen_result") {
            putString("selectedItem", "Item 1")
        }
        navController.popBackStack()
    }) {
        Text("Select and Return")
    }
}
```

### Catching a result

`CatchBoomerangLifecycleEffect` tries to catch a result when the screen reaches a lifecycle event (defaults to `ON_START`). Return `true` to consume it and remove it from the store.

```kotlin
@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    CatchBoomerangLifecycleEffect("home_screen_result") { boomerang ->
        selectedItem = boomerang.getString("selectedItem")
        true
    }

    Text("Selected item: $selectedItem")
}
```

To catch on a different lifecycle event:

```kotlin
CatchBoomerangLifecycleEffect(
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
// Sending screen
store.storeEvent("refresh_needed")

// Receiving screen
CatchEventBoomerangLifecycleEffect("refresh_needed") {
    // react to the event
}
```

### Manual catching

You can also catch results outside of lifecycle effects:

```kotlin
val store = LocalBoomerangStore.current

Button(onClick = {
    store.tryConsumeValue("some_key") { boomerang ->
        // process
        true
    }
}) {
    Text("Check for Result")
}
```

## API Reference

| Component | What it does |
|-----------|-------------|
| `LocalBoomerangStore` | `CompositionLocal` providing the current `BoomerangStore` |
| `CompositionHostedDefaultBoomerangStoreScope` | Creates and provides a `DefaultBoomerangStore` with state preservation |
| `CompositionHostedBoomerangStoreScope` | Provides a custom `BoomerangStore` implementation |
| `ActivityHostedBoomerangStoreScope` | Provides the store from a `BoomerangStoreHost` Activity |
| `CatchBoomerangLifecycleEffect` | Catches a result at a lifecycle event |
| `CatchEventBoomerangLifecycleEffect` | Catches an event notification at a lifecycle event |
| `rememberBoomerangStore` | Remembers a store across recompositions (uses `rememberSaveable` on Android) |

## Requirements

- Kotlin 1.5.0+
- Compose Multiplatform 1.0.0+
- Android API 21+ / iOS 14+ / Desktop JVM 11+
