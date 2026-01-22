plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

// Safely read shared properties with fallbacks
val compileSdkProp = (project.findProperty("compileSdk") as? String)?.toInt()
    ?: (rootProject.extra.properties["compileSdk"] as? Int) ?: 36
val minSdkProp = (project.findProperty("minSdk") as? String)?.toInt()
    ?: (rootProject.extra.properties["minSdk"] as? Int) ?: 26
val targetSdkProp = (project.findProperty("targetSdk") as? String)?.toInt()
    ?: (rootProject.extra.properties["targetSdk"] as? Int) ?: 36

// read versions with safe fallbacks
val roomVersionProp: String = (project.findProperty("roomVersion") as? String)
    ?: (rootProject.extra.properties["roomVersion"] as? String)
    ?: "2.8.0"
val archLifecycleVersionProp: String = (project.findProperty("archLifecycleVersion") as? String)
    ?: (rootProject.extra.properties["archLifecycleVersion"] as? String)
    ?: "2.2.0"

android {
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    namespace = "com.engineer.dateview"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava2)
    kapt(libs.androidx.room.compiler)
    androidTestImplementation(libs.androidx.room.testing)

    implementation(libs.androidx.lifecycle.extensions)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.mpandroidchart)
    implementation(libs.smarttable)
}
