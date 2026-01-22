plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val compileSdkProp = (project.findProperty("compileSdk") as String).toInt()
val minSdkProp = (project.findProperty("minSdk") as String).toInt()
val targetSdkProp = (project.findProperty("targetSdk") as String).toInt()

android {
    compileSdk = compileSdkProp

    defaultConfig {
        minSdk = minSdkProp
        targetSdk = targetSdkProp
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    sourceSets {
        getByName("main") {
            // append a single resource directory (does not replace the default src/main/res)
            resources.srcDir("src/main/res/layouts/bird")
        }
    }

    namespace = "com.engineer.android.game"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")

    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
