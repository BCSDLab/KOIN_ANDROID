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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.m3)

    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
}
