import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
    alias(libs.plugins.koin.library.orbit)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.koin.library.paparazzi)
    id("kotlin-parcelize")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "in.koreatech.koin.feature.store"

    buildTypes {
        getByName("debug") {
            buildConfigField(
                "String",
                "ORDER_BASE_URL",
                "String.valueOf(\"${localProperties["order_stage_base_url"]}\")"
            )
        }

        getByName("release") {
            buildConfigField(
                "String",
                "ORDER_BASE_URL",
                "String.valueOf(\"${localProperties["order_base_url"]}\")"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core:webapp"))
    implementation(project(":domain"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material2)
    implementation(libs.bundles.compose.m3)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.timber)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    debugImplementation(libs.bundles.compose.debug.test)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.coil.compose)
    implementation(libs.toolbar.compose)
    implementation(libs.lottie.compose)
}
