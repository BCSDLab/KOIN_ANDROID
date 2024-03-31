import `in`.koreatech.convention.kapt

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.koin.compose)
    alias(libs.plugins.koin.application)
    alias(libs.plugins.hilt)
}

android {
    namespace = "in.koreatech.business"

    packagingOptions {
        resources.pickFirsts.add("META-INF/gradle/incremental.annotation.processors")
    }
    //  2 files found with path 'META-INF/gradle/incremental.annotation.processors' from inputs: 오류 해결

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
    implementation(libs.compose.navigation)
    implementation(libs.orbit.compose)
    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    kapt(libs.hilt.compiler)
    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":data")))
    implementation(project(mapOf("path" to ":core")))
}
