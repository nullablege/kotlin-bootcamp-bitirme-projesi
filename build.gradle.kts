buildscript {
    dependencies {
        // Navigation Safe Args için classpath
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7") // Sürümü 2.7.7 yapın
    }

}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    // Safe Args eklentisini TOML'dan al
    alias(libs.plugins.androidx.navigation.safeArgs) apply false
}