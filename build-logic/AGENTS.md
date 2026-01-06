# BUILD-LOGIC Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working with the BUILD-LOGIC module of the KOIN_ANDROID repository.

## Module Overview

The `build-logic` module contains **Gradle convention plugins** that provide consistent build configuration across all modules in the project. It uses Gradle's composite builds feature (`includeBuild`) to share build logic and eliminate duplication.

### Architecture Position
```
┌─────────────────────────────────────────┐
│         Root Project                   │
│  (settings.gradle: includeBuild)      │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│      build-logic/convention            │
│  (Gradle Convention Plugins)           │
└─────────────────────────────────────────┘
         ↓ Applied to ↓
┌──────────────────┬────────────────────┐
│   App Modules    │  Feature Modules   │
│  koin, business  │  store, chat...   │
└──────────────────┴────────────────────┘
```

## Core Responsibilities

1. **Convention Plugins**: Define reusable Gradle plugins for common configurations
2. **Configuration Functions**: Extract shared configuration logic into reusable functions
3. **Dependency Management**: Use version catalog for centralized dependency management
4. **Build Consistency**: Ensure all modules use consistent build settings (SDK versions, Java/Kotlin versions)
5. **Code Quality**: Apply ktlint, KSP, and other tooling consistently
6. **Type Safety**: Provide type-safe dependency extensions

## Module Structure

```
build-logic/
├── convention/
│   ├── src/main/java/
│   │   ├── in/koreatech/convention/           # Configuration functions
│   │   │   ├── AndroidProject.kt              # Common Android config
│   │   │   ├── AndroidLibrary.kt              # Library-specific config
│   │   │   ├── AndroidCompose.kt              # Compose setup
│   │   │   ├── AndroidOrbit.kt               # Orbit MVI setup
│   │   │   ├── AndroidTest.kt                # Android test config
│   │   │   ├── Test.kt                       # Unit test config
│   │   │   ├── ComposeTest.kt               # Paparazzi test config
│   │   │   ├── KotlinAndroid.kt              # Kotlin Android config
│   │   │   ├── DependencyHandlerExtension.kt   # Type-safe dependencies
│   │   │   └── ProjectExtension.kt           # Version catalog access
│   │   ├── AndroidApplicationConventionPlugin.kt
│   │   ├── AndroidFeatureConventionPlugin.kt
│   │   ├── AndroidLibraryConventionPlugin.kt
│   │   ├── AndroidApplicationHiltConventionPlugin.kt
│   │   ├── AndroidApplicationOrbitConventionPlugin.kt
│   │   ├── AndroidLibraryOrbitConventionPlugin.kt
│   │   ├── AndroidLibraryPaparazziConventionPlugin.kt
│   │   ├── FirebaseConventionPlugin.kt
│   │   ├── JavaLibraryConventionPlugin.kt
│   │   └── OssLicensePlugin.kt
│   └── build.gradle.kts                         # Plugin registration
├── settings.gradle.kts
└── AGENTS.md
```

## Convention Plugin Architecture

### Plugin Registration

**MUST** register all plugins in `convention/build.gradle.kts`:

```kotlin
plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.gradle.tool)
}

gradlePlugin {
    plugins {
        // Application plugins
        register("AndroidApplicationPlugin") {
            id = "in.koreatech.plugin.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("AndroidApplicationHiltPlugin") {
            id = "in.koreatech.plugin.hilt"
            implementationClass = "AndroidApplicationHiltConventionPlugin"
        }
        register("AndroidApplicationOrbitPlugin") {
            id = "in.koreatech.plugin.orbit"
            implementationClass = "AndroidApplicationOrbitConventionPlugin"
        }

        // Library plugins
        register("AndroidLibraryPlugin") {
            id = "in.koreatech.plugin.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("AndroidFeaturePlugin") {
            id = "in.koreatech.plugin.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("AndroidLibraryOrbitPlugin") {
            id = "in.koreatech.plugin.library.orbit"
            implementationClass = "AndroidLibraryOrbitConventionPlugin"
        }
        register("AndroidLibraryPaparazziPlugin") {
            id = "in.koreatech.plugin.library.paparazzi"
            implementationClass = "AndroidLibraryPaparazziConventionPlugin"
        }

        // Special purpose plugins
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
```

