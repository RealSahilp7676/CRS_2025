plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.crs2025"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.crs2025"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.espresso.idling.resource)
    implementation(platform(libs.firebase.bom))
    implementation(libs.ext.junit)
    implementation(libs.espresso.contrib)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.intents)
    androidTestImplementation(libs.core)
    androidTestImplementation(libs.rules)

    // Enforce Protobuf version
    implementation("com.google.protobuf:protobuf-javalite:3.25.1")

    // Exclude conflicting protobuf-lite from all Firebase dependencies
    configurations.all {
        exclude (group= "com.google.protobuf", module= "protobuf-lite")
    }

    // Optional: Force all protobuf dependencies to use javalite 3.25.1
    configurations.all {
        resolutionStrategy {
            force ("com.google.protobuf:protobuf-javalite:3.25.1")
        }
    }
}