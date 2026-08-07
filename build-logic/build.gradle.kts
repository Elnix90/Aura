plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "org.elnix.aura.buildlogic"

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.gradle.api)
    compileOnly(libs.kotlin.gradle.plugin.api)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("auraLibrary") {
            id = "aura.library"
            implementationClass = "aura.buildlogic.AuraAndroidLibraryPlugin"
        }
        register("auraCompose") {
            id = "aura.compose"
            implementationClass = "aura.buildlogic.AuraAndroidComposePlugin"
        }
        register("auraSerialization") {
            id = "aura.serialization"
            implementationClass = "aura.buildlogic.AuraAndroidSerializationPlugin"
        }
        register("auraHilt") {
            id = "aura.hilt"
            implementationClass = "aura.buildlogic.AuraHiltPlugin"
        }
        register("auraApplication") {
            id = "aura.application"
            implementationClass = "aura.buildlogic.AuraAndroidApplicationPlugin"
        }
    }
}

