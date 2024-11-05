plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // resources 디렉토리를 설정
    sourceSets {
        getByName("main") {
            resources.srcDirs("src/main/resources")
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI 및 관련 라이브러리
    implementation("androidx.compose.material3:material3:1.0.1") // material3 라이브러리
    implementation("androidx.compose.material:material-icons-extended:1.5.0") // material3 아이콘

    implementation("androidx.compose.foundation:foundation:1.5.0") // Foundation 라이브러리
    implementation("androidx.compose.ui:ui:1.5.0") // Compose UI 라이브러리
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0") // UI 툴링 프리뷰

    implementation("androidx.navigation:navigation-compose:2.5.3") // 하단 바 아이콘 누르면 다른 스크린으로 이동하게 설정
    

    // 기타 테스트 및 디버그 라이브러리
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0")
}
