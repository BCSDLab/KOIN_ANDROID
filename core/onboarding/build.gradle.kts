plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
}

android {
    namespace = "in.koreatech.koin.core.onboarding"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.m3)
    implementation(libs.timber)
    implementation(libs.balloon)
    implementation(libs.balloon.compose)
}
