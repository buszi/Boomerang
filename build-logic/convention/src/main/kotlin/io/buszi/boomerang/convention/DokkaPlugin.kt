package io.buszi.boomerang.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension

class DokkaPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlin-dokka"))
        }

        extensions.configure<DokkaExtension> {
            dokkaSourceSets.named("main") {
                sourceLink {
                    remoteUrl("https://github.com/buszi/Boomerang")
                }
            }
        }

        with(dependencies) {
            add("dokkaPlugin", libs.findLibrary("kotlin-dokka-android").get())
        }
    }
}
