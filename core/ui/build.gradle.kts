import `in`.koreatech.buildsrc.util.implementation

plugins {
    id("koin.android.library")
    id("koin.android.hilt")
}

android {
    namespace = "in.koreatech.core.ui"
}

dependencies {
    implementation(
        project(":core:designsystem"),
        project(":core:util")
    )

    implementation(
        libs.androidx.appcompat,
        libs.androidx.material,
        libs.stickyscrollview
    )
}