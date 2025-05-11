plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.library.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.vanniktech.publish) apply false
    alias(libs.plugins.kotlin.dokka)
}

dokka {
    basePublicationsDirectory.set(project.file("docs"))
}

dependencies {
    dokka(projects.core)
    dokka(projects.compose)
    dokka(projects.fragment)
}
