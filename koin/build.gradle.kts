import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.koin.compose)
    alias(libs.plugins.koin.application)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.firebase)
    id("com.google.gms.google-services")
}

android {
    namespace = "in.koreatech.koin"

    defaultConfig {
        applicationId = "in.koreatech.koin"
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"].toString()
        manifestPlaceholders["naverMapKey"] = getPropertyKey("navermap_key")
    }

    signingConfigs {
        create("release") {
            storeFile = file("./team_kap_android.jks")
            storePassword = getPropertyKey("koin_store_password")
            keyAlias = getPropertyKey("koin_release_key")
            keyPassword = getPropertyKey("koin_store_password")
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField("Boolean", "IS_DEBUG", "true")
            firebaseCrashlytics {
                mappingFileUploadEnabled = false
            }
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField("Boolean", "IS_DEBUG", "false")
            signingConfig = signingConfigs.getByName("release")
            firebaseAppDistribution {
                artifactType = "AAB"
                releaseNotes = "${rootProject.extra["versionName"]} release"
                groups = "bcsd"
            }
        }
    }
    buildFeatures {
        dataBinding = true
    }
}

fun getPropertyKey(propertyKey: String): String {
    val nullableProperty: String? =
        gradleLocalProperties(rootDir).getProperty(propertyKey)
    return nullableProperty ?: "null"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.guava)

    /* Dependency - glide & coil */
    implementation(libs.glide)
    implementation(libs.coil)
    kapt(libs.glide.compiler)

    /* Dependency - butterknife api */
    implementation(libs.butterknife)
    kapt(libs.butterknife.compiler)

    /* Dependency - naver api */
    implementation(libs.map.sdk)

    // https://github.com/irshuLx/Android-WYSIWYG-Editor
    implementation(libs.laser.native.editor)
    implementation(libs.colorpicker)
    implementation(libs.photoview)

    implementation(libs.markerman.roundedImageView)
    implementation(libs.powerSpinner)
    implementation(libs.viewpager2)
    implementation(libs.napier)
}
