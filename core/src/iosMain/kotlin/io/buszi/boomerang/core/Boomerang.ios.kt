package io.buszi.boomerang.core

actual object BoomerangFactory {
    actual fun create(): Boomerang = MapBoomerang()
}
