import gradleconfig.configureNaverMap
import gradleconfig.getStorePassword
import `in`.koreatech.buildsrc.AppConfig

plugins {
    id("koin.android.application")
    id("dagger.hilt.android.plugin")
    id("koin.android.firebase")
}

android {
    configureNaverMap(this)

    defaultConfig {
        applicationId = "in.koreatech.koin"
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
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
        }

        getByName("release") {
            isDebuggable = false
        }
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

    // https://github.com/irshuLx/Android-WYSIWYG-Editor
    implementation(libs.photoview)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
