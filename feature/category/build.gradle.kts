plugins {
    alias(libs.plugins.koin.feature)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.library.orbit)
}

android {
    namespace = "in.koreatech.koin.feature.category"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.analytics)
    implementation(projects.core.navigation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.timber)
}
