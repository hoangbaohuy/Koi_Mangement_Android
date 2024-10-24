plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.koimanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.koimanagement"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Thêm Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("org.json:json:20210307")

    implementation ("com.auth0:java-jwt:3.18.2")

    // ViewPager2 dependency
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    // Rounded ImageView dependency
    implementation ("com.makeramen:roundedimageview:2.3.0")


}