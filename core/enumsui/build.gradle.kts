plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.compose)
}

android {
    namespace = "org.elnix.aura.enumsui"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    api(libs.androidx.compose.runtime)

    implementation(project(":core:i18n"))
    implementation(project(":core:base"))
}
