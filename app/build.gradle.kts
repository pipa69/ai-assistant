plugins {
    id("com.android.application")
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
}