### Version Catalog Access

**MUST** use the `Project.libs` extension to access version catalog:

```kotlin
// In ProjectExtension.kt
val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

// Usage in plugins
libs.findPlugin("androidLibrary")  // Access plugin
libs.findLibrary("hilt-android")   // Access library
libs.findBundle("hilt")           // Access dependency bundle
libs.versions.okhttp.get()        // Access version string
```

### Type-Safe Dependency Extensions

**MUST** use extension functions for type-safe dependency addition:

```kotlin
// DependencyHandlerExtension.kt
fun DependencyHandler.ksp(dependency: Any) {
    add("ksp", dependency)
}

fun DependencyHandler.kapt(dependency: Any) {
    add("kapt", dependency)
}

fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

fun DependencyHandler.debugImplementation(dependency: Any) {
    add("debugImplementation", dependency)
}

fun DependencyHandler.api(dependency: Any) {
    add("api", dependency)
}

fun DependencyHandler.androidTestImplementation(dependency: Any) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandler.testImplementation(dependency: Any) {
    add("testImplementation", dependency)
}

fun DependencyHandler.coreLibraryDesugaring(dependency: Any) {
    add("coreLibraryDesugaring", dependency)
}

// Usage in plugins
dependencies {
    implementation(libs.findLibrary("hilt-android").get())
    ksp(libs.findLibrary("hilt-compiler").get())
    testImplementation(libs.findLibrary("junit").get())
}
```

## Convention Plugin Patterns

### 1. Android Application Plugin

**Purpose**: Configure application modules (koin, business)

**Plugin ID**: `in.koreatech.plugin.application`

```kotlin
internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // Apply required plugins
                apply(libs.findPlugin("android-application").get().get().pluginId)
                apply(libs.findPlugin("kotlin-android").get().get().pluginId)
                apply(libs.findPlugin("kotlin-parcelize").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
                apply(libs.findPlugin("ktlint").get().get().pluginId)
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            }

            // Configure Android extension
            val extension = extensions.getByType<BaseAppModuleExtension>()
            extensions.configure<ApplicationExtension> {
                configureAndroidProject(extension)     // Common Android config
                configureAndroidCompose(this)           // Compose setup
                configureTest()                        // Unit test config
                configureAndroidTest()                  // Android test config
            }
        }
    }
}
```

**Applied Configuration**:
- compileSdk = 35
- targetSdk = 35 (for ApplicationExtension)
- minSdk = 28
- Java 17 compatibility
- Compose enabled
- KSP enabled
- ktlint enabled
- Test configurations

### 2. Android Feature Plugin

**Purpose**: Configure feature modules (timetable, store, chat, etc.)

**Plugin ID**: `in.koreatech.plugin.feature`

```kotlin
internal class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("androidLibrary").get().get().pluginId)
                apply(libs.findPlugin("kotlin-android").get().get().pluginId)
                apply(libs.findPlugin("kotlin-parcelize").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
                apply(libs.findPlugin("ktlint").get().get().pluginId)
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureAndroidLibrary(this)           // Library-specific config
                configureAndroidCompose(this)           // Compose setup
                configureTest()                        // Unit test config
                configureAndroidTest()                  // Android test config
            }
        }
    }
}
```

### 3. Android Library Plugin

**Purpose**: Configure core library modules (core:analytics, core:designsystem, etc.)

**Plugin ID**: `in.koreatech.plugin.library`

```kotlin
internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("androidLibrary").get().get().pluginId)
                apply(libs.findPlugin("kotlin-android").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
                apply(libs.findPlugin("ktlint").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureAndroidLibrary(this)           // Library-specific config
                configureTest()                        // Unit test config
                configureAndroidTest()                  // Android test config
            }
        }
    }
}
```

### 4. Hilt Plugin

**Purpose**: Configure Hilt dependency injection

**Plugin IDs**:
- Application: `in.koreatech.plugin.hilt`
- Libraries: Applied via manual plugin application in modules

