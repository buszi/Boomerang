package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.Boomerang
import io.buszi.boomerang.core.BoomerangConfig
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

open class BoomerangFormat(
    val serializersModule: SerializersModule = EmptySerializersModule(),
) {

    inline fun <reified T : Any> serialize(value: T): Boomerang {
        val encoder = RootBoomerangEncoder(serializersModule)
        encoder.encodeSerializableValue(serializer<T>(), value)
        return encoder.boomerang
    }

    inline fun <reified T : Any> deserialize(boomerang: Boomerang): T {
        val decoder = RootBoomerangDecoder(boomerang, serializersModule)
        return decoder.decodeSerializableValue(serializer<T>())
    }

    companion object : BoomerangFormat() {

        internal var INSTANCE: BoomerangFormat = this

        class Builder {
            var serializersModule: SerializersModule = EmptySerializersModule()
        }

        operator fun invoke(block: Builder.() -> Unit): BoomerangFormat {
            val builder = Builder().apply(block)
            return BoomerangFormat(
                serializersModule = builder.serializersModule,
            )
        }
    }
}

@Suppress("UnusedReceiverParameter")
var BoomerangConfig.format: BoomerangFormat
    get() = BoomerangFormat.INSTANCE
    set(value) {
        BoomerangFormat.INSTANCE = value
    }
