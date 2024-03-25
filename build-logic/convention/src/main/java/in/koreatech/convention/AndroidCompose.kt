package `in`.koreatech.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures.compose = true
        composeOptions {
            kotlinCompilerExtensionVersion =
                libs.findVersion("composeCompilerVersion").get().requiredVersion
        }
        buildFeatures {
            compose = true
        }

        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    dependencies {
        implementation(platform(libs.findLibrary("compose.bom").get()))
        implementation(libs.findBundle("compose").get())
        debugImplementation(libs.findBundle("compose.debug.test").get())
        androidTestImplementation(libs.findLibrary("compose.ui.test.manifest").get())
    }
}