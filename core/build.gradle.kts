plugins {
    alias(libs.plugins.koin.feature)
    alias(libs.plugins.koin.hilt)
}

android {
    namespace = "in.koreatech.koin.core"

    buildTypes {
        getByName("debug") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "IS_DEBUG", "true")
        }

        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "IS_DEBUG", "false")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.datastore.preferences)

    // Dependency - AndroidX Jetpack
    api(libs.androidx.appcompat)
    api(libs.androidx.recyclerview)
    api(libs.androidx.cardview)
    api(libs.material)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.fragment.ktx)
    api(libs.androidx.constraintlayout)
    implementation(platform(libs.androidx.compose.bom))

    // Dependency - kotlin
    api(libs.kotlin.stdlib)
    api(libs.kotlinx.coroutines.android)

    // Dependency - Retrofit2
    api(libs.retrofit)
    api(libs.retrofit.converter.gson)
    api(libs.retrofit.rxjava2)

    // Dependency - okhttp
    api(libs.okhttp)
    api(libs.okhttp.logging.interceptor)

    // Dependency - leakcanary
    debugImplementation(libs.leakcanary.android)

    // Dependency - glide & coil
    implementation(libs.glide)
    ksp(libs.glide.ksp)
    implementation(libs.coil)
    implementation(libs.coil.gif)

    // Dependency - sticky scroll view
    api(libs.stickyScrollView)

    // Dependency - firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)

    api(libs.rxjava.android)
    api(libs.rxjava)
    api(libs.javax.inject)
    api(libs.jsr250.api)
    api(libs.materialtabstrip.library)
    api(libs.jetbrains.annotations)

    implementation(libs.photoview)
    testImplementation(libs.junit)
}
