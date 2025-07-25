@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("plugins.multiplatform.application")
    id("plugins.multiplatform.compose")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.compose)
            implementation(projects.serializationKotlinx)
            implementation(projects.composeSerializationKotlinx)

            implementation(libs.androidx.navigation.multiplatform)

            implementation(libs.kotlin.serialization.core)

            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }
        androidMain.dependencies {
            implementation(projects.fragment)
            implementation(projects.fragmentSerializationKotlinx)

            implementation(libs.androidx.activity.compose)

            implementation(libs.androidx.navigation.fragment)
            implementation(libs.androidx.navigation.ui)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.junit)
            implementation(libs.androidx.test.core)
            implementation(libs.androidx.test.runner)
            implementation(compose.uiTest)
            implementation(compose.uiTestJUnit4)
            implementation(libs.espresso.core)
            implementation(libs.espresso.contrib)
        }
        getByName("desktopMain").dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}
