plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.undercouchDownload)
}

android {
    namespace = "com.engineer.ai"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro")
            )
        }
    }

    // preBuild check
    tasks.named("preBuild").configure {
        doFirst {
            if (!file("./src/main/assets/mnist.tflite").exists()) {
                throw GradleException("mnist.tflite not found. Make sure you have trained and downloaded your TensorFlow Lite model to assets/ folder")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    androidResources {
        noCompress.add("tflite")
    }
    buildFeatures {
        viewBinding = true
    }
}
// Import DownloadModels task
project.ext.set("ASSET_DIR", "$projectDir/src/main/assets")
apply(from = "download_model.gradle")

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material.google)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.android.draw)
//    implementation(libs.play.services.tasks)

    implementation(libs.litert)
    implementation(libs.litert.support) {
        exclude(group = "com.google.ai.edge.litert", module = "litert-api")
    }
    implementation(libs.litert.metadata) {
        exclude(group = "com.google.ai.edge.litert", module = "litert-api")
    }
//    implementation(libs.play.services.tflite.java) {
//        exclude(group = "com.google.ai.edge.litert", module = "litert-api")
//    }
//    implementation(libs.play.services.tflite.support) {
//        exclude(group = "com.google.ai.edge.litert", module = "litert-api")
//    }
//
//    implementation(libs.tensorflow.lite)
//    implementation(libs.tensorflow.lite.support)
}
