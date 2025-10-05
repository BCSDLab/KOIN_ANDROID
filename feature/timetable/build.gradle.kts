plugins {
    alias(libs.plugins.koin.feature)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.library.orbit)
    alias(libs.plugins.koin.library.paparazzi)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "in.koreatech.koin.feature.timetable"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.analytics)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.bundles.compose)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.timber)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
