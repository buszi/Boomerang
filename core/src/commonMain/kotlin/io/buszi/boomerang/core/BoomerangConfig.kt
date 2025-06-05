package io.buszi.boomerang.core

/**
 * Configuration object for the Boomerang library.
 * 
 * This object provides global configuration options for the Boomerang library.
 * It allows applications to customize the behavior of the library across all modules.
 */
object BoomerangConfig {

    /**
     * The logger instance to use for logging Boomerang operations.
     * 
     * By default, this is null, which means logging is disabled. To enable logging,
     * set this property to an instance of [BoomerangLogger].
     * 
     * Example usage:
     * ```
     * // For Android:
     * BoomerangConfig.logger = AndroidBoomerangLogger(LogLevel.DEBUG)
     * 
     * // For other platforms or simple console logging:
     * BoomerangConfig.logger = BoomerangLogger.PRINT_LOGGER
     * 
     * // To disable logging:
     * BoomerangConfig.logger = null
     * ```
     * 
     * When set, the logger will receive log messages about key operations like
     * storing and retrieving values, which can be helpful for debugging.
     */
    var logger: BoomerangLogger? = null
}
