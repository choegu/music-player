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
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("com.google.dagger:hilt-android-testing:2.48")

    // Android Test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.0")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    androidTestImplementation("app.cash.turbine:turbine:0.12.1")

    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")
    testImplementation("org.robolectric:robolectric:4.10.3")

    testImplementation("org.mockito:mockito-android:5.5.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    androidTestImplementation("org.mockito:mockito-android:5.5.0")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
}