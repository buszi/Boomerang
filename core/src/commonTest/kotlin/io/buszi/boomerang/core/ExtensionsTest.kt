package io.buszi.boomerang.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ExtensionsTest {

    @Test
    fun storeValueWithBuilderCreatesAndStoresBoomerang() {
        val store = DefaultBoomerangStore()
        store.storeValue("key") {
            putString("name", "Alice")
            putInt("age", 25)
        }

        val result = store.getValue("key")
        assertNotNull(result)
        assertEquals("Alice", result.getString("name"))
        assertEquals(25, result.getInt("age"))
    }

    @Test
    fun storeValueWithBuilderOverwritesPreviousValue() {
        val store = DefaultBoomerangStore()
        store.storeValue("key") { putString("v", "first") }
        store.storeValue("key") { putString("v", "second") }

        assertEquals("second", store.getValue("key")?.getString("v"))
    }
}
