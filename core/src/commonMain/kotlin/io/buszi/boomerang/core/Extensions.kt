package io.buszi.boomerang.core

inline fun BoomerangStore.storeValue(key: String, resultBuilder: Boomerang.() -> Unit) {
    storeValue(key, buildBoomerang(resultBuilder))
}
