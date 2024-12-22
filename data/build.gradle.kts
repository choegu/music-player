plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)

    // kapt
    alias(libs.plugins.kotlin.kapt)

    // hilt
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.choegozip.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 앱단 모듈
    implementation(project(":domain"))
    implementation(project(":data:playback"))

    // hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // datastore
    implementation(libs.datastore)

    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")

    // Media3 Session
    implementation("androidx.media3:media3-session:1.2.0")

    // Guava
    implementation("com.google.guava:guava:32.1.2-jre")

    // Guava와 Kotlin Coroutines 통합
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.1")

    // gson
    implementation("com.google.code.gson:gson:2.10.1")
}