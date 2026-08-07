plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.compose)
    alias(libs.plugins.aura.serialization)
}

android {
    namespace = "org.elnix.aura.base"
}

dependencies {
    implementation(libs.bundles.kotlin)

    implementation(libs.androidx.ui)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.material)
    implementation(libs.androidx.palette)
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.stringsimilarity)
    implementation(libs.dragon.logging)

    api(libs.androidx.ui.graphics)

    implementation(project(":core:ktx"))
    implementation(project(":core:i18n"))
}

