
import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties

plugins {
    alias(libs.plugins.aura.application)
}


val dotenv = Properties().apply {
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        envFile.inputStream().use { load(it) }
    }
}

fun env(name: String): String? =
    System.getenv(name) ?: dotenv.getProperty(name)


extensions.configure<ApplicationExtension> {
    namespace = "org.elnix.aura"

    defaultConfig {
        applicationId = "org.elnix.aura"
        versionName = "1.0.0"
        versionCode = 1
    }

    val hasSigningConfig = env("KEYSTORE_FILE") != null &&
            env("KEYSTORE_PASSWORD") != null &&
            env("KEY_ALIAS") != null &&
            env("KEY_PASSWORD") != null

    signingConfigs {
        create("release") {
            if (hasSigningConfig) {
                storeFile = file(env("KEYSTORE_FILE")!!)
                storePassword = env("KEYSTORE_PASSWORD")!!
                keyAlias = env("KEY_ALIAS")!!
                keyPassword = env("KEY_PASSWORD")!!
            } else {
                println("No signing config found, APK will be unsigned")
            }
        }
    }

    buildTypes {
        if (hasSigningConfig) {
            // I use signing config because when I try to install the debug app, and that the release is installed, it won't work.
            getByName("release") {
                signingConfig = signingConfigs.getByName("release")
            }

            getByName("beta") {
                signingConfig = signingConfigs.getByName("release")
            }

            getByName("debug") {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.dagger)
    implementation(libs.hilt.core)
    implementation(libs.dragon.logging)
    implementation(libs.settings.core)
    implementation(libs.settings.runtime)

    ksp(libs.hilt.compiler)

    implementation(project(":ui:base"))
    implementation(project(":ui:main"))
    implementation(project(":ui:theme"))

    implementation(project(":core:models"))
    implementation(project(":core:settings"))
}


// Copy files in the fastlane/metadata dir to the assets folder, where they are compiled and added to the app
tasks.register<Copy>("copyChangelogsToAssets") {
    description = "Copy the fastlane changelogs folder directly to the assets folder to have them in-app"
    from("../fastlane/metadata/android/en-US/changelogs")
    into(file("src/main/assets/changelogs"))
    include("*.txt")
}
