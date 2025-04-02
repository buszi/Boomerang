package io.buszi.boomerang.core

import android.os.Bundle

fun interface BoomerangCatcher {
    fun tryCatch(value: Bundle): Boolean
}
