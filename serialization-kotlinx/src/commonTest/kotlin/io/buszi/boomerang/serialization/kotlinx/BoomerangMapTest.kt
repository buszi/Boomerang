package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.emptyBoomerang
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BoomerangMapTest {

    @Serializable
    data class StringIntMap(val tags: Map<String, Int>)

    @Test
    fun roundtrip_map_of_string_to_int() {
        val original = StringIntMap(tags = mapOf("a" to 1, "b" to 2, "c" to 3))
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<StringIntMap>(boom)
        assertEquals(original, back)
    }

    @Test
    fun serialize_map_of_string_to_int_stores_alternating_list() {
        val original = StringIntMap(tags = mapOf("x" to 42))
        val boom = BoomerangFormat.serialize(original)

        val list = boom.getBoomerangsList("tags")
        assertNotNull(list)
        // Map entries are stored as alternating key/value pairs
        assertEquals(2, list.size)
        assertEquals("x", list[0].getString("v"))
        assertEquals(42, list[1].getInt("v"))
    }

    @Serializable
    data class StringStringMap(val headers: Map<String, String>)

    @Test
    fun roundtrip_map_of_string_to_string() {
        val original = StringStringMap(
            headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "text/html",
            )
        )
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<StringStringMap>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class StringLongMap(val ids: Map<String, Long>)

    @Test
    fun roundtrip_map_of_string_to_long() {
        val original = StringLongMap(ids = mapOf("user" to 123456789L, "session" to 987654321L))
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<StringLongMap>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class StringBoolMap(val flags: Map<String, Boolean>)

    @Test
    fun roundtrip_map_of_string_to_boolean() {
        val original = StringBoolMap(flags = mapOf("enabled" to true, "visible" to false))
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<StringBoolMap>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class StringDoubleMap(val scores: Map<String, Double>)

    @Test
    fun roundtrip_map_of_string_to_double() {
        val original = StringDoubleMap(scores = mapOf("math" to 95.5, "english" to 87.3))
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<StringDoubleMap>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class User(val id: Long, val name: String)

    @Serializable
    data class StringObjectMap(val users: Map<String, User>)

    @Test
    fun roundtrip_map_of_string_to_object() {
        val original = StringObjectMap(
            users = mapOf(
                "admin" to User(1L, "Alice"),
                "guest" to User(2L, "Bob"),
            )
        )
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<StringObjectMap>(boom)
        assertEquals(original, back)
    }

    @Test
    fun serialize_map_of_string_to_object_stores_alternating_list() {
        val original = StringObjectMap(
            users = mapOf("admin" to User(1L, "Alice"))
        )
        val boom = BoomerangFormat.serialize(original)

        val list = boom.getBoomerangsList("users")
        assertNotNull(list)
        // key (primitive) + value (object) = 2 entries
        assertEquals(2, list.size)
        // Key stored under "v"
        assertEquals("admin", list[0].getString("v"))
        // Object fields stored directly
        assertEquals(1L, list[1].getLong("id"))
        assertEquals("Alice", list[1].getString("name"))
    }

    @Serializable
    data class IntStringMap(val lookup: Map<Int, String>)

    @Test
    fun roundtrip_map_of_int_to_string() {
        val original = IntStringMap(lookup = mapOf(1 to "one", 2 to "two", 3 to "three"))
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<IntStringMap>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class LongBoolMap(val permissions: Map<Long, Boolean>)

    @Test
    fun roundtrip_map_of_long_to_boolean() {
        val original = LongBoolMap(permissions = mapOf(100L to true, 200L to false))
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<LongBoolMap>(boom)
        assertEquals(original, back)
    }

    enum class Priority { LOW, MEDIUM, HIGH }

    @Serializable
    data class EnumKeyMap(val labels: Map<Priority, String>)

    @Test
    fun roundtrip_map_with_enum_keys() {
        val original = EnumKeyMap(
            labels = mapOf(
                Priority.LOW to "Not urgent",
                Priority.HIGH to "Critical",
            )
        )
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<EnumKeyMap>(boom)
        assertEquals(original, back)
    }

    @Test
    fun roundtrip_empty_map() {
        val original = StringIntMap(tags = emptyMap())
        val boom = BoomerangFormat.serialize(original)

        val list = boom.getBoomerangsList("tags")
        assertNotNull(list)
        assertTrue(list.isEmpty())

        val back = BoomerangFormat.deserialize<StringIntMap>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class Config(val settings: Map<String, String>)

    @Serializable
    data class AppState(val config: Config, val version: Int)

    @Test
    fun roundtrip_map_in_nested_object() {
        val original = AppState(
            config = Config(settings = mapOf("theme" to "dark", "lang" to "en")),
            version = 3,
        )
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<AppState>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class MultiMap(
        val strings: Map<String, String>,
        val ints: Map<String, Int>,
    )

    @Test
    fun roundtrip_multiple_maps_in_same_object() {
        val original = MultiMap(
            strings = mapOf("key" to "value"),
            ints = mapOf("count" to 42),
        )
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<MultiMap>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class MixedProperties(
        val name: String,
        val tags: Map<String, Int>,
        val active: Boolean,
    )

    @Test
    fun roundtrip_map_alongside_other_properties() {
        val original = MixedProperties(
            name = "test",
            tags = mapOf("priority" to 1, "severity" to 5),
            active = true,
        )
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<MixedProperties>(boom)
        assertEquals(original, back)
    }

    @Test
    fun deserialize_map_from_manual_boomerang() {
        val key1 = emptyBoomerang().apply { putString("v", "hello") }
        val val1 = emptyBoomerang().apply { putInt("v", 100) }
        val key2 = emptyBoomerang().apply { putString("v", "world") }
        val val2 = emptyBoomerang().apply { putInt("v", 200) }

        val boom = emptyBoomerang().apply {
            putBoomerangsList("tags", listOf(key1, val1, key2, val2))
        }

        val back = BoomerangFormat.deserialize<StringIntMap>(boom)
        assertEquals(mapOf("hello" to 100, "world" to 200), back.tags)
    }
}
