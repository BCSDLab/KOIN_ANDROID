import `in`.koreatech.buildsrc.AppConfig

plugins {
    id("koin.android.library")
    id("koin.android.hilt")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
}

android {
    compileSdk = AppConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AppConfig.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField ("boolean", "IS_DEBUG", "true")
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField ("boolean", "IS_DEBUG", "false")
        }
    }

    /* guava */
    /* configurations.all {
        resolutionStrategy.force = "com.google.code.findbugs:jsr305:1.3.9"
    } */

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources.excludes += "DebugProbesKt.bin"
    }
}

dependencies {

    api(libs.kotlin.stdlib)
    api(libs.androidx.core.ktx)

    api(libs.androidx.appcompat)
    api(libs.androidx.recyclerview)
    api(libs.androidx.cardview)
    api(libs.androidx.material)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.fragment.ktx)

    api(libs.firebase.core)
    api(libs.firebase.database)
    api(libs.firebase.perf)

    api(libs.retrofit)
    api(libs.retrofit.gson.converter)
    api(libs.retrofit.rxjava2)

    debugImplementation(libs.leakcanary.debug)

    api(libs.glide)
    kapt(libs.glide.compiler)

    api(libs.butterknife)
    kapt(libs.butterknife.compiler)

    api(libs.okhttp)
    api(libs.okhttp.logging)

    api(libs.stickyscrollview)

    api(libs.kotlinx.coroutines.android)

    api("androidx.constraintlayout:constraintlayout:1.1.3")
    api("io.reactivex.rxjava2:rxandroid:2.0.1")
    api("io.reactivex.rxjava2:rxjava:2.1.2")
    api("javax.inject:javax.inject:1")
    api("javax.annotation:jsr250-api:1.0")
    api("com.jpardogo.materialtabstrip:library:1.1.1")
    testImplementation("junit:junit:4.12")
    api("org.jetbrains:annotations:15.0")
    api("com.google.firebase:firebase-crashlytics:17.3.1")
    api("com.google.firebase:firebase-analytics:18.0.2")
}