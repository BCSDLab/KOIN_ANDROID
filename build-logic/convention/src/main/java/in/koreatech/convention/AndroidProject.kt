package `in`.koreatech.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions


internal fun configureAndroidProject(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    (commonExtension as? ApplicationExtension)?.let {
        it.defaultConfig.targetSdk = 34
    }

    commonExtension.apply {
        compileSdk = 34
        (this as? ApplicationExtension)?.let {
            it.defaultConfig.targetSdk = 34
        }
        defaultConfig {
            minSdk = 23
            testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

}

fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
