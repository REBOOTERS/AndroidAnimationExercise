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

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("androidx.room:room-runtime:$roomVersionProp")
    implementation("androidx.room:room-rxjava2:$roomVersionProp")
    kapt("androidx.room:room-compiler:$roomVersionProp")
    androidTestImplementation("androidx.room:room-testing:$roomVersionProp")

    implementation("androidx.lifecycle:lifecycle-extensions:$archLifecycleVersionProp")

    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.github.PhilJay:MPAndroidChart:3.0.3")
    implementation("com.github.huangyanbin:SmartTable:2.2.0")
}
