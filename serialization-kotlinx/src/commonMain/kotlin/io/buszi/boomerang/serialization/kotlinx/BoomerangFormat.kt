package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.Boomerang
import kotlinx.serialization.serializer

open class BoomerangFormat {

    inline fun <reified T : Any> serialize(value: T): Boomerang {
        val encoder = RootBoomerangEncoder()
        encoder.encodeSerializableValue(serializer<T>(), value)
        return encoder.boomerang
    }

    inline fun <reified T : Any> deserialize(boomerang: Boomerang): T {
        val decoder = RootBoomerangDecoder(boomerang)
        return decoder.decodeSerializableValue(serializer<T>())
    }

    companion object : BoomerangFormat()
}
