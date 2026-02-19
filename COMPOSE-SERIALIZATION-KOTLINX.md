# Module compose-serialization-kotlinx

Compose lifecycle effects for catching `@Serializable` objects from the Boomerang store. This module connects the Compose module with the Serialization module, so you can catch typed objects in a single composable call.

Supports primitives, enums, nested objects, lists, and maps. Targets Android, iOS, and Desktop.

## Installation

```kotlin
implementation("io.github.buszi.boomerang:core:1.6.0")
implementation("io.github.buszi.boomerang:compose:1.6.0")
implementation("io.github.buszi.boomerang:serialization-kotlinx:1.6.0")
implementation("io.github.buszi.boomerang:compose-serialization-kotlinx:1.6.0")
```

## Usage

### Catching a serializable object

`CatchSerializableLifecycleEffect` deserializes the result and passes it to your lambda. Return `true` to consume (remove from store), `false` to leave it.

```kotlin
@Composable
fun PreferencesScreen() {
    var pref by remember { mutableStateOf<UserPreference?>(null) }

    CatchSerializableLifecycleEffect<UserPreference>("user_preferences") { preference ->
        pref = preference
        true
    }
}
```

### Consuming (always remove)

If you always want to consume the result, use `ConsumeSerializableLifecycleEffect` -- it automatically returns `true` for you:

```kotlin
ConsumeSerializableLifecycleEffect<UserPreference>("user_preferences") { preference ->
    pref = preference
}
```

### Using type as key

Both `Catch` and `Consume` variants have overloads that use the type's qualified name as the key, so you don't need to manage key strings:

```kotlin
// These use the qualified name of UserPreference as the key
CatchSerializableLifecycleEffect<UserPreference> { preference ->
    pref = preference
    true
}

ConsumeSerializableLifecycleEffect<UserPreference> { preference ->
    pref = preference
}
```

This pairs with `store.storeValue(userPreference)` (no explicit key) on the sending side.

### Storing

Storing is done through the serialization module's extension functions -- this module only adds the Compose catching side:

```kotlin
val store = LocalBoomerangStore.current
store.storeValue(UserPreference("dark", true, 14))

// Or with an explicit key
store.storeValue("user_preferences", UserPreference("dark", true, 14))
```

### Custom lifecycle event

By default, catching happens on `ON_START`. You can change this:

```kotlin
CatchSerializableLifecycleEffect<UserPreference>(
    key = "user_preferences",
    lifecycleEvent = Lifecycle.Event.ON_RESUME
) { preference ->
    pref = preference
    true
}
```

## Configuration

If you need custom serializers, configure `BoomerangFormat` globally (see [Serialization module docs](SERIALIZATION-KOTLINX.md)):

```kotlin
BoomerangConfig.format = BoomerangFormat {
    serializersModule = SerializersModule { /* ... */ }
}
```

## API Reference

| Component | Description |
|-----------|-------------|
| `CatchSerializableLifecycleEffect(key, ...) { }` | Catch and optionally consume a `@Serializable` object at a lifecycle event |
| `CatchSerializableLifecycleEffect<T> { }` | Same, using type's qualified name as key |
| `ConsumeSerializableLifecycleEffect(key, ...) { }` | Catch and always consume a `@Serializable` object at a lifecycle event |
| `ConsumeSerializableLifecycleEffect<T> { }` | Same, using type's qualified name as key |

## Requirements

- Kotlin 1.5.0+
- Compose Multiplatform 1.0.0+
- Kotlinx Serialization 1.5.0+
- Android API 21+ / iOS 14+ / Desktop JVM 11+
