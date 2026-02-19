package io.buszi.boomerang.core

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BoomerangConfigTest {

    @AfterTest
    fun tearDown() {
        BoomerangConfig.logger = null
    }

    @Test
    fun loggerDefaultsToNull() {
        assertNull(BoomerangConfig.logger)
    }

    @Test
    fun loggerCanBeSet() {
        val messages = mutableListOf<String>()
        BoomerangConfig.logger = object : BoomerangLogger {
            override fun log(tag: String, message: String) {
                messages.add("$tag: $message")
            }
        }

        BoomerangConfig.logger?.log("Test", "hello")
        assertEquals(1, messages.size)
        assertEquals("Test: hello", messages[0])
    }

    @Test
    fun invokeOperatorConfiguresBlock() {
        val messages = mutableListOf<String>()
        BoomerangConfig {
            logger = object : BoomerangLogger {
                override fun log(tag: String, message: String) {
                    messages.add("$tag: $message")
                }
            }
        }

        BoomerangConfig.logger?.log("Tag", "msg")
        assertEquals("Tag: msg", messages[0])
    }

    @Test
    fun printLoggerWritesFormattedOutput() {
        val logger = BoomerangLogger.PRINT_LOGGER
        // Just verify it doesn't throw
        logger.log("TestTag", "TestMessage")
    }
}
