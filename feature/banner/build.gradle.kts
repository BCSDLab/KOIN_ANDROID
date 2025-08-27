plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "in.koreatech.koin.feature.banner"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
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
    implementation(libs.kotlinx.collections.immutable)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.m3)
    implementation(libs.coil.compose)
    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
