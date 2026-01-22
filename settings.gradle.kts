pluginManagement {
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
    ":subs",
    ":subs:skeleton",
    ":subs:dataview",
    ":subs:gif-revert",
    ":subs:game",
    ":subs:cpp_native",
    ":subs:ai",
    ":subs:compose"
)
