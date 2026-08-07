package aura.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class AuraHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.google.devtools.ksp")
        target.pluginManager.apply("com.google.dagger.hilt.android")
    }
}
