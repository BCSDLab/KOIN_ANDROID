import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.koin.feature)
    alias(libs.plugins.koin.hilt)
}

android {
    namespace = "in.koreatech.koin.feature.dining"

    defaultConfig {
        manifestPlaceholders["kakaoScheme"] = "kakao" + getPropertyKey("kakao_native_app_key")
    }
}

fun getPropertyKey(propertyKey: String): String {
    val nullableProperty: String? =
        gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
    return nullableProperty ?: "null"
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.analytics)
    implementation(projects.core.navigation)
    implementation(projects.core.onboarding)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.kakao.share)

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.timber)
}
