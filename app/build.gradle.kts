import java.util.Properties
import java.io.FileInputStream

val localProperties = Properties()

val localPropertiesFile = rootProject.file("local.properties")


if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.amazonpricetracker"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.amazonpricetracker"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val canopyApiKey = localProperties.getProperty("CANOPY_API_KEY",
            "YOUR_DEFAULT_API_KEY_IF_NOT_FOUND")
        buildConfigField("String", "CANOPY_API_KEY", "\"$canopyApiKey\"")

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
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    compileOnly ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}