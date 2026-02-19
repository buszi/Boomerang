package io.buszi.boomerang.core

import io.buszi.boomerang.core.BoomerangStore.Companion.EVENT_KEY
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EventBoomerangCatcherTest {

    @Test
    fun catchesMatchingEvent() {
        var eventFired = false
        val catcher = eventBoomerangCatcher("my_event") { eventFired = true }

        val boomerang = buildBoomerang { putString(EVENT_KEY, "my_event") }
        val caught = catcher.tryCatch(boomerang)

        assertTrue(caught)
        assertTrue(eventFired)
    }

    @Test
    fun doesNotCatchNonMatchingEventKey() {
        var eventFired = false
        val catcher = eventBoomerangCatcher("my_event") { eventFired = true }

        val boomerang = buildBoomerang { putString(EVENT_KEY, "other_event") }
        val caught = catcher.tryCatch(boomerang)

        assertFalse(caught)
        assertFalse(eventFired)
    }

    @Test
    fun doesNotCatchBoomerangWithExtraKeys() {
        var eventFired = false
        val catcher = eventBoomerangCatcher("my_event") { eventFired = true }

        val boomerang = buildBoomerang {
            putString(EVENT_KEY, "my_event")
            putString("extra", "data")
        }
        val caught = catcher.tryCatch(boomerang)

        assertFalse(caught)
        assertFalse(eventFired)
    }

    @Test
    fun doesNotCatchBoomerangWithoutEventKey() {
        var eventFired = false
        val catcher = eventBoomerangCatcher("my_event") { eventFired = true }

        val boomerang = buildBoomerang { putString("name", "Alice") }
        val caught = catcher.tryCatch(boomerang)

        assertFalse(caught)
        assertFalse(eventFired)
    }

    @Test
    fun doesNotCatchEmptyBoomerang() {
        var eventFired = false
        val catcher = eventBoomerangCatcher("my_event") { eventFired = true }

        val boomerang = emptyBoomerang()
        val caught = catcher.tryCatch(boomerang)

        assertFalse(caught)
        assertFalse(eventFired)
    }
}
