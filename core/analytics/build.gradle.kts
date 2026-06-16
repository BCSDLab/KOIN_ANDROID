plugins {
    alias(libs.plugins.koin.library)
}

android {
    namespace = "in.koreatech.koin.core.analytics"

    buildTypes {
        getByName("debug") {
            buildConfigField("Boolean", "IS_DEBUG", "true")
            buildConfigField("Boolean", "IS_QA", "false")
        }

        getByName("release") {
            buildConfigField("Boolean", "IS_DEBUG", "false")
            buildConfigField("Boolean", "IS_QA", "false")
        }

        getByName("qa") {
            // Set true in core:analytics module because logging should have debug suffix on qa variant
            buildConfigField("Boolean", "IS_DEBUG", "true")
            buildConfigField("Boolean", "IS_QA", "true")
        }
    }
}

dependencies {
    implementation(projects.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}

kover {
    reports {
        filters {
            excludes {
                classes("*")
            }
        }
    }
}
