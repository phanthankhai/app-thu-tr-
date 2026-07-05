plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.smartrent"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.smartrent"
        minSdk = 24
        targetSdk = 37
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Retrofit & Gson để gọi API và tự động chuyển JSON thành Object
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp Logging để xem log API báo lỗi/thành công ngay trong Logcat (giống hệt Postman)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Thư viện Glide để tải ảnh từ mạng
    implementation("com.github.bumptech.glide:glide:4.16.0")
}