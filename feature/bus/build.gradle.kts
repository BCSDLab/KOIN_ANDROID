plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "in.koreatech.koin.feature.bus"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":core:onboarding"))
    implementation(project(":core:designsystem"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.kotlinxCollectionsImmutable)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.m3)

    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.compose.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation(libs.kotlinx.serialization.json)
}