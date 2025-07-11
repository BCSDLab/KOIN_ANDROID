plugins {
    alias(libs.plugins.koin.library)
}

android {
    namespace = "in.koreatech.koin.core.designsystem"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.lottie.compose.v660)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.m3)

    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.compose.ui.test.manifest)
}
