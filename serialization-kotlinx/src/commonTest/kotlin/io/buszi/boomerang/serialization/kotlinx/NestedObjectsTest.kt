package io.buszi.boomerang.serialization.kotlinx

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class NestedObjectsTest {

    @Serializable
    data class User(val id: Long)

    @Serializable
    data class SomeResult(val user: User)

    @Test
    fun encodeAndDecodeNestedObjects() {
        val value = SomeResult(User(42))

        val encoded = BoomerangFormat.serialize(value)

        // Ensure nested structure stored under key "user"
        // The nested Boomerang should have key "id" -> 42
        val userBoomerang = encoded.getBoomerang("user")
        requireNotNull(userBoomerang)
        assertEquals(42L, userBoomerang.getLong("id"))

        val decoded = BoomerangFormat.deserialize<SomeResult>(encoded)
        assertEquals(value, decoded)
    }
}
