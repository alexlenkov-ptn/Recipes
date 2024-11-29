plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.navigation.safeargs.kotlin) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false // todo подключить в toml
}
