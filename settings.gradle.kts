pluginManagement {
    plugins {
        id("com.android.application") version "8.13.1"
        id("com.android.library") version "8.13.1"
        id("org.jetbrains.kotlin.android") version "2.2.10"
        id("org.jetbrains.kotlin.kapt") version "2.2.10"
        id("com.google.devtools.ksp") version "2.2.10-2.0.2"
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.10"
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://plugins.gradle.org/m2/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "AndroidAnimationExercise"

include(
    ":app",
    ":imitate",
    ":subs:skeleton",
    ":subs:dataview",
    ":subs:gif-revert",
    ":subs:game",
    ":subs:cpp_native",
    ":subs:ai",
    ":subs:compose"
)
