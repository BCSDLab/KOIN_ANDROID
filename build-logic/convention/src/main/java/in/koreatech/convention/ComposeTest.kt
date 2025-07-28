import com.android.build.api.dsl.CommonExtension
import `in`.koreatech.convention.androidTestImplementation
import `in`.koreatech.convention.libs
import `in`.koreatech.convention.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidPaparazzi(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    dependencies {
        androidTestImplementation(libs.findLibrary("androidx-test-ext-junit-ktx").get())
        androidTestImplementation(libs.findLibrary("androidx-compose-ui-test-junit4-android").get())
        testImplementation(libs.findLibrary("paparazzi").get())
    }
}