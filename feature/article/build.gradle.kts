plugins {
    alias(libs.plugins.koin.feature)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.library.orbit)
}

android {
    namespace = "in.koreatech.koin.feature.article"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.onboarding)
    implementation(projects.core.designsystem)
    implementation(projects.core.analytics)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.timber)
}
