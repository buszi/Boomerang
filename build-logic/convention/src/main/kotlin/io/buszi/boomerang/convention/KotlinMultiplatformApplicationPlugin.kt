package io.buszi.boomerang.convention

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class KotlinMultiplatformApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlin-multiplatform"))
            apply(libs.findPlugin("android-application"))
            apply(libs.findPlugin("kotlin-compose-multiplatform"))
        }

        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        (this as KotlinJvmCompile).compilerOptions.jvmTarget.set(JVM_TARGET)
                    }
                }
            }

            jvm("desktop")

            applyDefaultHierarchyTemplate()
            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64(),
            ).forEach { target ->
                target.binaries.framework {
                    baseName = "ComposeApp"
                    isStatic = true
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

            with(buildFeatures) {
                compose = true
                viewBinding = true
            }
        }

        extensions.configure<ComposeExtension> {
            extensions.configure<DesktopExtension> {
                application {
                    mainClass = "io.buszi.boomerang.MainKt"

                    nativeDistributions {
                        targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                        packageName = "Boomerang Desktop Preview"
                        packageVersion = "1.4.0"
                    }
                }
            }
        }
    }
}
