plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.iptv.online.player.live.tv.smart"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.iptv.online.player.live.tv.smart"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.cronet)
    implementation(libs.material)
    implementation(libs.media3.common)
    implementation(libs.media3.datasource.okhttp)
    implementation(libs.media3.ui)
    implementation(libs.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    implementation("com.squareup.okhttp3:okhttp:5.5.1")
    implementation("com.google.code.gson:gson:2.15.0")
    implementation("com.github.bumptech.glide:glide:5.0.9")
//    implementation(libs.media3.exoplayer)
//    implementation(libs.media3.exoplayer.hls)
    implementation("androidx.media3:media3-exoplayer:1.11.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.11.1")
    implementation("androidx.media3:media3-ui:1.11.1")

}