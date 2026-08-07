plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.hilt)
}

android {
    namespace = "org.elnix.aura.i18n"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.commons.text) {
        exclude(group = "javax.script")
    }
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
