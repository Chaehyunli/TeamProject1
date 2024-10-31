// Root build.gradle.kts
plugins {
    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    kotlin("jvm") version "1.9.22" apply false // 루트에서 공통 Kotlin 버전 설정
}

allprojects {

}

ext {
    set("kotlin_version", "1.9.22") // Kotlin 버전을 루트에서 정의
}
