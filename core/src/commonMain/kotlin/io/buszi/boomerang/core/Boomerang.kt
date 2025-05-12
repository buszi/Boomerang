package io.buszi.boomerang.core

interface Boomerang {

    fun putString(key: String, value: String)
    fun getString(key: String): String?

    fun putInt(key: String, value: Int)
    fun getInt(key: String): Int?

    fun putLong(key: String, value: Long)
    fun getLong(key: String): Long?

    fun putFloat(key: String, value: Float)
    fun getFloat(key: String): Float?

    fun putDouble(key: String, value: Double)
    fun getDouble(key: String): Double?

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean?

    fun contains(key: String): Boolean
    fun remove(key: String)
    fun clear()
}

expect object BoomerangFactory {
    fun create(): Boomerang
}

inline fun buildBoomerang(builder: Boomerang.() -> Unit): Boomerang = BoomerangFactory.create().apply(builder)

fun boomerangOf(vararg pairs: Pair<String, Any>): Boomerang = buildBoomerang {
    pairs.forEach { (key, value) ->
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> putDouble(key, value)
            is Boolean -> putBoolean(key, value)
            else -> error("Unsupported type: ${value::class.simpleName}")
        }
    }
}
