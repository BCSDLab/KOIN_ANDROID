pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            setUrl("https://naver.jfrog.io/artifactory/maven/")
        }
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://naver.jfrog.io/artifactory/maven/")
        }
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

rootProject.name = "koin"

include(":koin")
include(":core")
include(":data")
include(":domain")