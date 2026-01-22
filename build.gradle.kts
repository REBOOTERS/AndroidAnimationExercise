// Top-level build.gradle.kts converted from Groovy
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlinCompose) apply false
}


// Project-wide version/SDK properties are now managed in the version catalog (gradle/libs.versions.toml)

tasks.register("clean", Delete::class) {
//    delete(rootProject.buildDir)
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