```kotlin
internal class AndroidApplicationHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("hilt").get().get().pluginId)
            }

            dependencies {
                // Use Hilt bundle from version catalog
                implementation(libs.findBundle("hilt").get())
                ksp(libs.findLibrary("hilt-compiler").get())
            }
        }
    }
}
```

### 5. Orbit MVI Plugin

**Purpose**: Configure Orbit MVI state management

**Plugin IDs**:
- Application: `in.koreatech.plugin.orbit`
- Library: `in.koreatech.plugin.library.orbit`

```kotlin
// Application version
internal class AndroidApplicationOrbitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<ApplicationExtension> {
                configureAndroidOrbit(this)
            }
        }
    }
}

// Library version
internal class AndroidLibraryOrbitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                configureAndroidOrbit(this)
            }
        }
    }
}

// Configuration function
internal fun Project.configureAndroidOrbit(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    dependencies {
        // Use Orbit bundle from version catalog
        implementation(libs.findBundle("orbit").get())
    }
}
```

### 6. Paparazzi Plugin

**Purpose**: Configure Paparazzi screenshot testing

**Plugin ID**: `in.koreatech.plugin.library.paparazzi`

```kotlin
internal class AndroidLibraryPaparazziConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                configureAndroidPaparazzi(this)
            }
        }
    }
}

// Configuration function (in ComposeTest.kt)
internal fun Project.configureAndroidPaparazzi(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    dependencies {
        androidTestImplementation(libs.findLibrary("androidx-test-ext-junit-ktx").get())
        androidTestImplementation(libs.findLibrary("androidx-compose-ui-test-junit4-android").get())
        testImplementation(libs.findLibrary("paparazzi").get())
    }
}
```

### 7. Firebase Plugin

**Purpose**: Configure Firebase SDKs

**Plugin ID**: `in.koreatech.plugin.firebase`

```kotlin
internal class FirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("firebase-appdistribution").get().get().pluginId)
                apply(libs.findPlugin("firebase-crashlytics").get().get().pluginId)
            }

            dependencies {
                implementation(platform(libs.findLibrary("firebase-bom").get()))
                implementation(libs.findBundle("firebase").get())
            }
        }
    }
}
```

### 8. Java Library Plugin

**Purpose**: Configure pure Java/Kotlin library modules (domain)

**Plugin ID**: `in.koreatech.plugin.java`

```kotlin
class JavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("java-library")
            }

            dependencies {
                implementation(libs.findLibrary("javax-inject").get())
            }

            configureKotlinJvm()
        }
    }
}
```

### 9. OSS License Plugin

**Purpose**: Configure OSS license attribution

**Plugin ID**: `in.koreatech.plugin.oss`

```kotlin
class OssLicensePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("oss-license-plugin").get().get().pluginId)
            }

            dependencies {
                implementation(libs.findLibrary("oss-licenses").get())
            }
        }
    }
}
```

## Configuration Functions

### configureAndroidProject()

**Purpose**: Common Android configuration (application & library)

```kotlin
internal fun configureAndroidProject(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    (commonExtension as? ApplicationExtension)?.let {
        it.defaultConfig.targetSdk = 35
    }

    commonExtension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 28
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
```

### configureAndroidLibrary()

**Purpose**: Library-specific configuration

```kotlin
internal fun Project.configureAndroidLibrary(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    (commonExtension as? LibraryExtension)?.let {
        it.defaultConfig.targetSdk = 35
    }

    commonExtension.apply {
        compileSdk = 35

        defaultConfig {
            minSdk = 28
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        packagingOptions {
            resources.excludes += "DebugProbesKt.bin"
        }
    }
}
```

### configureAndroidCompose()

**Purpose**: Configure Jetpack Compose

```kotlin
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures.compose = true
        buildFeatures {
            compose = true
        }

        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    dependencies {
        // Use Compose BOM from version catalog
        implementation(platform(libs.findLibrary("androidx.compose.bom").get()))
        implementation(libs.findBundle("compose.m3").get())
        debugImplementation(libs.findBundle("compose.debug.test").get())
        androidTestImplementation(libs.findLibrary("androidx.compose.ui.test.manifest").get())
    }
}
```

