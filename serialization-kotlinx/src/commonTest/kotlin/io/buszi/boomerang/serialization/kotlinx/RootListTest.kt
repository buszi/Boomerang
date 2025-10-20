package io.buszi.boomerang.serialization.kotlinx

import io.buszi.boomerang.core.emptyBoomerang
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RootListTest {

    @Test
    fun roundtrip_root_list_of_ints() {
        val original = listOf(1, 2, 3)
        val boom = BoomerangFormat.serialize(original)
        // Ensure representation is under reserved key
        val list = boom.getBoomerangsList(ROOT_LIST_KEY)
        assertNotNull(list)
        assertEquals(3, list.size)
        assertEquals(1, list[0].getInt("v"))
        assertEquals(2, list[1].getInt("v"))
        assertEquals(3, list[2].getInt("v"))

        val back = BoomerangFormat.deserialize<List<Int>>(boom)
        assertEquals(original, back)
    }

    @Serializable
    data class User(val id: Int)

    @Test
    fun roundtrip_root_list_of_objects() {
        val original = listOf(User(1), User(2))
        val boom = BoomerangFormat.serialize(original)
        // Representation should store objects without "v" key; fields are directly inside each element boomerang
        val list = boom.getBoomerangsList(ROOT_LIST_KEY)
        assertNotNull(list)
        assertEquals(2, list.size)
        assertEquals(1, list[0].getInt("id"))
        assertEquals(2, list[1].getInt("id"))

        val back = BoomerangFormat.deserialize<List<User>>(boom)
        assertEquals(original, back)
    }

    @Test
    fun deserialize_root_list_from_manual_boomerang() {
        val e1 = emptyBoomerang().apply { putInt("v", 10) }
        val e2 = emptyBoomerang().apply { putInt("v", 20) }
        val boom = emptyBoomerang().apply { putBoomerangsList(ROOT_LIST_KEY, listOf(e1, e2)) }

        val back = BoomerangFormat.deserialize<List<Int>>(boom)
        assertEquals(listOf(10, 20), back)
    }
}
