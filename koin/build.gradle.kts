import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.koin.compose)
    alias(libs.plugins.koin.application)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.firebase)
    alias(libs.plugins.koin.oss.license)
    id("com.google.gms.google-services")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "in.koreatech.koin"

    defaultConfig {
        applicationId = "in.koreatech.koin"
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"].toString()
        manifestPlaceholders["naverMapKey"] = getPropertyKey("navermap_key")
        manifestPlaceholders["kakaoScheme"] = "kakao" + getPropertyKey("kakao_native_app_key")
    }

    signingConfigs {
        create("release") {
            storeFile = file("./team_kap_android.jks")
            storePassword = getPropertyKey("koin_store_password")
            keyAlias = "koin_release_key"
            keyPassword = getPropertyKey("koin_store_password")
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".dev"
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["appName"] = "@string/app_name_dev"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_koin"
            manifestPlaceholders["appLinkUri"] = "stage.koreatech.in"
            buildConfigField("Boolean", "IS_DEBUG", "true")
            buildConfigField(
                "String",
                "KAKAO_NATIVE_APP_KEY",
                "String.valueOf(\"${localProperties["kakao_native_app_key"]}\")"
            )
            buildConfigField("String", "OKHTTP_VERSION", "\"${libs.versions.okhttpVersion.get()}\"")
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["appName"] = "@string/app_name"
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_koin"
            manifestPlaceholders["appLinkUri"] = "koreatech.in"
            buildConfigField("Boolean", "IS_DEBUG", "false")
            signingConfig = signingConfigs.getByName("release")
            buildConfigField(
                "String",
                "KAKAO_NATIVE_APP_KEY",
                "String.valueOf(\"${localProperties["kakao_native_app_key"]}\")"
            )
            buildConfigField("String", "OKHTTP_VERSION", "\"${libs.versions.okhttpVersion.get()}\"")
            firebaseAppDistribution {
                artifactType = "AAB"
                releaseNotes = "${rootProject.extra["versionName"]} release"
                groups = "bcsd"
            }
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

fun getPropertyKey(propertyKey: String): String {
    val nullableProperty: String? =
        gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
    return nullableProperty ?: "null"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":core"))
    implementation(project(":core:notification"))
    implementation(project(":core:navigation"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:analytics"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core:onboarding"))
    implementation(project(":feature:timetable"))
    implementation(project(":feature:bus"))
    implementation(project(":feature:lostandfound"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:banner"))
    implementation(project(":feature:club"))

    implementation(libs.guava)

    // Dependency - glide & coil
    implementation(libs.glide)
    implementation(libs.coil)
    ksp(libs.glide.ksp)

    // Dependency - naver api
    implementation(libs.map.sdk)

    // Dependency -google play core
    implementation(libs.inApp.update)
    implementation(libs.inApp.update.ktx)
    implementation(libs.feature.delivery.ktx)

    implementation(libs.colorpicker)
    implementation(libs.photoview)

    implementation(libs.markerman.roundedImageView)
    implementation(libs.powerSpinner)
    implementation(libs.viewpager2)

    implementation(libs.kakao.share)
    implementation(libs.lottie)
    implementation(libs.dataStore)

    implementation(libs.nav.fragment.ktx)
    implementation(libs.nav.ui.ktx)
    implementation(libs.nav.dynamic.features.fragment)

    implementation(libs.feature.delivery.ktx)

    implementation(libs.timber)

    implementation(libs.firebase.crashlytics)

    implementation(libs.compose.lifecycle)
    implementation(libs.kotlinxCollectionsImmutable)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.m3)
}
