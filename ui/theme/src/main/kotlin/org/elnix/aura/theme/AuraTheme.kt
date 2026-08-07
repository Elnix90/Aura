package org.elnix.aura.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.elnix90.runtime.asState
import org.elnix.aura.enumsui.toggle.Theme
import org.elnix.aura.settings.stores.map.ColorModesSettingsStore


@Composable
private fun systemColorScheme(): ColorScheme {
    val context = LocalContext.current
    val darkTheme = isSystemInDarkTheme()
    val dynamicColors by ColorModesSettingsStore.dynamicColors.asState()

    return remember(darkTheme, dynamicColors, context) {
        when {
            dynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                if (darkTheme) {
                    dynamicDarkColorScheme(context)
                } else {
                    dynamicLightColorScheme(context)
                }

            darkTheme ->
                DarkDragonColorScheme

            else ->
                LightDragonColorScheme
        }
    }
}

@Composable
fun rememberCurrentColorScheme(): ColorScheme {
    val defaultTheme by ColorModesSettingsStore.defaultTheme.asState()
    val systemScheme = systemColorScheme()

    return remember(
        defaultTheme,
        systemScheme,
    ) {
        when (defaultTheme) {
            Theme.Light -> LightDragonColorScheme
            Theme.Dark -> DarkDragonColorScheme
            Theme.Amoled -> AmoledDragonColorScheme
            Theme.System -> systemScheme
        }
    }
}

@Composable
fun AuraTheme(content: @Composable () -> Unit) {
    MaterialExpressiveTheme(
        colorScheme = rememberCurrentColorScheme(),
        motionScheme = MotionScheme.expressive(),
        typography = Typography,
        content = content
    )
}
