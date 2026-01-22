plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
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

android {
    namespace = "com.engineer.compose"
    compileSdk = compileSdkProp

    defaultConfig {
        minSdk = minSdkProp
        targetSdk = targetSdkProp
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    api("androidx.compose.material:material-icons-extended-android:1.6.8")
    api(libs.coil.compose)
    api("io.coil-kt:coil-gif:2.7.0")

    api(libs.androidx.compose.runtime)
    api(libs.androidx.lifecycle.viewmodel.compose)

    api(libs.accompanist.pager)
    api(libs.accompanist.pager.indicator)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugApi(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
}
