package org.elnix.aura.settings.stores.map

import io.github.elnix90.annotations.SettingKey
import io.github.elnix90.annotations.SettingsStore
import io.github.elnix90.core.objects.BooleanSettingObject
import io.github.elnix90.core.objects.boolean
import io.github.elnix90.core.stores.MapSettingsStore
import org.elnix.aura.i18n.R

@SettingsStore
public object BehaviorSettingsStore : MapSettingsStore() {

    @SettingKey
    public val keepScreenOn: BooleanSettingObject = boolean(
        title = R.string.keep_screen_on,
        description = R.string.keep_screen_on_desc,
        default = false
    )

    @SettingKey
    public val disableHapticFeedbackGlobally: BooleanSettingObject = boolean(
        title = R.string.disable_haptic_globally,
        description = R.string.disable_haptic_globally_desc,
        default = false
    )
}