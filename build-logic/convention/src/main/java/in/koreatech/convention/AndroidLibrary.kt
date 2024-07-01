package `in`.koreatech.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureAndroidLibrary(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    (commonExtension as? LibraryExtension)?.let {
        it.defaultConfig.targetSdk = 34
    }

    commonExtension.apply {
        (this as? LibraryExtension)?.let {
            it.defaultConfig.targetSdk = 34
        }

        compileSdk = 34

        defaultConfig {
            minSdk = 24

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        buildFeatures {
            viewBinding = true
        }

        packagingOptions {
            resources.excludes += "DebugProbesKt.bin"
        }
    }
}
