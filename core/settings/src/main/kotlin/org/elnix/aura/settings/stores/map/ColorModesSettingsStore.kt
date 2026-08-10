package org.elnix.aura.settings.stores.map

import io.github.elnix90.annotations.SettingKey
import io.github.elnix90.annotations.SettingsStore
import io.github.elnix90.core.objects.boolean
import io.github.elnix90.core.objects.enum
import io.github.elnix90.core.stores.MapSettingsStore
import org.elnix.aura.enumsui.toggle.Theme
import org.elnix.aura.i18n.R

@SettingsStore
object ColorModesSettingsStore : MapSettingsStore() {
    @SettingKey
    val theme = enum(Theme.Amoled)

    @SettingKey
    val dynamicColors = boolean(
        title = R.string.dynamic_colors,
        description = R.string.dynamic_colors_desc,
        default = false
    )

    /**
     * Whether to use my custom-made color schemes for objects, or the default Android colors schemes.
     * For ex: my switch uses no borders, and other colors channels than the default one, while the android one has borders
     * */
    @SettingKey
    val useCustomColorChannels = boolean(
        title = R.string.use_custom_color_channels,
        description = R.string.use_custom_color_channels_desc,
        default = true
    )
}