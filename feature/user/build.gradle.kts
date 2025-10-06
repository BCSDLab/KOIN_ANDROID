plugins {
    alias(libs.plugins.koin.feature)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.koin.library.orbit)
}

android {
    namespace = "in.koreatech.koin.feature.user"

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
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
    implementation(libs.lottie.compose)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.timber)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
}
