plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.library.orbit)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "in.koreatech.koin.feature.article"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
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
    implementation(projects.core.navigation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.m3)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.glide)

    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
