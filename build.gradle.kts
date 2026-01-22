// Top-level build.gradle.kts converted from Groovy
plugins {
    id("com.android.application") version "8.13.1" apply false
    id("com.android.library") version "8.13.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
    id("org.jetbrains.kotlin.kapt") version "2.2.10" apply false
    id("com.google.devtools.ksp") version "2.2.10-2.0.2" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
}

apply(from = file("config.gradle"))

// Mirror important ext properties from the applied Groovy script into Kotlin `extra` for safe access from Kotlin DSL modules
// (These values match those defined in config.gradle)
extra.set("compileSdk", 36)
extra.set("minSdk", 26)
extra.set("targetSdk", 36)
extra.set("androidx_appcompat", "1.7.0")
extra.set("androidx_material", "1.12.0")
extra.set("constraint_layout", "2.2.1")
extra.set("kotlin_version", "2.0.0")
extra.set("gradle_version", "8.1.1")
extra.set("glide", "4.16.0")
extra.set("arouter_api", "1.5.2")
extra.set("arouter_compiler", "1.5.2")
extra.set("arouter_register", "1.0.2")
// Add missing properties from config.gradle so modules can read them via project.property("...")
extra.set("support_version", "1.4.0")
extra.set("androidx_preference", "1.2.1")
extra.set("core_ktx", "1.16.0")
extra.set("doraemonkit", "3.7.11")
extra.set("rxjava", "2.2.2")
extra.set("rxandroid", "2.1.0")
extra.set("roomVersion", "2.8.0")
extra.set("roomVersion_runtime", "2.8.0")
extra.set("archLifecycleVersion", "2.2.0")
extra.set("kotlin_coroutines", "1.10.1")
extra.set("kotlin_coroutines_android", "1.10.2")
// ... add other properties as needed

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
    delete(rootDir.toString() + File.separator + project.property("apkDir"))
    delete(rootDir.toString() + File.separator + "repo")
}

subprojects {
    beforeEvaluate {
        println("#### Evaluate before of ${project.path}")
    }
    afterEvaluate {
        println("#### Evaluate after of ${project.path}")
    }
}

allprojects {
    tasks.withType<Test>().configureEach {
        // Kotlin arithmetic: integer division
        val cpus = Runtime.getRuntime().availableProcessors().let { it / 2 }
        maxParallelForks = if (cpus > 0) cpus else 1
        if (!project.hasProperty("createReports")) {
            reports.html.required = false
            reports.junitXml.required = false
        }
    }

    gradle.projectsEvaluated {
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs = options.compilerArgs + listOf("-Xmaxerrs", "4000", "-Xmaxwarns", "4000")
        }
    }
}
