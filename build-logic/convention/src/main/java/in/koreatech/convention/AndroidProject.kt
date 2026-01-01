package `in`.koreatech.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware

internal fun configureAndroidProject(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    (commonExtension as? ApplicationExtension)?.let {
        it.defaultConfig.targetSdk = 35
    }

    commonExtension.apply {
        compileSdk = 35
        (this as? ApplicationExtension)?.let {
            it.defaultConfig.targetSdk = 35
        }
        defaultConfig {
            minSdk = 28
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
