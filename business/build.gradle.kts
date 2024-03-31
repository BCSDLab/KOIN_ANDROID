@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.koin.compose)
    alias(libs.plugins.koin.application)
}

android {
    namespace = "in.koreatech.business"


    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(project(":core"))
    implementation(libs.orbit.compose)
    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
}
