package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.Boomerang
import kotlinx.serialization.serializer

open class BoomerangFormat {

    inline fun <reified T : Any> serialize(value: T): Boomerang {
        val encoder = RootBoomerangEncoder()
        encoder.encodeSerializableValue(serializer<T>(), value)
        return encoder.boomerang
    }

    companion object : BoomerangFormat()
}
