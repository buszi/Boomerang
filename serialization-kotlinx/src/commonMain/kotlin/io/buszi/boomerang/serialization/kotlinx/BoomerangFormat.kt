package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.Boomerang
import io.buszi.boomerang.core.BoomerangConfig
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

/**
 * A small adapter that bridges Kotlinx Serialization with Boomerang.
 *
 * `BoomerangFormat` provides `serialize`/`deserialize` helpers that convert between
 * arbitrary `@Serializable` objects and the `Boomerang` key–value container used
 * throughout the Boomerang ecosystem (core, fragment, compose, etc.).
 *
 * Configuration
 * - You can pass a custom [serializersModule] to register polymorphic serializers,
 *   custom serializers, and context-aware serialization.
 * - A singleton, lazily configurable instance is exposed via [companion object].
 *   You can globally replace it using [BoomerangConfig.format].
 *
 * Thread-safety
 * - Instances are immutable and can be safely shared across threads.
 *
 * Typical usage
 * ```kotlin
 * @Serializable data class User(val id: String)
 *
 * // Serialize to a Boomerang
 * val b: Boomerang = BoomerangFormat.serialize(User("42"))
 *
 * // Deserialize back
 * val u: User = BoomerangFormat.deserialize(b)
 *
 * // Or configure globally for extension functions in this module
 * BoomerangConfig.format = BoomerangFormat {
 *     serializersModule = SerializersModule { /* polymorphic {} etc. */ }
 * }
 * ```
 */
open class BoomerangFormat(
    /**
     * The Kotlinx Serialization [SerializersModule] used for resolving contextual,
     * polymorphic, and custom serializers during encode/decode. Defaults to
     * [EmptySerializersModule].
     */
    val serializersModule: SerializersModule = EmptySerializersModule(),
) {

    /**
     * Serializes the given value of type [T] into a [Boomerang].
     *
     * - The reified type [T] must be `@Serializable` or have a registered serializer
     *   in [serializersModule].
     * - For collections and nested objects, structure is preserved using nested
     *   `Boomerang` instances and lists.
     */
    inline fun <reified T : Any> serialize(value: T): Boomerang {
        val encoder = RootBoomerangEncoder(serializersModule)
        encoder.encodeSerializableValue(serializer<T>(), value)
        return encoder.boomerang
    }

    /**
     * Deserializes a value of type [T] from the provided [boomerang].
     *
     * - Missing primitive values are read as their default values by the decoder.
     * - For nested objects/lists, the structure created by [serialize] is expected.
     */
    inline fun <reified T : Any> deserialize(boomerang: Boomerang): T {
        val decoder = RootBoomerangDecoder(boomerang, serializersModule)
        return decoder.decodeSerializableValue(serializer<T>())
    }

    /**
     * Default singleton-format and a lightweight builder API.
     *
     * You can configure a custom instance via `BoomerangFormat { ... }` and assign it
     * to [BoomerangConfig.format] to affect the extension helpers in this module.
     */
    companion object : BoomerangFormat() {

        internal var INSTANCE: BoomerangFormat = this

        /**
         * Builder used by `BoomerangFormat { ... }` for configuring a custom instance.
         */
        class Builder {
            /**
             * Serializers module to be used by the created [BoomerangFormat] instance.
             * Defaults to [EmptySerializersModule].
             */
            var serializersModule: SerializersModule = EmptySerializersModule()
        }

        /**
         * Creates a new [BoomerangFormat] using the provided [block] to configure
         * [Builder] options.
         */
        operator fun invoke(block: Builder.() -> Unit): BoomerangFormat {
            val builder = Builder().apply(block)
            return BoomerangFormat(
                serializersModule = builder.serializersModule,
            )
        }
    }
}

/**
 * Global `BoomerangFormat` accessor used by the Kotlinx integration helpers in this module.
 *
 * Assigning this property allows applications to plug a custom serializers module or an
 * entirely custom `BoomerangFormat` implementation, affecting helpers like
 * `BoomerangStore.storeValue`, `Boomerang.getSerializable`, etc.
 */
@Suppress("UnusedReceiverParameter")
var BoomerangConfig.format: BoomerangFormat
    get() = BoomerangFormat.INSTANCE
    set(value) {
        BoomerangFormat.INSTANCE = value
    }
