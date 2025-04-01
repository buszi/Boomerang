package io.buszi.boomerang.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Optional

val JDK_VERSION = JavaVersion.VERSION_17
val JVM_TARGET = JvmTarget.JVM_17

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.findVersionString(name: String): String =
    findVersion(name).get().requiredVersion

fun VersionCatalog.findVersionInt(name: String): Int =
    findVersion(name).get().requiredVersion.toInt()

fun PluginManager.apply(plugin: Optional<Provider<PluginDependency>>) {
    apply(plugin.get().get().pluginId)
}
