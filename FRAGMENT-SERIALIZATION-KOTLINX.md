# Module fragment-serialization-kotlinx

Fragment lifecycle extensions for catching `@Serializable` objects from the Boomerang store. This module connects the Fragment module with the Serialization module, so you can catch typed objects in a single call from `onCreate`.

Supports primitives, enums, nested objects, lists, and maps. Android only.

## Installation

```kotlin
implementation("io.github.buszi.boomerang:core:1.6.0")
implementation("io.github.buszi.boomerang:fragment:1.6.0")
implementation("io.github.buszi.boomerang:serialization-kotlinx:1.6.0")
implementation("io.github.buszi.boomerang:fragment-serialization-kotlinx:1.6.0")
```

## Usage

### Catching a serializable object

`catchSerializableWithLifecycleEvent` deserializes the result and passes it to your lambda. Return `true` to consume (remove from store), `false` to leave it.

```kotlin
class PreferencesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catchSerializableWithLifecycleEvent<UserPreference>("user_preferences") { preference ->
            // use preference
            true
        }
    }
}
```

### Consuming (always remove)

If you always want to consume the result, use `consumeSerializableWithLifecycleEvent` -- it automatically returns `true` for you:

```kotlin
consumeSerializableWithLifecycleEvent<UserPreference>("user_preferences") { preference ->
    // use preference
}
```

### Using type as key

Both `catch` and `consume` variants have overloads that use the type's qualified name as the key:

```kotlin
catchSerializableWithLifecycleEvent<UserPreference> { preference ->
    // use preference
    true
}

consumeSerializableWithLifecycleEvent<UserPreference> { preference ->
    // use preference
}
```

This pairs with `store.storeValue(userPreference)` (no explicit key) on the sending side.

### Storing

Storing is done through the serialization module's extension functions -- this module only adds the Fragment catching side:

```kotlin
val store = findBoomerangStore()
store.storeValue(UserPreference("dark", true, 14))

// Or with an explicit key
store.storeValue("user_preferences", UserPreference("dark", true, 14))
```

### Custom lifecycle event

By default, catching happens on `ON_START`. You can change this:

```kotlin
catchSerializableWithLifecycleEvent<UserPreference>(
    key = "user_preferences",
    lifecycleEvent = Lifecycle.Event.ON_RESUME
) { preference ->
    // use preference
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
| `catchSerializableWithLifecycleEvent(key, ...) { }` | Catch and optionally consume a `@Serializable` object at a lifecycle event |
| `catchSerializableWithLifecycleEvent<T> { }` | Same, using type's qualified name as key |
| `consumeSerializableWithLifecycleEvent(key, ...) { }` | Catch and always consume a `@Serializable` object at a lifecycle event |
| `consumeSerializableWithLifecycleEvent<T> { }` | Same, using type's qualified name as key |

## Requirements

- Android API 21+
- Kotlin 1.5.0+
- AndroidX Fragment 1.3.0+
- Kotlinx Serialization 1.5.0+
