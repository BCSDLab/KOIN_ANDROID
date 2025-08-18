import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "in.koreatech.koin.feature.dining"

    defaultConfig {
        manifestPlaceholders["kakaoScheme"] = "kakao" + getPropertyKey("kakao_native_app_key")
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
    }
}

fun getPropertyKey(propertyKey: String): String {
    val nullableProperty: String? =
        gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
    return nullableProperty ?: "null"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:analytics"))
    implementation(project(":core:navigation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.kakao.share)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.m3)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.timber)
    implementation(project(":core:onboarding"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}