import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import gradleconfig.configureNaverMap
import gradleconfig.getStorePassword
import `in`.koreatech.buildsrc.AppConfig

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
}

val applicationName = "koin"

android {
    configureNaverMap(this)

    compileSdk = AppConfig.COMPILE_SDK

    defaultConfig {
        applicationId = "in.koreatech.koin"
        minSdk = AppConfig.MIN_SDK
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }

    signingConfigs {
        create("release") {
            storeFile = file("./team_kap_android.jks")
            storePassword = getStorePassword()
            keyAlias = "koin_release_key"
            keyPassword = getStorePassword()
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "IS_DEBUG", "true")
            the<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension>().mappingFileUploadEnabled = false
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "IS_DEBUG", "false")
            signingConfig = signingConfigs.getByName("release")
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotes = "3.0.7 release"
                groups = "bcsd"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation(group = "com.google.guava", name = "guava", version = "27.0.1-android") {
        exclude(group = "com.google.guava", module = "failureaccess")
    }

    /* Dependency - glide */
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    /* Dependency - butterknife */
    implementation(libs.butterknife)
    kapt(libs.butterknife.compiler)

    //naver map
    implementation(libs.naver.map.sdk)

    // https://github.com/irshuLx/Android-WYSIWYG-Editor
    implementation(libs.photoview)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
