import com.android.build.api.dsl.ApplicationExtension
import com.google.firebase.appdistribution.gradle.AppDistributionExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import `in`.koreatech.buildsrc.AppConfig
import `in`.koreatech.buildsrc.config.configureKotlinAndroid
import `in`.koreatech.buildsrc.util.implementation
import `in`.koreatech.buildsrc.util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

class AndroidFirebasePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.google.gms.google-services")
            apply("com.google.firebase.crashlytics")
            apply("com.google.firebase.appdistribution")
        }

        dependencies {
            implementation(
                libs("firebase.analytics"),
                libs("firebase.crashlytics")
            )
        }

        extensions.configure<ApplicationExtension> {

            buildTypes {
                getByName("debug") {
                    configure<CrashlyticsExtension> {
                        mappingFileUploadEnabled = false
                    }
                }

                getByName("release") {
                    configure<AppDistributionExtension> {
                        artifactType = "APK"
                        releaseNotes = "${AppConfig.VERSION_NAME} release"
                        groups = "bcsd"
                    }
                }
            }
        }
    }
}