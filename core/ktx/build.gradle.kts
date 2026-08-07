plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.compose)
}

android {
    namespace = "org.elnix.aura.ktx"
}

dependencies {
    implementation(libs.bundles.kotlin)

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.androidx.lifecycle)

    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.annotation)
    implementation(libs.androidx.compose.ui.unit)
    implementation(libs.androidx.ui)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.dragon.logging)

    testImplementation(libs.bundles.test)
    testImplementation(libs.androidx.compose.ui.geometry)
    testImplementation(libs.kotlinx.coroutines.core)

    testDebugImplementation(libs.robolectric)
}
