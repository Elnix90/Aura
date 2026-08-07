plugins {
    alias(libs.plugins.aura.library)
    alias(libs.plugins.aura.serialization)
    alias(libs.plugins.aura.hilt)
}

android {
    namespace = "org.elnix.aura.database"

    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.settings.core)
    implementation(libs.settings.runtime)

    api(libs.androidx.roomruntime)
    api(libs.androidx.room)
    ksp(libs.androidx.roomcompiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)


    implementation(project(":core:i18n"))
    implementation(project(":core:ktx"))
    implementation(project(":core:settings"))
    implementation(project(":core:base"))
}
