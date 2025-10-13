package `in`.koreatech.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureAndroidLibrary(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    (commonExtension as? LibraryExtension)?.let {
        it.defaultConfig.targetSdk = 35
    }

    commonExtension.apply {
        (this as? LibraryExtension)?.let {
            it.defaultConfig.targetSdk = 35
        }

        compileSdk = 35

        defaultConfig {
            minSdk = 28

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        packagingOptions {
            resources.excludes += "DebugProbesKt.bin"
        }
    }
}
