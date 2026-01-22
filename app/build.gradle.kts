plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}


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

    compileSdk = libs.versions.compileSdk.get().toInt()

    // Define flavor dimensions at the android top-level (not inside defaultConfig)
    flavorDimensions.clear()
    flavorDimensions += "versionCode"

    defaultConfig {
        applicationId = "home.smart.fly.animations"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
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
    implementation(libs.gson)
    implementation(libs.androidx.appcompat)
    implementation(libs.material.google)
    implementation("com.orhanobut:logger:2.2.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.cardview)
    implementation(libs.glide)
    implementation(libs.okhttp)
    implementation(libs.okio)
    implementation(libs.multidex)
    implementation(libs.vectordrawable)
    implementation(libs.androidx.constraintlayout)

    implementation("com.github.bingoogolapple:BGARefreshLayout-Android:2.0.1")
    implementation("com.github.bingoogolapple:BGABanner-Android:3.0.1")
    implementation("com.github.bingoogolapple:BGABaseAdapter-Android:2.0.1")
    implementation("com.jakewharton:butterknife:10.2.3")
    implementation(libs.androidx.legacy.support.v4)
    kapt("com.jakewharton:butterknife-compiler:10.2.3")
    implementation(libs.stetho)
    implementation(libs.androidx.dynamicanimation)
    implementation(libs.lottie)
    implementation("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")
    implementation(libs.commons.io)

    implementation(libs.arouter.api)
    kapt(libs.arouter.compiler)

    implementation(libs.androidx.palette.ktx)
    implementation(libs.mmkv)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hugo.runtime)
    implementation(libs.facebook.fresco)
    implementation(libs.photoview)
    implementation(libs.rebooters.matisse)
    implementation(libs.permissionx)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.streamsupport)
    implementation(libs.picasso)
    implementation(libs.ucrop)
    // ShortcutBadger used with @aar classifier: leave explicit or migrate and add artifact block if needed
    implementation("me.leolin:ShortcutBadger:1.1.22@aar")
    implementation(libs.floatwindow)
    implementation(libs.transformationlayout)

    if (!(project.findProperty("imitate_isApp") as String).toBoolean()) {
        implementation(project(":imitate"))
    }
    implementation(project(":subs:dataview"))
    implementation(project(":subs:skeleton"))
    implementation(project(":subs:compose"))
    implementation(libs.fastjson.new)
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
