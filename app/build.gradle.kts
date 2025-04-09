plugins {
    id("plugins.android.application")
    id("plugins.android.compose")
}

android {
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.compose)
    implementation(projects.fragment)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.compose.ui)
    implementation(libs.compose.material3)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.compose.test)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.contrib)
}
