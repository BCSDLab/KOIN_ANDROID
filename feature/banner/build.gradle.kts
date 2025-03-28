plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.library.orbit)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "in.koreatech.koin.feature.banner"

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":core:onboarding"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:analytics"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.m3)
    implementation(libs.coil.compose)
    implementation(libs.timber)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.nav.fragment.ktx)
    implementation(libs.nav.ui.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}