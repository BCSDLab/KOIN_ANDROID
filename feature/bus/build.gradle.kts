plugins {
    alias(libs.plugins.koin.feature)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "in.koreatech.koin.feature.bus"
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

    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.lottie.compose)

    implementation(libs.timber)
}
