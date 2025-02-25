plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Firebase plugin
}

android {
    namespace = "com.maxte.taskmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maxte.taskmanager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.preference)
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    // ✅ Firebase & Google Sign-In dependencies
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    // AndroidX Credentials Library
    implementation("androidx.credentials:credentials:1.2.0")

    // AndroidX Credentials Play Services Auth
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0")

    // Google ID SDK
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation(libs.play.services.base)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)

}