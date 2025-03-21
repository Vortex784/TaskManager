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
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    // ✅ Firebase & Google Sign-In dependencies
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.play.services.auth)
    implementation (libs.firebase.firestore)
    // AndroidX Credentials Library
    implementation(libs.credentials)

    // AndroidX Credentials Play Services Auth
    implementation(libs.androidx.credentials.play.services.auth)

    // Google ID SDK
    implementation(libs.googleid)
    implementation(libs.play.services.base)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)

}