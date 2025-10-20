package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.emptyBoomerang
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BoomerangCollectionsTest {

    @Serializable
    data class SomeResult(val ids: List<Long>)

    @Serializable
    data class Item(val id: Long)

    @Serializable
    data class Container(val items: List<Item>)

    @Test
    fun serialize_list_of_longs() {
        val value = SomeResult(ids = listOf(1L, 2L, 3L))

        val boom = BoomerangFormat.serialize(value)

        assertTrue(boom.contains("ids"))
        val list = boom.getBoomerangsList("ids")
        assertNotNull(list)
        assertEquals(3, list.size)
        assertEquals(1L, list[0].getLong("v"))
        assertEquals(2L, list[1].getLong("v"))
        assertEquals(3L, list[2].getLong("v"))
    }

    @Test
    fun deserialize_list_of_longs() {
        val elem1 = emptyBoomerang().apply { putLong("v", 10L) }
        val elem2 = emptyBoomerang().apply { putLong("v", 20L) }
        val elem3 = emptyBoomerang().apply { putLong("v", 30L) }

        val boom = emptyBoomerang().apply {
            putBoomerangsList("ids", listOf(elem1, elem2, elem3))
        }

        val decoded = BoomerangFormat.deserialize<SomeResult>(boom)
        assertEquals(listOf(10L, 20L, 30L), decoded.ids)
    }

    @Test
    fun roundtrip_list_of_strings() {
        @Serializable
        data class Names(val names: List<String>)

        val original = Names(listOf("Alice", "Bob", "Charlie"))
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<Names>(boom)
        assertEquals(original, back)

        // Ensure underlying representation stores "v" entries
        val list = boom.getBoomerangsList("names")
        assertNotNull(list)
        assertEquals("Alice", list[0].getString("v"))
        assertEquals("Bob", list[1].getString("v"))
        assertEquals("Charlie", list[2].getString("v"))
    }

    @Test
    fun roundtrip_list_of_objects() {
        val original = Container(items = listOf(Item(5L), Item(6L)))
        val boom = BoomerangFormat.serialize(original)
        val back = BoomerangFormat.deserialize<Container>(boom)
        assertEquals(original, back)

        // Representation: each element boomerang contains the object's fields (no "v" key)
        val list = boom.getBoomerangsList("items")
        assertNotNull(list)
        assertEquals(2, list.size)
        assertEquals(5L, list[0].getLong("id"))
        assertEquals(6L, list[1].getLong("id"))
    }

    @Test
    fun serialize_empty_list() {
        @Serializable
        data class Group(val ids: List<Int>)

        val boom = BoomerangFormat.serialize(Group(emptyList()))
        val list = boom.getBoomerangsList("ids")
        // We accept empty list being present as empty; representation detail may vary
        assertNotNull(list)
        assertTrue(list.isEmpty())

        val back = BoomerangFormat.deserialize<Group>(boom)
        assertEquals(emptyList(), back.ids)
    }
}
