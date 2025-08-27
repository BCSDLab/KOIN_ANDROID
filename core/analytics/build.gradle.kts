plugins {
    alias(libs.plugins.koin.library)
}

android {
    namespace = "in.koreatech.koin.core.analytics"

    buildTypes {
        getByName("debug") {
            buildConfigField("Boolean", "IS_DEBUG", "true")
        }

        getByName("release") {
            buildConfigField("Boolean", "IS_DEBUG", "false")
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
