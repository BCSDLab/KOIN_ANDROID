plugins {
    alias(libs.plugins.koin.feature)
    alias(libs.plugins.koin.hilt)
}

android {
    namespace = "in.koreatech.koin.feature.banner"
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
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.coil.compose)
    implementation(libs.timber)
}
