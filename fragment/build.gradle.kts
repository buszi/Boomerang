plugins {
    id("plugins.android.library")
}

android {
    namespace = "io.buszi.boomerang.fragment"
}

dependencies {
    implementation(projects.core)

    implementation(libs.androidx.fragment.core)
    implementation(libs.androidx.lifecycle.runtime)
}
