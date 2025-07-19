plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
}

android {
    namespace = "in.koreatech.koin.core.notification"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.timber)
}
