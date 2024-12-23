plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)

    // kapt
    alias(libs.plugins.kotlin.kapt)

    // hilt
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.choegozip.presentation"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 앱단 모듈
    implementation(project(":domain"))

    // hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // navigation
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)

    // orbit
    implementation(libs.orbit.core)
    implementation(libs.orbit.compose)
    implementation(libs.orbit.viewmodel)

    // coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    // 권한 요청
    implementation("io.github.ParkSangGwon:tedpermission-coroutine:3.4.2")

    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")

    // Media3 Session
    implementation("androidx.media3:media3-session:1.2.0")

    // Guava
    implementation("com.google.guava:guava:32.1.2-jre")

    // Guava와 Kotlin Coroutines 통합
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.1")

    // Unit Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("io.mockk:mockk:1.13.5")

    // Android Test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")
    testImplementation("org.robolectric:robolectric:4.10.3")
}