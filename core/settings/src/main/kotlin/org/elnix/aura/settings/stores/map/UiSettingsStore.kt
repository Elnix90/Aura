package org.elnix.aura.settings.stores.map

import io.github.elnix90.annotations.SettingKey
import io.github.elnix90.annotations.SettingsStore
import io.github.elnix90.core.objects.BooleanSettingObject
import io.github.elnix90.core.objects.boolean
import io.github.elnix90.core.stores.MapSettingsStore
import org.elnix.aura.i18n.R

@SettingsStore
public object UiSettingsStore : MapSettingsStore() {
    @SettingKey
    public val fullScreen: BooleanSettingObject = boolean(
        title = R.string.fullscreen_app,
        description = R.string.fullscreen_description,
        default = false
    )
}