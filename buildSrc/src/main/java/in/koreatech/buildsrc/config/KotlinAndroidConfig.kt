package `in`.koreatech.buildsrc.config

import com.android.build.api.dsl.CommonExtension
import `in`.koreatech.buildsrc.AppConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import `in`.koreatech.buildsrc.util.implementation
import `in`.koreatech.buildsrc.util.libs
import org.gradle.kotlin.dsl.dependencies

fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
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

        compileOptions {
            isCoreLibraryDesugaringEnabled = true

            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            jvmTarget = "1.8"
        }

        packagingOptions {
            resources.excludes += "DebugProbesKt.bin"
        }

        dataBinding.enable = true
    }

    dependencies {
        add("coreLibraryDesugaring", libs("android.desugarJdkLibs"))

        libs("androidx.core.ktx")
        libs("kotlinx.coroutines.android")
    }
}

fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}