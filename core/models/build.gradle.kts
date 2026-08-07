plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.serialization)
    alias(libs.plugins.aura.hilt)
}

android {
    namespace = "org.elnix.aura.models"

    packaging {
        jniLibs.pickFirsts += "META-INF/gradle/incremental.annotation.processors"
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.timber)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.compose.ui.unit)
    implementation(libs.androidx.core)
    implementation(libs.bundles.kotlin)
    implementation(libs.androidx.compose.material3)
    implementation(libs.dragon.logging)
    implementation(libs.settings.core)
    implementation(libs.settings.runtime)

    implementation(libs.hilt.core)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    api(libs.dagger)
    api(libs.javax.inject)

    implementation(project(":core:settings"))
    api(project(":core:base"))
    api(project(":core:i18n"))
    api(project(":core:enumsui"))
    api(project(":core:security"))
    api(project(":core:database"))
    api(project(":core:ktx"))
}
