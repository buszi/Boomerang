package io.buszi.boomerang.convention

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

@Suppress("UnstableApiUsage")
class KotlinMultiplatformLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlin-multiplatform"))
            apply(libs.findPlugin("android-library-multiplatform"))
        }

        extensions.configure<KotlinMultiplatformExtension> {
            androidLibrary {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        (this as KotlinJvmCompile).compilerOptions.jvmTarget.set(JVM_TARGET)
                    }
                }
                compileSdk = libs.findVersionInt("compileSdk")
                minSdk = libs.findVersionInt("minSdk")
            }

            jvm("desktop")

            applyDefaultHierarchyTemplate()
            iosX64()
            iosArm64()
            iosSimulatorArm64()
        }
    }
}
