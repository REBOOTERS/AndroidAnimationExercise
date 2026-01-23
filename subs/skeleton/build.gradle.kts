plugins {
    id("com.android.library")
}

// Helper to safely read a string property from project or root extra with a fallback default
val getProp: (String, String) -> String = { name, default ->
    (project.findProperty(name) as? String)
        ?: (rootProject.extra.properties[name] as? String)
        ?: default
}

// versions using helper
val supportVersionProp = getProp("support_version", "1.4.0")
val appcompatProp = getProp("androidx_appcompat", "1.7.0")

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
        }
    }

    namespace = "com.ethanhua.skeleton"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.io.supercharge.shimmerlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.appcompat)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)
}
