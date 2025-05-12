package io.buszi.boomerang.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeMultiplatformPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlin-compose"))
            apply(libs.findPlugin("kotlin-compose-multiplatform"))
        }
    }
}
