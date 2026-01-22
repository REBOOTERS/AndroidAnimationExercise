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
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), file("proguard-rules.pro"))
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
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    // keep version-catalog references as-is; they will resolve if the catalog is configured
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.alibaba:arouter-api:1.5.2")
    add("kapt", "com.alibaba:arouter-compiler:1.5.2")

    implementation("com.facebook.fresco:fresco:3.6.0") {
        exclude(group = "com.facebook.soloader", module = "soloader")
        exclude(group = "com.facebook.fresco", module = "soloader")
        exclude(group = "com.facebook.fresco", module = "nativeimagefilters")
        exclude(group = "com.facebook.fresco", module = "nativeimagetranscoder")
        exclude(group = "com.facebook.fresco", module = "memory-type-native")
        exclude(group = "com.facebook.fresco", module = "imagepipeline-native")
    }
    implementation("com.facebook.fresco:animated-webp:3.6.0")
    implementation("com.facebook.fresco:webpsupport:3.6.0")
    implementation("com.github.REBOOTERS:Matisse:v0.6.0")
    implementation("com.guolindev.permissionx:permissionx:1.8.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.facebook.stetho:stetho:1.6.0")
    implementation("net.sourceforge.streamsupport:streamsupport:1.7.4")
    implementation("com.github.andrefrsousa:SuperBottomSheet:1.3.0")

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.viewpager:viewpager:1.1.0")
    implementation("com.afollestad:assent:2.3.1")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.github.prostory:AndroidZdog:v1.0.0")

    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.test:rules:1.6.1")
    testImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation("com.facebook.soloader:soloader:0.12.1")

    implementation(group = "commons-io", name = "commons-io", version = "20030203.000550")
    implementation(project(":subs:game"))
    implementation(project(":subs:gif-revert"))
    implementation(project(":subs:ai"))
    implementation(project(":subs:cpp_native"))
    implementation(project(":subs:compose"))

    implementation("com.jaredrummler:simple-mvp:1.0.2")
    implementation("com.github.Dovar66:DToast:1.1.5")
    implementation("com.github.romandanylyk:PageIndicatorView:v.1.0.3")

    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.preference:preference-ktx:1.2.1")

    implementation("androidx.room:room-runtime:2.8.0")
    implementation(libs.androidx.room.rxjava2)
    add("kapt", "androidx.room:room-compiler:2.8.0")

    implementation("com.github.wanglu1209:PhotoViewer:0.50")
    implementation("com.geyifeng.immersionbar:immersionbar:3.2.2")
    implementation("com.alibaba:fastjson:1.1.71.android")
    implementation("com.google.code.gson:gson:2.13.1")
    implementation(libs.dagger)
    add("kapt", libs.dagger.compiler)

    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    add("kapt", "com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.property("kotlin_coroutines")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${project.property("kotlin_coroutines_android")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}

apply(from = file("../gradle/report_apk_size_after_package.gradle"))
