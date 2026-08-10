package org.elnix.aura.settings.stores.map

import io.github.elnix90.annotations.SettingKey
import io.github.elnix90.annotations.SettingsStore
import io.github.elnix90.core.objects.boolean
import io.github.elnix90.core.objects.float
import io.github.elnix90.core.objects.int
import io.github.elnix90.core.stores.MapSettingsStore
import org.elnix.aura.i18n.R

@SettingsStore
object UiSettingsStore : MapSettingsStore() {
    @SettingKey
    val fullScreen = boolean(
        title = R.string.fullscreen_app,
        description = R.string.fullscreen_description,
        default = false
    )

    @SettingKey
    val radiusKm = int(
        title = R.string.address_radius,
        description = R.string.address_radius_desc,
        default = 10,
        allowedRange = 1..100
    )

    @SettingKey
    val linearInterpolationForCardsColor = float(
        title = R.string.linear_interpolation_for_card_colors,
        description = R.string.address_radius_desc,
        default = 0.5f,
        allowedRange = 0f..1f
    )
}