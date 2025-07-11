plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.library.orbit)
    alias(libs.plugins.koin.library.paparazzi)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "in.koreatech.koin.feature.timetable"

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
    implementation(project(":core:designsystem"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material2)
    implementation(libs.bundles.compose.m3)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.timber)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
