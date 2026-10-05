plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "in.koreatech.koin.core.camera"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.m3)
    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.compose)
}

kover {
    reports {
        filters {
            excludes {
                classes("*")
            }
        }
    }
}
