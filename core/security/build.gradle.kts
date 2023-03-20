plugins {
    id("koin.android.library")
    id("koin.android.hilt")
}

android {
    namespace = "in.koreatech.core.security"
}

dependencies {
    implementation(libs.androidx.security)
}