package org.elnix.aura.settings.stores.map

import io.github.elnix90.annotations.SettingKey
import io.github.elnix90.annotations.SettingsStore
import io.github.elnix90.core.objects.boolean
import io.github.elnix90.core.stores.MapSettingsStore
import org.elnix.aura.i18n.R

@SettingsStore
object BehaviorSettingsStore : MapSettingsStore() {
    @SettingKey
    val keepScreenOn = boolean(
        title = R.string.keep_screen_on,
        description = R.string.keep_screen_on_desc,
        default = false
    )
}