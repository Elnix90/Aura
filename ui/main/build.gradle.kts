import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.compose)
    alias(libs.plugins.aura.serialization)
}

kotlin {
    explicitApi = ExplicitApiMode.Disabled
}

android {
    namespace = "org.elnix.aura.ui.main"
}

dependencies {
    implementation(libs.androidx.compose.animation.graphics)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.process)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.material3)
    implementation(libs.reorderable)
    implementation(libs.shapeindicators)
    implementation(libs.shizuku.api)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.collection)
    implementation(libs.androidx.compose.animation.core)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.runtime.saveable)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.ui.unit)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.lazycolumnscrollbar)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.timber)
    implementation(libs.bundles.kotlin)
    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.dragon.logging)
    implementation(libs.settings.core)
    implementation(libs.settings.runtime)
    implementation(libs.compose.lock)
    implementation(libs.colorpicker.compose)

    api(libs.androidx.activity.compose)
    api(libs.androidx.activity)
    api(libs.androidx.compose.animation)
    api(libs.androidx.compose.ui.geometry)
    api(libs.androidx.fragment)
    api(libs.androidx.lifecycle.common)
    api(libs.kotlinx.coroutines.core)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.runtime)

    implementation(project(":ui:base"))
    implementation(project(":ui:theme"))
    implementation(project(":ui:dragon"))

    api(project(":core:base"))
    api(project(":core:i18n"))
    api(project(":core:models"))
    api(project(":core:enumsui"))
    api(project(":core:settings"))
    implementation(project(":core:ktx"))
}
