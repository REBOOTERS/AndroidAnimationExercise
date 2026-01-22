plugins {
    id("com.android.library")
}

// Helper to safely read a string property from project or root extra with a fallback default
val getProp: (String, String) -> String = { name, default ->
    (project.findProperty(name) as? String)
        ?: (rootProject.extra.properties[name] as? String)
        ?: default
}

val compileSdkProp = (project.findProperty("compileSdk") as? String)?.toInt()
    ?: (rootProject.extra.properties["compileSdk"] as? Int) ?: 36
val minSdkProp = (project.findProperty("minSdk") as? String)?.toInt()
    ?: (rootProject.extra.properties["minSdk"] as? Int) ?: 26
val targetSdkProp = (project.findProperty("targetSdk") as? String)?.toInt()
    ?: (rootProject.extra.properties["targetSdk"] as? Int) ?: 36

// versions using helper
val supportVersionProp = getProp("support_version", "1.4.0")
val appcompatProp = getProp("androidx_appcompat", "1.7.0")

android {
    compileSdk = compileSdkProp

    defaultConfig {
        minSdk = minSdkProp
        targetSdk = targetSdkProp
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), file("proguard-rules.pro"))
        }
    }

    namespace = "com.ethanhua.skeleton"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("io.supercharge:shimmerlayout:2.1.0")
    implementation("androidx.recyclerview:recyclerview:$supportVersionProp")
    implementation("androidx.appcompat:appcompat:$appcompatProp")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
