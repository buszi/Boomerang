package io.buszi.boomerang.core

import android.os.Bundle

interface BoomerangStore {

    fun getValue(key: String): Bundle?

    fun storeValue(key: String, value: Bundle)

    fun dropValue(key: String)

    fun tryCatch(key: String, catcher: BoomerangCatcher) {
        getValue(key)?.let { value ->
            val isCaught = catcher.tryCatch(value)
            if (isCaught) dropValue(key)
        }
    }
}
