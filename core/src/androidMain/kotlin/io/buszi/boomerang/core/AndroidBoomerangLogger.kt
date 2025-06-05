package io.buszi.boomerang.core

import android.util.Log

/**
 * Defines the available log levels for Android logging.
 * 
 * These levels correspond to the standard Android logging levels and determine
 * the severity of log messages when using [AndroidBoomerangLogger].
 */
enum class LogLevel {
    /** Verbose level for detailed logging (lowest priority) */
    VERBOSE,

    /** Debug level for debugging information */
    DEBUG,

    /** Info level for general information */
    INFO,

    /** Warning level for potential issues */
    WARN,

    /** Error level for errors (highest priority) */
    ERROR
}

/**
 * Android-specific implementation of [BoomerangLogger] that uses Android's Log utility.
 * 
 * This logger integrates with Android's built-in logging system, allowing Boomerang
 * log messages to appear in Logcat with the appropriate level and tag.
 * 
 * @property level The log level to use for messages, defaults to [LogLevel.DEBUG]
 */
class AndroidBoomerangLogger(
    private val level: LogLevel = LogLevel.DEBUG
) : BoomerangLogger {

    /**
     * Logs a message with the specified tag using Android's Log utility.
     * 
     * The message will be logged at the level specified in the constructor.
     * 
     * @param tag The tag to identify the source of the log message
     * @param message The message to be logged
     */
    override fun log(tag: String, message: String) {
        when (level) {
            LogLevel.VERBOSE -> Log.v(tag, message)
            LogLevel.DEBUG -> Log.d(tag, message)
            LogLevel.INFO -> Log.i(tag, message)
            LogLevel.WARN -> Log.w(tag, message)
            LogLevel.ERROR -> Log.e(tag, message)
        }
    }
}
