plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.kapslobby"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.kapslobby"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        ndk {
            abiFilters.addAll(setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
    }

    // --- YE NAYA BLOCK ADD KAREIN (Realme 32-bit Fix) ---
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    implementation("ai.nexconn.chat:chat:26.4.0") // (Apne dashboard ke "Nexconn docs" se exact version verify kar lein)
}