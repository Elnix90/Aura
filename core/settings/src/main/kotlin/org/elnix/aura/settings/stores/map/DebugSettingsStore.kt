package org.elnix.aura.settings.stores.map

import android.util.Log
import io.github.elnix90.annotations.SettingKey
import io.github.elnix90.annotations.SettingsStore
import io.github.elnix90.core.objects.boolean
import io.github.elnix90.core.objects.int
import io.github.elnix90.core.objects.string
import io.github.elnix90.core.stores.MapSettingsStore
import org.elnix.aura.i18n.R

@SettingsStore
object DebugSettingsStore : MapSettingsStore() {

    @SettingKey
    val debugEnabled = boolean(
        title = R.string.activate_debug_mode,
        description = R.string.activate_debug_mode_desc,
        default = false
    )

    @SettingKey
    val forceAppLanguageSelector = boolean(
        title = R.string.force_app_language_selector,
        description = R.string.force_app_language_selector_desc,
        default = false
    )

    @SettingKey
    val enableLogging = boolean(
        title = R.string.enable_logging,
        description = R.string.enable_logging_desc,
        default = true
    )

    @SettingKey
    val disableExtensionSignatureCheck = boolean(
        title = R.string.disable_extension_signature_check,
        description = R.string.disable_extension_signature_check_desc,
        default = false
    )

    @SettingKey
    val snackBarLogLevel = int(
        title = R.string.snackbar_log_level,
        description = R.string.snackbar_log_level_desc,
        default = 8, // No logs
        allowedRange = 2..8
    )

    @SettingKey
    val filesLogLevel = int(
        title = R.string.files_log_level,
        description = R.string.files_log_level_desc,
        default = Log.DEBUG,
        allowedRange = 2..8
    )

    @SettingKey
    val filterTag = string(
        title = R.string.filter_tag,
        default = ""
    )

    @SettingKey
    val showFps = boolean(
        title = R.string.show_fps,
        description = R.string.show_fps_desc,
        default = false
    )

    @SettingKey
    val showKillLauncherActionInActionPicker = boolean(
        title = R.string.show_kill_launcher_action,
        description = R.string.show_kill_launcher_action_desc,
        default = false
    )
}