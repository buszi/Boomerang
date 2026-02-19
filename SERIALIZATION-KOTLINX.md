# Module serialization-kotlinx

Kotlinx Serialization integration for Boomerang. Store and retrieve `@Serializable` objects directly -- no manual key-value packing needed.

Supports primitives, enums, nested objects, lists, and maps. Targets Android, iOS, and Desktop.

## Installation

```kotlin
implementation("io.github.buszi.boomerang:core:1.6.0")
implementation("io.github.buszi.boomerang:serialization-kotlinx:1.6.0")

// Kotlinx Serialization dependency
implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:<version>")
```

## Configuration

`BoomerangFormat` bridges Kotlinx Serialization and the Boomerang container. By default, the built-in format handles standard `@Serializable` classes out of the box.

If you need polymorphism or custom serializers, configure a global format:

```kotlin
BoomerangConfig.format = BoomerangFormat {
    serializersModule = SerializersModule {
        // polymorphic {}, contextual {}, etc.
    }
}
```

This affects all module helpers: `storeValue(value)`, `getSerializable()`, `putSerializable()`, and the catchers in the Compose and Fragment serialization modules.

You can also create a local instance for one-off use:

```kotlin
val format = BoomerangFormat { /* ... */ }
val boomerang: Boomerang = format.serialize(myObject)
val back: MyType = format.deserialize(boomerang)
```

## Usage

### Store and retrieve

```kotlin
@Serializable
data class UserPreference(
    val theme: String,
    val notificationsEnabled: Boolean,
    val fontSize: Int
)

val store: BoomerangStore = // from LocalBoomerangStore.current or findBoomerangStore()

// Store with an explicit key
store.storeValue("user_preferences", UserPreference("dark", true, 14))

// Or use the type's qualified name as the key
store.storeValue(UserPreference("dark", true, 14))

// Retrieve
val pref: UserPreference? = store.getSerializable("user_preferences")
val pref: UserPreference? = store.getSerializable() // type-based key
```

### Maps

Objects with `Map` properties work out of the box:

```kotlin
@Serializable
data class AppConfig(
    val settings: Map<String, String>,
    val featureFlags: Map<String, Boolean>
)

store.storeValue(AppConfig(
    settings = mapOf("theme" to "dark", "lang" to "en"),
    featureFlags = mapOf("newUI" to true, "beta" to false)
))

val config: AppConfig? = store.getSerializable()
```

Maps with non-string keys (e.g., `Map<Int, String>`, `Map<MyEnum, Boolean>`) are supported too.

### Low-level Boomerang access

You can also add serializable objects to an existing `Boomerang` instance directly:

```kotlin
val boomerang = emptyBoomerang()
boomerang.putSerializable("prefs", UserPreference("dark", true, 14))

val pref: UserPreference? = boomerang.getSerializable("prefs")
```

### Custom catchers

If you're working with the core `tryConsumeValue` API directly, you can create a catcher that deserializes automatically:

```kotlin
val catcher = kotlinxSerializationBoomerangCatcher<UserPreference> { pref ->
    // use pref
    true
}
store.tryConsumeValue("user_preferences", catcher)
```

## API Reference

### BoomerangStore extensions

| Function | Description |
|----------|-------------|
| `storeValue(key, value)` | Serialize and store an object with an explicit key |
| `storeValue(value)` | Serialize and store, using the type's qualified name as key |
| `getSerializable(key)` | Retrieve and deserialize an object by key |
| `getSerializable()` | Retrieve and deserialize, using the type's qualified name as key |

### Boomerang extensions

| Function | Description |
|----------|-------------|
| `putSerializable(key, value)` | Add a serializable object to a Boomerang |
| `getSerializable(key)` | Retrieve a serializable object from a Boomerang |

### Other

| Component | Description |
|-----------|-------------|
| `BoomerangFormat` | Serializes objects to `Boomerang` and back. Configurable via `SerializersModule`. |
| `kotlinxSerializationBoomerangCatcher` | Creates a `BoomerangCatcher` that deserializes before passing to your lambda |

## Requirements

- Kotlin 1.5.0+
- Kotlinx Serialization 1.5.0+
- Android API 21+ / iOS 14+ / Desktop JVM 11+
