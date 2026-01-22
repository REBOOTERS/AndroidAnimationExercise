plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val compileSdkProp = (project.findProperty("compileSdk") as String).toInt()
val minSdkProp = (project.findProperty("minSdk") as String).toInt()
val targetSdkProp = (project.findProperty("targetSdk") as String).toInt()

android {
    namespace = "com.engineer.ai"
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
    kotlinOptions {
        jvmTarget = "21"
    }
    androidResources {
        noCompress("tflite")
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.github.divyanshub024:AndroidDraw:v0.1")
    implementation("com.google.android.gms:play-services-tasks:18.2.0")

    implementation("com.google.android.gms:play-services-tflite-java:16.4.0")
    implementation("com.google.android.gms:play-services-tflite-support:16.4.0")
}
