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
    implementation(project(":domain"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}