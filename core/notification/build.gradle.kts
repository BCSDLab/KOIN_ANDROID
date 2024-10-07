plugins {
    alias(libs.plugins.koin.library)
    alias(libs.plugins.koin.hilt)
}

android {
    namespace = "in.koreatech.koin.core.notification"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.timber)
}