### configureTest()

**Purpose**: Configure unit tests

```kotlin
internal fun Project.configureTest() {
    dependencies {
        testImplementation(libs.findLibrary("junit").get())
    }
}
```

### configureAndroidTest()

**Purpose**: Configure Android instrumented tests

```kotlin
internal fun Project.configureAndroidTest() {
    dependencies {
        androidTestImplementation(libs.findLibrary("androidx.test.ext.junit").get())
        androidTestImplementation(libs.findLibrary("androidx.test.espresso.core").get())
    }
}
```

### configureKotlinJvm()

**Purpose**: Configure Kotlin JVM projects

```kotlin
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
```

## Usage in Modules

### Application Module (koin/build.gradle.kts)

```kotlin
plugins {
    alias(libs.plugins.koin.application)      // AndroidApplicationConventionPlugin
    alias(libs.plugins.koin.hilt)              // AndroidApplicationHiltConventionPlugin
    alias(libs.plugins.koin.firebase)           // FirebaseConventionPlugin
    alias(libs.plugins.koin.oss.license)       // OssLicensePlugin
    id("com.google.gms.google-services")
}

android {
    namespace = "in.koreatech.koin"

    defaultConfig {
        applicationId = "in.koreatech.koin"
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"].toString()
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".dev"
            isDebuggable = true
            isMinifyEnabled = false
        }

        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core)
    implementation(projects.feature.store)
    // ... other dependencies
}
```

### Feature Module (feature/store/build.gradle.kts)

```kotlin
plugins {
    alias(libs.plugins.koin.feature)           // AndroidFeatureConventionPlugin
    alias(libs.plugins.koin.hilt)              // AndroidApplicationHiltConventionPlugin
    alias(libs.plugins.koin.library.orbit)      // AndroidLibraryOrbitConventionPlugin
    alias(libs.plugins.koin.library.paparazzi)  // AndroidLibraryPaparazziConventionPlugin
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "in.koreatech.koin.feature.store"

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "ORDER_BASE_URL", "...")
        }

        getByName("release") {
            buildConfigField("String", "ORDER_BASE_URL", "...")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.analytics)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    debugImplementation(libs.bundles.compose.debug.test)
}
```

### Library Module (domain/build.gradle.kts)

```kotlin
plugins {
    alias(libs.plugins.koin.java)              // JavaLibraryConventionPlugin
    alias(libs.plugins.ktlint)
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
}
```

## Version Catalog Structure

**MUST** define dependencies in `gradle/libs.versions.toml`:

```toml
[versions]
androidGradle = "8.13.0"
kotlin = "2.2.20"
hilt = "2.57.2"
orbit = "7.0.1"
androidxComposeBom = "2025.10.00"

[libraries]
# Android Gradle
android-gradle-tool = { module = "com.android.tools.build:gradle", version.ref = "androidGradle" }
kotlin-gradle = { module = "org.jetbrains.kotlin:kotlin-gradle-plugin", version.ref = "kotlin" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }

# Orbit
orbit-core = { group = "org.orbit-mvi", name = "orbit-core", version.ref = "orbit" }
orbit-viewmodel = { group = "org.orbit-mvi", name = "orbit-viewmodel", version.ref = "orbit" }
orbit-compose = { group = "org.orbit-mvi", name = "orbit-compose", version.ref = "orbit" }

# Compose
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "androidxComposeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }

[plugins]
android-application = { id = "com.android.application", version.ref = "androidGradle" }
androidLibrary = { id = "com.android.library", version.ref = "androidGradle" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }

# Convention plugins (for type-safe access)
koin-application = { id = "in.koreatech.plugin.application" }
koin-feature = { id = "in.koreatech.plugin.feature" }
koin-library = { id = "in.koreatech.plugin.library" }
koin-hilt = { id = "in.koreatech.plugin.hilt" }
koin-orbit = { id = "in.koreatech.plugin.orbit" }
koin-library-orbit = { id = "in.koreatech.plugin.library.orbit" }
koin-library-paparazzi = { id = "in.koreatech.plugin.library.paparazzi" }
koin-firebase = { id = "in.koreatech.plugin.firebase" }
koin-java = { id = "in.koreatech.plugin.java" }
koin-oss-license = { id = "in.koreatech.plugin.oss" }

[bundles]
hilt = ["androidx-hilt-navigation-compose", "hilt-android"]
orbit = ["orbit-core", "orbit-viewmodel", "orbit-compose"]
compose = ["androidx-compose-ui", "androidx-compose-graphics", "androidx-compose-preview", "androidx-compose-material2", "androidx-activity-compose", "androidx-compose-ui-tooling"]
compose_m3 = ["androidx-compose-ui", "androidx-compose-graphics", "androidx-compose-preview", "androidx-compose-material3", "androidx-activity-compose", "androidx-compose-ui-tooling", "androidx-compose-material-icons-core"]
compose_debug_test = ["androidx-compose-ui-tooling", "androidx-compose-ui-test-manifest"]
firebase = ["firebase-crashlytics", "firebase-analytics", "firebase-analytics-ktx", "firebase-database", "firebase-perf", "firebase-messsaing"]
```

