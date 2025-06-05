package io.buszi.boomerang.core

/**
 * Interface for logging operations within the Boomerang library.
 * 
 * This interface provides a simple logging mechanism that can be implemented
 * by applications to integrate with their preferred logging system. The library
 * uses this interface to log important operations like storing and retrieving values.
 * 
 * Logging is completely optional and disabled by default. To enable logging,
 * set a logger implementation in [BoomerangConfig.logger].
 */
interface BoomerangLogger {

    /**
     * Logs a message with the specified tag.
     * 
     * @param tag The tag to identify the source of the log message
     * @param message The message to be logged
     */
    fun log(tag: String, message: String)

    companion object {
        /**
         * A simple implementation that prints log messages to the standard output.
         * 
         * This implementation is useful for debugging in environments where
         * more sophisticated logging systems are not available.
         */
        val PRINT_LOGGER: BoomerangLogger = object : BoomerangLogger {
            override fun log(tag: String, message: String) {
                println("$tag: $message")
            }
        }
    }
}
