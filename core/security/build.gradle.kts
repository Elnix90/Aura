plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.serialization)
    alias(libs.plugins.aura.hilt)
}

android {
    namespace = "org.elnix.aura.services.security"

}

dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.androidx.biometric)
    implementation(libs.dragon.logging)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(project(":core:ktx"))
    implementation(project(":core:base"))
    implementation(project(":core:i18n"))
}