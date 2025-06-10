plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.library.orbit)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.koin.library.paparazzi)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "in.koreatech.koin.feature.store"

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

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.compose.lifecycle)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material2)
    implementation(libs.bundles.compose.m3)
    implementation(libs.kotlinxCollectionsImmutable)

    implementation(libs.timber)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.coroutines.test)

    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.coil.compose)
    implementation(libs.toolbar.compose)
}