## Root Configuration

### settings.gradle (root project)

**MUST** include build-logic as composite build:

```gradle
pluginManagement {
    includeBuild("build-logic")  // <-- CRITICAL: Include convention plugins
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://repository.map.naver.com/archive/maven' }
        maven { url 'https://jitpack.io' }
        maven { url 'https://devrepo.kakao.com/nexus/content/groups/public/' }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include ':koin', ':core'
include ':data'
include ':domain'
include ':business'
include ':core:notification'
include ':core:navigation'
include ':core:onboarding'
include ':feature:timetable'
// ... other modules
```

### build.gradle.kts (root project)

**MUST** configure buildscript and declare plugins:

```kotlin
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    extra.apply {
        set("versionName", "4.5.10")
        set("versionCode", 40510)
        set("versionBusinessName", "1.0.1")
        set("versionBusinessCode", 1000002)
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
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
```

## Creating New Convention Plugin

When creating a new convention plugin, follow this workflow:

### Step 1: Create Configuration Function (if needed)

```kotlin
// File: convention/src/main/java/in/koreatech/convention/MyNewFeature.kt
package `in`.koreatech.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureMyNewFeature(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    dependencies {
        implementation(libs.findLibrary("my-library").get())
    }

    commonExtension.apply {
        // Configure extension properties
        buildFeatures {
            myNewFeature = true
        }
    }
}
```

### Step 2: Create Plugin Class

```kotlin
// File: convention/src/main/java/MyNewConventionPlugin.kt
package `in`.koreatech.convention

import `in`.koreatech.convention.configureMyNewFeature
import `in`.koreatech.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class MyNewConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("my-plugin").get().get().pluginId)
            }

            extensions.configure<CommonExtension<*, *, *, *, *, *>> {
                configureMyNewFeature(this)
            }
        }
    }
}
```

### Step 3: Register Plugin

```kotlin
// File: convention/build.gradle.kts
gradlePlugin {
    plugins {
        register("myNewFeaturePlugin") {
            id = "in.koreatech.plugin.mynewfeature"
            implementationClass = "MyNewConventionPlugin"
        }
    }
}
```

### Step 4: Add to Version Catalog

```toml
# File: gradle/libs.versions.toml
[plugins]
my-plugin = { id = "com.example.plugin", version = "1.0.0" }

# For type-safe access
koin-mynewfeature = { id = "in.koreatech.plugin.mynewfeature" }

[libraries]
my-library = { group = "com.example", name = "library", version = "1.0.0" }
```

### Step 5: Apply in Modules

```kotlin
// File: feature/myfeature/build.gradle.kts
plugins {
    alias(libs.plugins.koin.mynewfeature)
}
```

## Critical Rules

These rules are **non-negotiable**:

