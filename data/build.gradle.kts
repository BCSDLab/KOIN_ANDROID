plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
}

android {
    namespace = "in.koreatech.koin.data"

    defaultConfig {
        consumerProguardFiles ("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField("Boolean", "IS_DEBUG", "true")
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField("Boolean", "IS_DEBUG", "false")
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    /* Dependency - androidx */
    implementation(libs.core.ktx)

    /* Dependency - kotlin */
    implementation(libs.kotlinx.coroutines.android)

    /* Dependency - Retrofit2 */
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.rxjava2)

    /* Dependency - okhttp */
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.androidx.security.crypto)
}
