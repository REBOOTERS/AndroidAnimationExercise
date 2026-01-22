plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

//import com.android.build.api.instrumentation.FramesComputationMode
//import com.android.build.api.instrumentation.InstrumentationScope
//import com.engineer.plugin.transforms.FooClassVisitorFactory
//import com.engineer.plugin.transforms.track.TrackClassVisitorFactory
//import com.engineer.plugin.transforms.tiger.TigerClassVisitorFactory

// Helper to read properties from gradle.properties (strings) and convert when needed
val compileSdkProp = (project.findProperty("compileSdk") as String).toInt()
val minSdkProp = (project.findProperty("minSdk") as String).toInt()
val targetSdkProp = (project.findProperty("targetSdk") as String).toInt()

android {
    signingConfigs {
        // Avoid creating a duplicate debug SigningConfig if it already exists (some AGP versions provide a default)
        val existingDebug = findByName("debug")
        if (existingDebug != null) {
            existingDebug.apply {
                keyAlias = "animation"
                keyPassword = "123456"
                storeFile = file("$rootDir/animationkey")
                storePassword = "123456"
            }
        } else {
            create("debug") {
                keyAlias = "animation"
                keyPassword = "123456"
                storeFile = file("$rootDir/animationkey")
                storePassword = "123456"
            }
        }
    }

    compileSdk = compileSdkProp

    // Define flavor dimensions at the android top-level (not inside defaultConfig)
    flavorDimensions.clear()
    flavorDimensions += "versionCode"

    defaultConfig {
        applicationId = "home.smart.fly.animations"
        minSdk = minSdkProp
        targetSdk = targetSdkProp
        versionCode = 5
        versionName = "5.0"
        renderscriptTargetApi = 19
        renderscriptSupportModeEnabled = true
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true

        javaCompileOptions {
            annotationProcessorOptions {
                // Kotlin DSL expects map-like arguments
                arguments(mapOf("AROUTER_MODULE_NAME" to project.name))
            }
        }

        // aaptOptions.cruncherEnabled was removed in newer AGP; keep current behavior by not enabling cruncher
        // (no-op)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), file("proguard-rules.pro"))
        }
        getByName("debug") {
            // splits is a property; access via named API
            splits.abi.isEnable = false
            splits.density.isEnable = false
        }
    }

    // set product flavors and assign the correct dimension and resource configs
    productFlavors {
        create("free") {
            dimension = "versionCode"
            versionNameSuffix = "_free"
            // use resConfigs to filter packaged resources (replacement for resourceConfigurations)
            resConfigs("ch", "xhdpi")
            minSdk = 26
            extra["alwaysUpdateBuildId"] = false
        }

        create("charge") {
            dimension = "versionCode"
            versionNameSuffix = "_charge"
            resConfigs("en", "xhdpi")
            minSdk = 26
            extra["alwaysUpdateBuildId"] = false
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    buildFeatures {
        viewBinding = true
    }

    namespace = "home.smart.fly.animations"
    lint {
        abortOnError = false
    }
}

configurations.all {
    // keep placeholder for resolutionStrategy if needed
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("androidx.appcompat:appcompat:${project.property("androidx_appcompat")}")
    implementation("com.google.android.material:material:${project.property("androidx_material")}")
    implementation("com.orhanobut:logger:2.2.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.bumptech.glide:glide:${project.property("glide")}")
    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    implementation("com.squareup.okio:okio:3.15.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.vectordrawable:vectordrawable:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:${project.property("constraint_layout")}")

    implementation("com.github.bingoogolapple:BGARefreshLayout-Android:2.0.1")
    implementation("com.github.bingoogolapple:BGABanner-Android:3.0.1")
    implementation("com.github.bingoogolapple:BGABaseAdapter-Android:2.0.1")
    implementation("com.jakewharton:butterknife:10.2.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt("com.jakewharton:butterknife-compiler:10.2.3")
    implementation("com.facebook.stetho:stetho:1.6.0")
    implementation("androidx.dynamicanimation:dynamicanimation:1.1.0")
    implementation("com.airbnb.android:lottie:6.6.7")
    implementation("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")
    implementation("net.sourceforge.streamsupport:streamsupport:1.7.4")

    implementation("com.alibaba:arouter-api:${project.property("arouter_api")}")
    kapt("com.alibaba:arouter-compiler:${project.property("arouter_compiler")}")

    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("com.tencent:mmkv:2.2.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.jakewharton.hugo:hugo-runtime:1.2.1")

    implementation("com.facebook.fresco:fresco:3.6.0")
    implementation("com.github.chrisbanes:PhotoView:2.1.4")
    implementation("com.github.REBOOTERS:Matisse:v0.6.0")
    implementation("com.guolindev.permissionx:permissionx:1.8.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.github.yalantis:ucrop:2.2.1")
    implementation("me.leolin:ShortcutBadger:1.1.22@aar")
    implementation("com.github.yhaolpz:FloatWindow:1.0.9")
    implementation("com.github.skydoves:transformationlayout:1.1.5")

    if (!(project.findProperty("imitate_isApp") as String).toBoolean()) {
        implementation(project(":imitate"))
    }
    implementation(project(":subs:dataview"))
    implementation(project(":subs:skeleton"))
    implementation(project(":subs:compose"))
    implementation("com.alibaba:fastjson:2.0.57")
}


// Remove androidComponents instrumentation transforms for now (migrating custom transforms to KTS requires more work).
// If you need these transforms, we can port them carefully to the new AGP instrumentation API.
// androidComponents {
//    onVariants(selector().all()) {
//        instrumentation.transformClassesWith(FooClassVisitorFactory::class.java, InstrumentationScope.PROJECT) {}
//        instrumentation.transformClassesWith(TrackClassVisitorFactory::class.java, InstrumentationScope.PROJECT) { param ->
//            param.trackOn = true
//        }
//        instrumentation.transformClassesWith(TigerClassVisitorFactory::class.java, InstrumentationScope.PROJECT) { param ->
//            param.tigerOn = true
//        }
//        instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
//    }
// }

// apply Groovy script fragments (keep existing behavior)
apply(from = file("../gradle/funs.gradle"))
apply(from = file("../gradle/report_apk_size_after_package.gradle"))
