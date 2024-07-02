

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.koin.compose)
    alias(libs.plugins.koin.application)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.orbit)
}

android {
    namespace = "in.koreatech.business"

    androidComponents {
        onVariants(selector().withBuildType("release")) {
            it.packaging.resources.excludes.add("META-INF/**")
        }
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
    implementation(libs.bundles.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.compose.navigation)
    implementation(libs.androidx.security.crypto)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.compose.numberPicker)
    implementation(libs.androidx.security.crypto)
    implementation(libs.compose.numberPicker)
    implementation(project(mapOf("path" to ":domain")))
    implementation(project(mapOf("path" to ":data")))
    implementation(project(mapOf("path" to ":core")))
}
