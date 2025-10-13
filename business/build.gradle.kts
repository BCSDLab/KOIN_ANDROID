

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.koin.application)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.orbit)
}

android {
    namespace = "in.koreatech.business"

    defaultConfig {
        applicationId = "in.koreatech.business"
        versionCode = rootProject.extra["versionBusinessCode"] as Int
        versionName = rootProject.extra["versionBusinessName"].toString()
        buildConfigField("String", "OKHTTP_VERSION", "\"${libs.versions.okhttp.get()}\"")
    }

    androidComponents {
        onVariants(selector().withBuildType("release")) {
            it.packaging.resources.excludes.add("META-INF/**")
        }
    }
    //  2 files found with path 'META-INF/gradle/incremental.annotation.processors' from inputs: 오류 해결

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(libs.guava)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.security.crypto)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.compose.numberPicker)
    implementation(libs.androidx.security.crypto)
    implementation(libs.compose.numberPicker)

    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.core)
    implementation(projects.core.designsystem)
}
