package org.elnix.aura.ui.base.compositionlocals

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import io.github.elnix90.runtime.asState
import org.elnix.aura.settings.stores.map.ColorModesSettingsStore


/**
 * Main Composition local provider, I just for everything I can here to avoid having to import them everywhere
 * I know that I should carefully review what global locals I add, but until now it worked to I'll keep it that way until I notice lag
 */
@Composable
fun ProvideGlobalCompositionLocals(content: @Composable () -> Unit) {
    val useCustomColorChannels by ColorModesSettingsStore.useCustomColorChannels.asState()

    CompositionLocalProvider(
        LocalUseCustomColorChannels provides useCustomColorChannels,
        content = content
    )
}