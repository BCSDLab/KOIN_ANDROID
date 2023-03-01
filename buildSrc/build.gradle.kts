plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.firebase.crashlytics.gradle)
    implementation(libs.hilt.android.gradle.plugin)
}