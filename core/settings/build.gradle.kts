import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.serialization)
    alias(libs.plugins.settings) // My plugin 🤎
}

kotlin {
    explicitApi = ExplicitApiMode.Disabled
}

android {
    namespace = "org.elnix.aura.settings"
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.datastore.core)
    implementation(libs.timber)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.material3)

    api(libs.androidx.datastore.preferences.core)
    api(libs.kotlinx.coroutines.core)

    // My plugin 🤎
    implementation(libs.settings.core)
    implementation(libs.settings.runtime)
    implementation(libs.settings.annotations)

    implementation(libs.dragon.logging)

    api(project(":core:i18n"))
    api(project(":core:enumsui"))
    implementation(project(":core:base"))
}
