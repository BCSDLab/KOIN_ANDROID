// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    extra.apply {
        set("versionName", "5.1.0")
        set("versionCode", 50100)
        // 코인 버전 관리

        set("versionBusinessName", "1.0.1")
        set("versionBusinessCode", 1000002)
        //코안 사장님 버전 관리
    }

    dependencies {
        classpath(libs.android.gradle.tool)
        classpath(libs.kotlin.gradle)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.firebase.appdistribution.gradle)
        classpath(libs.oss.licenses.plugin)
        classpath(libs.hilt.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.google.service) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.sonarqube)
}

val reportsDir = subprojects.map { subproject ->
    subproject.projectDir.absolutePath
}.filter {
    !it.endsWith("domain") && !it.endsWith("feature")
}.map { subproject ->
    "${subproject}/build/reports"
}

val ktlintReports = reportsDir.joinToString(",") { subproject ->
    "${subproject}/ktlint/ktlintMainSourceSetCheck/ktlintMainSourceSetCheck.xml"
}

val lintReports = reportsDir.joinToString(",") { subproject ->
    "${subproject}/lint-results-debug.xml"
}

val detektReports = reportsDir.joinToString(",") { subproject ->
    "${subproject}/detekt/detekt.xml"
}

val koverReports = reportsDir.joinToString(",") { subproject ->
    "${subproject}/kover/report.xml"
}

val sonarCoverageExclusions = listOf(
    "**/core/analytics/**",
    "**/core/designsystem/**",
    "**/core/navigation/**",
    "**/core/network/**",
    "**/core/notification/**",
    "**/core/onboarding/**",
    "**/core/webapp/**",
    "**/firebase/**",
    "**/di/**",
    "**/navigation/*",
    "**/feature/**/model/**",
    "**/feature/**/component/**",
    "**/ui/**/*Screen.kt",
    "**/ui/**/*State.kt",
    "**/ui/**/*SideEffect.kt",
    "**/*Activity.kt",
    "**/*RecyclerAdapter.kt",
    "**/*RecyclerViewAdapter.kt",
    "**/*Fragment.kt"
).joinToString(", ")

sonar {
    properties {
        property("sonar.projectKey", "BCSDLab_KOIN_ANDROID")
        property("sonar.organization", "bcsdlab")
        property("sonar.coverage.jacoco.xmlReportPaths", koverReports)
        property("sonar.androidLint.reportPaths", lintReports)
        property("sonar.kotlin.detekt.reportPaths", detektReports)
        property("sonar.kotlin.ktlint.reportPaths", ktlintReports)
        property("sonar.coverage.exclusions", sonarCoverageExclusions)
        property("sonar.exclusions", "**/*.java")
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.named<Delete>("clean") {
    delete(rootProject.buildDir)
}
tasks.register("installGitHooks") {
    doLast {
        exec {
            commandLine("git", "config", "core.hooksPath", ".githooks")
        }
        println("Git hooks installed!")
    }
}
