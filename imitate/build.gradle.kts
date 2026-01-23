import org.gradle.process.CommandLineArgumentProvider
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.10"
    id("com.android.application")
    id("kotlin-kapt")
}

val imitateIsApp = (project.findProperty("imitate_isApp") as? String)?.toBoolean() ?: true
if (imitateIsApp) {
    plugins.apply("com.android.application")
} else {
    plugins.apply("com.android.library")
}
plugins.apply("com.android.application")
plugins.apply("org.jetbrains.kotlin.android")
plugins.apply("org.jetbrains.kotlin.plugin.compose")
plugins.apply("kotlin-kapt")

class RoomSchemaArgProvider(@get:InputDirectory @get:PathSensitive(PathSensitivity.RELATIVE) val schemaDir: File) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> = listOf("-Aroom.schemaLocation=${schemaDir.path}")
}

fun releaseTime(): String {
    val sdf = SimpleDateFormat("yyyyMMddHHmm")
    sdf.timeZone = TimeZone.getTimeZone("GMT+8")
    return sdf.format(Date())
}

val ext = rootProject.extra

android {
    compileSdk = 36

    defaultConfig {
        minSdk =  26
        targetSdk =  36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("moduleName", project.name)
            }
        }

        javaCompileOptions {
            annotationProcessorOptions {
                // arguments expects a Map<String, String>
                arguments(mapOf("AROUTER_MODULE_NAME" to project.name))
                // register the Room schema provider
                compilerArgumentProviders(RoomSchemaArgProvider(File(projectDir, "schemas")))
            }
        }

        // only keep chinese resources and xxhdpi images
        resourceConfigurations.addAll(listOf("zh-rCN", "xxhdpi"))

        ndk {
            abiFilters += "arm64-v8a"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    // mimic applicationVariants.all { ... } from Groovy


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

//    kotlinOptions {
//        (this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions).jvmTarget = "21"
//    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            if (imitateIsApp) {
                manifest.srcFile("src/main/module/AndroidManifest.xml")
            } else {
                manifest.srcFile("src/main/AndroidManifest.xml")
            }

            // keep the original additional resource dir (note: directory should follow Android res conventions)
            res.srcDir("src/main/res/layouts/ninepoint")
        }
    }

    packagingOptions {
        resources {
            pickFirsts.add("META-INF/library_release.kotlin_module")
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    signingConfigs {
        this.getByName("debug") {
            keyAlias = (project.findProperty("IMITATE_DEBUG_KEY_ALIAS") as? String) ?: ""
            keyPassword = (project.findProperty("IMITATE_DEBUG_KEY_PASSWORD") as? String) ?: ""
            val storeFilePath = (project.findProperty("IMITATE_DEBUG_STORE_FILE") as? String)
            if (!storeFilePath.isNullOrBlank()) storeFile = file(storeFilePath)
            storePassword = (project.findProperty("IMITATE_DEBUG_STORE_PASSWORD") as? String) ?: ""
        }
    }

    namespace = "com.engineer.imitate"
}

kapt {
    arguments {
        arg("moduleName", project.name)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.glide)
    implementation(libs.androidx.legacy.support.v4)
    // keep version-catalog references as-is; they will resolve if the catalog is configured
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.arouter.api)
    add("kapt", libs.arouter.compiler)

    implementation(libs.facebook.fresco) {
        exclude(group = "com.facebook.soloader", module = "soloader")
        exclude(group = "com.facebook.fresco", module = "soloader")
        exclude(group = "com.facebook.fresco", module = "nativeimagefilters")
        exclude(group = "com.facebook.fresco", module = "nativeimagetranscoder")
        exclude(group = "com.facebook.fresco", module = "memory-type-native")
        exclude(group = "com.facebook.fresco", module = "imagepipeline-native")
    }
    implementation(libs.facebook.fresco.animated.webp)
    implementation(libs.facebook.fresco.webpsupport)
    implementation(libs.rebooters.matisse)
    implementation(libs.permissionx)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.stetho)
    implementation(libs.streamsupport)
    implementation("com.github.andrefrsousa:SuperBottomSheet:1.3.0")

    implementation(libs.circleimageview)
    implementation("androidx.viewpager:viewpager:1.1.0")
    implementation("com.afollestad:assent:2.3.1")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.github.prostory:AndroidZdog:v1.0.0")

    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.test:rules:1.6.1")
    testImplementation(libs.androidx.espresso.core)
    testImplementation("com.facebook.soloader:soloader:0.12.1")

    implementation(libs.commons.io)
    implementation(project(":subs:game"))
    implementation(project(":subs:gif-revert"))
    implementation(project(":subs:ai"))
    implementation(project(":subs:cpp_native"))
    implementation(project(":subs:compose"))

    implementation(libs.jaredrummler.simple.mvp)
    implementation(libs.dtoast)
    implementation(libs.page.indicator.view)

    implementation(libs.material.google)
    implementation(libs.androidx.preference.ktx)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava2)
    add("kapt", libs.androidx.room.compiler)

    implementation(libs.photo.viewer)
    implementation(libs.immersionbar)
    implementation(libs.fastjson.old)
    implementation(libs.gson)
    implementation(libs.dagger)
    add("kapt", libs.dagger.compiler)

    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    add("kapt", libs.moshi.kotlin.codegen)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}

apply(from = file("../gradle/report_apk_size_after_package.gradle"))
