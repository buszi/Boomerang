package io.buszi.boomerang.convention

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class KotlinMultiplatformApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlin-multiplatform"))
            apply(libs.findPlugin("android-application"))
        }

        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        (this as KotlinJvmCompile).compilerOptions.jvmTarget.set(JVM_TARGET)
                    }
                }
            }
        }

        extensions.configure<AppExtension> {
            namespace = "io.buszi.boomerang"
            compileSdkVersion = "android-" + libs.findVersionString("compileSdk")

            with(defaultConfig) {
                applicationId = "io.buszi.boomerang"
                minSdk = libs.findVersionInt("minSdk")
                targetSdk = libs.findVersionInt("targetSdk")
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            with(compileOptions) {
                sourceCompatibility = JDK_VERSION
                targetCompatibility = JDK_VERSION
            }
        }
    }
}
