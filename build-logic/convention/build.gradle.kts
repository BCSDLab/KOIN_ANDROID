plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.gradle.tool)
}

gradlePlugin {
    plugins {
        register("AndroidApplicationPlugin") {
            id = "in.koreatech.plugin.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("AndroidApplicationComposePlugin") {
            id = "in.koreatech.plugin.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("AndroidApplicationHiltPlugin"){
            id = "in.koreatech.plugin.hilt"
            implementationClass = "AndroidApplicationHiltConventionPlugin"
        }

        register("AndroidApplicationOrbitPlugin"){
            id = "in.koreatech.plugin.orbit"
            implementationClass = "AndroidApplicationOrbitConventionPlugin"
        }
        register("AndroidLibraryPlugin") {
            id = "in.koreatech.plugin.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("FirebasePlugin") {
            id = "in.koreatech.plugin.firebase"
            implementationClass = "FirebaseConventionPlugin"
        }
        register("JavaLibraryPlugin") {
            id = "in.koreatech.plugin.java"
            implementationClass = "JavaLibraryConventionPlugin"
        }
        register("OssLicensePlugin") {
            id = "in.koreatech.plugin.oss"
            implementationClass = "OssLicensePlugin"
        }
    }
}
