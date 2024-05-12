import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "co.binary.exploregithubandroid.core.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
    }

    // Load secrets.properties file
    val properties = Properties().apply {
        load(File("secrets.properties").inputStream())
    }

    val accessToken = properties.getProperty("GITHUB_ACCESS_TOKEN")
    val githubBaseUrl = properties.getProperty("GITHUB_BASE_URL")

    buildTypes.forEach {
        it.buildConfigField("String", "GITHUB_ACCESS_TOKEN", accessToken)
        it.buildConfigField("String", "GITHUB_BASE_URL", githubBaseUrl)
    }
}

dependencies {

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    testImplementation(libs.junit)
}