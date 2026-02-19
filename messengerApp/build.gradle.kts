import java.util.Properties
import java.util.Date
import java.text.SimpleDateFormat
import com.android.build.api.variant.FilterConfiguration

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

repositories {
    mavenCentral()
    google()
}

configurations.all {
    exclude(group = "com.google.firebase", module = "firebase-core")
    exclude(group = "androidx.recyclerview", module = "recyclerview")
    exclude(group = "com.android.support", module = "support-v4")
}

dependencies {
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.palette:palette:1.0.0")
    implementation("androidx.exifinterface:exifinterface:1.4.2")
    implementation("androidx.dynamicanimation:dynamicanimation:1.1.0")
    implementation("androidx.sharetarget:sharetarget:1.2.0")
    implementation("com.google.android.gms:play-services-measurement:23.0.0")

    compileOnly("org.checkerframework:checker-qual:3.53.1")
    compileOnly("org.checkerframework:checker-compat-qual:2.5.6")

    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging:25.0.1")
    implementation("com.google.firebase:firebase-config:23.0.1")
    implementation("com.google.firebase:firebase-datatransport:20.0.1")
    implementation("com.google.firebase:firebase-appindexing:20.0.0")
    implementation("com.google.android.gms:play-services-maps:20.0.0")
    implementation("com.google.android.gms:play-services-auth:21.5.0")
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.android.gms:play-services-wearable:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-wallet:19.5.0")
    implementation("com.googlecode.mp4parser:isoparser:1.1.22")
    implementation("com.stripe:stripe-android:22.7.0")
    implementation("com.google.mlkit:language-id:17.0.6")
    implementation("com.android.billingclient:billing:8.3.0")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("com.google.guava:guava:33.5.0-jre")

    val camerax_version = "1.5.3"
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-video:${camerax_version}")
    implementation("androidx.interpolator:interpolator:1.0.0")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    val room_version = "2.8.4"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation("dev.gustavoavila:java-android-websocket-client:2.0.2")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
}

val isPmBuild = "false"
val abis = mapOf(9 to "universal", 1 to "armeabi-v7a", 5 to "arm64-v8a", 2 to "x86", 6 to "x86_64")

val APP_VERSION_CODE: String by project
val APP_VERSION_NAME: String by project

android {
    compileSdk = 36
    namespace = "org.telegram.messenger"
    ndkVersion = "27.3.13750724"

    buildFeatures {
        buildConfig = true
        viewBinding = true
        resValues = true
    }

    defaultConfig {
        applicationId = "dev.gosserness.anogram"
        versionCode = Integer.parseInt(APP_VERSION_CODE)
        versionName = APP_VERSION_NAME

        minSdk = 21
        targetSdk = 33

        val localProperties = Properties()
        if (project.rootProject.file("API_KEYS").exists()) {
            localProperties.load(project.rootProject.file("API_KEYS").inputStream())
        }

        buildConfigField("int", "APP_ID", localProperties.getProperty("APP_ID", "12345"))
        buildConfigField("String", "APP_HASH", "\"" + localProperties.getProperty("APP_HASH", "0123456789abcdef0123456789abcdef") + "\"")
        buildConfigField("String", "AYU_VERSION", "\"" + SimpleDateFormat("yyyyMMdd").format(Date()) + "\"")
        buildConfigField 'String', 'GOOGLE_AUTH_CLIENT_ID', localProperties.getProperty("GOOGLE_AUTH_CLIENT_ID")

        resValue("string", "MAPS_V2_API", localProperties.getProperty("MAPS_V2_API", ""))

        // NDK build is now handled by Bazel
        /*
        externalNativeBuild {
            cmake {
                version = "3.10.2"
                arguments("-DANDROID_STL=c++_static", "-DANDROID_PLATFORM=android-21")
            }
        }
        */

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }

    /*
    externalNativeBuild {
        cmake {
            path = file("jni/CMakeLists.txt")
        }
    }
    */

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("jni")
            res.srcDirs("src/main/res", "src/main/res-solar", "src/main/res-tabs")
        }
        getByName("release") {
            manifest.srcFile("config/release/AndroidManifest.xml")
        }
        getByName("debug") {
            manifest.srcFile("config/debug/AndroidManifest.xml")
        }
    }

    lint {
        checkReleaseBuilds = false
        disable += setOf("MissingTranslation", "ExtraTranslation", "BlockedPrivateApi")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    signingConfigs {
        create("release") {
            val localProperties = Properties()
            if (project.rootProject.file("API_KEYS").exists()) {
                localProperties.load(project.rootProject.file("API_KEYS").inputStream())
            }
            storeFile = file("./kesgram.keystore")
            storePassword = localProperties.getProperty("SIGNING_KEY_STORE_PASSWORD")
            keyAlias = localProperties.getProperty("SIGNING_KEY_ALIAS")
            keyPassword = localProperties.getProperty("SIGNING_KEY_PASSWORD")
        }
    }

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isJniDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            multiDexEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            ndk.debugSymbolLevel = "FULL"
            buildConfigField("boolean", "DEBUG_VERSION", "true")
            buildConfigField("boolean", "IS_PM_BUILD", isPmBuild)
        }

        getByName("release") {
            isDebuggable = false
            isJniDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            multiDexEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            ndk.debugSymbolLevel = "FULL"
            buildConfigField("boolean", "DEBUG_VERSION", "false")
            buildConfigField("boolean", "IS_PM_BUILD", isPmBuild)
        }
    }

    flavorDimensions += "minApi"

    productFlavors {
        create("armv7") {
            dimension = "minApi"
            ndk.abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
            extra["abiVersionCode"] = 1
        }
        create("x86") {
            dimension = "minApi"
            ndk.abiFilters.addAll(listOf("x86", "x86_64"))
            extra["abiVersionCode"] = 2
        }
        create("arm64") {
            dimension = "minApi"
            ndk.abiFilters.add("arm64-v8a")
            extra["abiVersionCode"] = 5
        }
        create("x64") {
            dimension = "minApi"
            ndk.abiFilters.add("x86_64")
            extra["abiVersionCode"] = 6
        }
        create("afat") {
            dimension = "minApi"
            ndk.abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
            extra["abiVersionCode"] = 9
        }
        create("beta") {
            dimension = "minApi"
            ndk.abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
            extra["abiVersionCode"] = 9
        }
    }

    androidComponents {
        onVariants(selector().all()) { variant ->
            variant.outputs.forEach { output ->
                val flavorName = variant.productFlavors[0].name
                val buildTypePrefix = if (flavorName == "beta") "beta-" else ""

                val date = Date()
                val formattedDate = SimpleDateFormat("ddMMyyyy").format(date)

                val abiVersionCode = when(flavorName) {
                    "armv7" -> 1
                    "x86" -> 2
                    "arm64" -> 5
                    "x64" -> 6
                    "afat", "beta" -> 9
                    else -> 0
                }

                val abi = abis[abiVersionCode] ?: "universal"

                output.outputFileName.set("KESGram-${buildTypePrefix}${abi}-${formattedDate}.apk")
            }
        }

        beforeVariants(selector().all()) { variantBuilder ->
             val flavorNames = variantBuilder.productFlavors.map { it.second }
             val isBeta = flavorNames.contains("beta")
             val buildType = variantBuilder.buildType

             val ignore = (buildType != "debug" && isBeta) || (buildType != "release" && !isBeta)
             variantBuilder.enable = !ignore
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }
    dependenciesInfo {
        includeInApk = false
    }
}

configurations.getByName("implementation") {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
}
