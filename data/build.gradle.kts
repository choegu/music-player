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

        testInstrumentationRunner = "com.choegozip.data.CustomRunner"
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
    implementation(libs.androidx.rules)
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
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // Media3 Session
    implementation(libs.androidx.media3.session)

    // Guava
    implementation(libs.guava)

    // Guava와 Kotlin Coroutines 통합
    implementation(libs.kotlinx.coroutines.guava)

    // gson
    implementation(libs.gson)

    // Unit Test
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockito.android)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)

    // Android Test
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.kotlin)
}