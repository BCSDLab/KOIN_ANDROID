package `in`.koreatech.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project


internal fun Project.configureAndroidLint(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        lint {
            baseline = file("lint-baseline.xml")
            abortOnError = false
        }
    }
}
