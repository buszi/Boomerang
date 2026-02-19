package io.buszi.boomerang.core

import io.buszi.boomerang.core.BoomerangStore.Companion.EVENT_KEY
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultBoomerangStoreTest {

    @Test
    fun storeAndGetValue() {
        val store = DefaultBoomerangStore()
        val boomerang = buildBoomerang { putString("name", "Alice") }
        store.storeValue("result", boomerang)

        val retrieved = store.getValue("result")
        assertNotNull(retrieved)
        assertEquals("Alice", retrieved.getString("name"))
    }

    @Test
    fun getValueReturnsNullForMissingKey() {
        val store = DefaultBoomerangStore()
        assertNull(store.getValue("missing"))
    }

    @Test
    fun dropValueRemovesStoredEntry() {
        val store = DefaultBoomerangStore()
        store.storeValue("key", buildBoomerang { putInt("x", 1) })
        assertNotNull(store.getValue("key"))

        store.dropValue("key")
        assertNull(store.getValue("key"))
    }

    @Test
    fun dropValueOnMissingKeyIsNoOp() {
        val store = DefaultBoomerangStore()
        store.dropValue("missing") // should not throw
    }

    @Test
    fun storeEventCreatesBoomerangWithEventKey() {
        val store = DefaultBoomerangStore()
        store.storeEvent("my_event")

        val boomerang = store.getValue("my_event")
        assertNotNull(boomerang)
        assertEquals("my_event", boomerang.getString(EVENT_KEY))
        assertEquals(1, boomerang.getKeys().size)
    }

    @Test
    fun tryConsumeValueDropsWhenCatcherReturnsTrue() {
        val store = DefaultBoomerangStore()
        store.storeValue("key", buildBoomerang { putString("data", "value") })

        var caughtValue: String? = null
        store.tryConsumeValue("key", BoomerangCatcher { boomerang ->
            caughtValue = boomerang.getString("data")
            true
        })

        assertEquals("value", caughtValue)
        assertNull(store.getValue("key"))
    }

    @Test
    fun tryConsumeValueKeepsWhenCatcherReturnsFalse() {
        val store = DefaultBoomerangStore()
        store.storeValue("key", buildBoomerang { putString("data", "value") })

        store.tryConsumeValue("key", BoomerangCatcher { false })

        assertNotNull(store.getValue("key"))
    }

    @Test
    fun tryConsumeValueDoesNothingForMissingKey() {
        val store = DefaultBoomerangStore()
        var catcherCalled = false

        store.tryConsumeValue("missing", BoomerangCatcher {
            catcherCalled = true
            true
        })

        assertTrue(!catcherCalled)
    }

    @Test
    fun packAndRestoreStateRoundtrip() {
        val store = DefaultBoomerangStore()
        store.storeValue("a", buildBoomerang { putString("name", "Alice") })
        store.storeValue("b", buildBoomerang { putInt("age", 30) })

        val packed = store.packState()

        val restoredStore = DefaultBoomerangStore()
        restoredStore.restoreState(packed)

        val a = restoredStore.getValue("a")
        assertNotNull(a)
        assertEquals("Alice", a.getString("name"))

        val b = restoredStore.getValue("b")
        assertNotNull(b)
        assertEquals(30, b.getInt("age"))
    }

    @Test
    fun packStateOfEmptyStoreProducesEmptyBoomerang() {
        val store = DefaultBoomerangStore()
        val packed = store.packState()
        assertTrue(packed.getKeys().isEmpty())
    }

    @Test
    fun restoreStatePreservesExistingEntries() {
        val store = DefaultBoomerangStore()
        store.storeValue("existing", buildBoomerang { putString("x", "y") })

        val packed = buildBoomerang {
            putBoomerang("new", buildBoomerang { putString("a", "b") })
        }
        store.restoreState(packed)

        assertNotNull(store.getValue("existing"))
        assertNotNull(store.getValue("new"))
    }

    @Test
    fun storeValueOverwritesPreviousEntry() {
        val store = DefaultBoomerangStore()
        store.storeValue("key", buildBoomerang { putString("v", "first") })
        store.storeValue("key", buildBoomerang { putString("v", "second") })

        assertEquals("second", store.getValue("key")?.getString("v"))
    }
}
