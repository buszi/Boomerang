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
            dokkaSourceSets.configureEach {
                sourceLink {
                    remoteUrl("https://github.com/buszi/Boomerang")
                }

                includes.from(
                    rootProject.file("CORE.md"),
                    rootProject.file("COMPOSE.md"),
                    rootProject.file("FRAGMENT.md"),
                )
            }
        }

        with(dependencies) {
            add("dokkaPlugin", libs.findLibrary("kotlin-dokka-android").get())
        }
    }
}