1. **Convention Plugins**: **MUST** use convention plugins for all modules (no duplicate configuration)
2. **Version Centralization**: **MUST** manage all dependency versions in `libs.versions.toml`
3. **Plugin Registration**: **MUST** register all plugins in `convention/build.gradle.kts`
4. **Type Safety**: **MUST** use `libs.findPlugin()`, `libs.findLibrary()`, `libs.findBundle()`
5. **Version Catalog Access**: **MUST** use `Project.libs` extension for version catalog access
6. **Dependency Extensions**: **MUST** use type-safe `DependencyHandler` extensions
7. **Configuration Functions**: **MUST** extract shared logic to configuration functions
8. **Package Structure**: **MUST** place configuration functions in `in.koreatech.convention` package
9. **Java Version**: **MUST** use Java 17 for all modules
10. **SDK Versions**: **MUST** use compileSdk = 35, minSdk = 28, targetSdk = 35
11. **Internal Visibility**: **MUST** mark plugin classes as `internal`
12. **Include Build**: **MUST** include `build-logic` in root `settings.gradle`

## Best Practices

### 1. DRY Principle

**Avoid** duplicate configuration:
```kotlin
// ❌ WRONG: Duplicate configuration
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    compileSdk = 35
    defaultConfig {
        minSdk = 28
    }
}
```

**Use** convention plugins:
```kotlin
// ✅ CORRECT: Use convention plugin
plugins {
    alias(libs.plugins.koin.library)
}
```

### 2. Configuration Reuse

**Extract** common logic:
```kotlin
// ✅ CORRECT: Shared configuration function
internal fun configureAndroidProject(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = 35
        defaultConfig { minSdk = 28 }
    }
}
```

### 3. Type Safety

**Use** type-safe extensions:
```kotlin
// ❌ WRONG: String-based dependency
dependencies {
    implementation("com.google.dagger:hilt-android:2.57.2")
}

// ✅ CORRECT: Type-safe version catalog
dependencies {
    implementation(libs.findLibrary("hilt-android").get())
}
```

### 4. Bundle Dependencies

**Group** related dependencies:
```kotlin
// ✅ CORRECT: Use bundles
dependencies {
    implementation(libs.findBundle("orbit").get())  // Adds all Orbit libraries
    implementation(libs.findBundle("hilt").get())   // Adds all Hilt libraries
}
```

### 5. Plugin Composition

**Combine** multiple plugins:
```kotlin
// Feature module composition
plugins {
    alias(libs.plugins.koin.feature)           // Android + Compose + Tests
    alias(libs.plugins.koin.hilt)              // Hilt DI
    alias(libs.plugins.koin.library.orbit)      // Orbit MVI
    alias(libs.plugins.koin.library.paparazzi)  // Screenshot testing
}
```

## Build Commands

```bash
# Build build-logic module
./gradlew :build-logic:convention:build

# Clean build-logic
./gradlew :build-logic:convention:clean

# Test build-logic
./gradlew :build-logic:convention:test

# Check ktlint for build-logic
./gradlew :build-logic:convention:ktlintCheck

# Format ktlint for build-logic
./gradlew :build-logic:convention:ktlintFormat

# Rebuild convention plugins (when modified)
./gradlew clean :build-logic:convention:build
```

## Common Issues and Solutions

### Issue: "Plugin not found"

**Cause**: Plugin not registered in `convention/build.gradle.kts`

**Solution**:
```kotlin
gradlePlugin {
    plugins {
        register("myPlugin") {
            id = "in.koreatech.plugin.myplugin"
            implementationClass = "MyConventionPlugin"
        }
    }
}
```

### Issue: "libs not found"

**Cause**: `ProjectExtension.kt` not created or imported

**Solution**:
```kotlin
// Create ProjectExtension.kt
val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

// Import in plugin file
import `in`.koreatech.convention.libs
```

### Issue: Changes not reflected

**Cause**: Convention plugin not rebuilt

**Solution**:
```bash
./gradlew clean :build-logic:convention:build
```

## Testing Convention Plugins

Convention plugins **SHOULD** be tested with sample modules:

1. Create test module in `build-logic/samples/`
2. Apply convention plugin
3. Verify generated configuration
4. Check for syntax errors
5. Validate applied plugins and dependencies

```bash
# Test convention plugin on sample module
./gradlew :build-logic:samples:app:assembleDebug
```

---

**Last Updated**: 2026-01-05  
**For**: AI Coding Agents working on BUILD-LOGIC module  
**Maintainers**: BCSD Android Track
