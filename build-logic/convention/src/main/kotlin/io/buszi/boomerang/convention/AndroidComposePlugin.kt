package io.buszi.boomerang.convention

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlin-compose"))
        }

        with(extensions.getByType<BaseExtension>()) {
            with(buildFeatures) {
                compose = true
            }
        }

        with(dependencies) {
            add("implementation", platform(libs.findLibrary("compose-bom").get()))
        }
    }
}
