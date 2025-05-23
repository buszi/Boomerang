@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("plugins.multiplatform.application")
    id("plugins.multiplatform.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.compose)

            implementation(libs.androidx.navigation.multiplatform)

            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }
        androidMain.dependencies {
            implementation(projects.fragment)

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
