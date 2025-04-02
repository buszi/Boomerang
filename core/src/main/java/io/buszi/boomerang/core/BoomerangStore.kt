package io.buszi.boomerang.core

import android.os.Bundle

interface BoomerangStore {

    fun tryConsumeValue(key: String): Bundle?

    fun storeValue(key: String, value: Bundle)

    fun clearValue(key: String)
}
