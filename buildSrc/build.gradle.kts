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
    implementation(libs.firebase.appdistribution.gradle)
    implementation(libs.hilt.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "koin.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "koin.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidHilt") {
            id = "koin.android.hilt"
            implementationClass = "AndroidHiltPlugin"
        }
        register("androidFirebase") {
            id = "koin.android.firebase"
            implementationClass = "AndroidFirebasePlugin"
        }
    }
}