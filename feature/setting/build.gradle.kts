plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "in.koreatech.koin.feature.setting"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.m3)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.timber)
    implementation(libs.play.services.oss.licenses)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
