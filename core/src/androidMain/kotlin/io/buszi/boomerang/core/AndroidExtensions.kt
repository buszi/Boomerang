package io.buszi.boomerang.core

import android.os.Bundle

fun Bundle.toBoomerang(): AndroidBoomerang = AndroidBoomerang(this)

fun Boomerang.toBundle(): Bundle = (this as AndroidBoomerang).bundle
