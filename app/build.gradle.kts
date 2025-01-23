plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.example.newsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.newsapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = "your-key-alias"                // Replace with your key alias
            keyPassword = "your-key-password"          // Replace with your key password
            storeFile = file("C:/Users/eagal/AndroidStudioProjects/NewsApp/keystore.jks")
            storePassword = "password"                // Replace with your keystore password
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Firebase BOM for version compatibility
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))

    implementation("com.google.firebase:firebase-messaging")

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Google Sign-In SDK
    implementation("com.google.android.gms:play-services-auth:20.2.0")

    // Facebook Login SDK
    implementation("com.facebook.android:facebook-login:15.1.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("com.google.firebase:firebase-firestore-ktx:24.5.0")

    implementation("com.google.firebase:firebase-firestore:24.7.0")


    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.runtime.livedata)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation(libs.androidx.room.ktx)


    // Compose Dependencies
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.compose)

    // ViewModel (for observeAsState)
    // implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
   // implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    // Coil for Image Loading
   implementation(libs.coil.compose)

  //  implementation("com.kwabenaberko.newsapilib:newsapilib:1.2.0")

  //  implementation("com.github.User:Repo:Tag")// Replace with actual dependency
  //  implementation("com.github.KwabenBerko:News-API-Java:1.1.2")

    implementation(libs.androidx.datastore.preferences)

    // JSON serialization library, works with the Kotlin serialization plugin and add plugin too
    implementation(libs.kotlinx.serialization.json)

    // AndroidX and Compose dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.navigation.compose)

    // Navigation for Compose
    implementation(libs.navigation.compose.v273)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //noinspection GradleDependency
    implementation(libs.news.api.java)
    // Retrofit core library
    implementation(libs.retrofit)

    // Converter for parsing JSON responses
    implementation(libs.converter.gson)

    // Optional: OkHttp logging interceptor for debugging network calls
    implementation(libs.logging.interceptor)
    //noinspection UseTomlInstead
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")





    val nav_version = "2.8.5"

    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)



    //icon


    implementation(platform(libs.androidx.compose.bom)) // Automatically resolves all Compose versions
    // Material icons core
    implementation(libs.androidx.material.icons.core)

    // Optional: Material icons extended (for additional icons)
    implementation(libs.androidx.material.icons.extended)


}
fun kapt(s: String) {

}