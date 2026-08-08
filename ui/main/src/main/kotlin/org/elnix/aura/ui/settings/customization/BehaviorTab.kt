@file:Suppress("AssignedValueIsNeverRead")

package org.elnix.aura.ui.settings.customization

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.github.elnix90.runtime.asState
import kotlinx.coroutines.launch
import org.elnix.aura.enumsui.toggle.LockMethod.Device
import org.elnix.aura.enumsui.toggle.LockMethod.None
import org.elnix.aura.enumsui.toggle.LockMethod.Pattern
import org.elnix.aura.enumsui.toggle.LockMethod.Pin
import org.elnix.aura.i18n.R
import org.elnix.aura.settings.stores.map.BehaviorSettingsStore
import org.elnix.aura.settings.stores.map.PrivateSettingsStore
import org.elnix.aura.ui.dialogs.security.LockMethodDialog
import org.elnix.aura.ui.dragon.components.DragonSettingsGroup
import org.elnix.aura.ui.dragon.settings.Setting
import org.elnix.aura.ui.helpers.settings.SettingsItem
import org.elnix.aura.ui.helpers.settings.SettingsScaffold


@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun BehaviorTab() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val lockMethod by PrivateSettingsStore.lockMethod.asState()
    var showLockMethodPicker by remember { mutableStateOf(false) }


    SettingsScaffold(
        title = stringResource(R.string.behavior),
        helpText = stringResource(R.string.behavior_help),
        resetText = stringResource(R.string.reset_behavior_tab),
        onReset = {
            scope.launch {
                BehaviorSettingsStore.resetAll(ctx)
            }
        }
    ) {

        DragonSettingsGroup(R.string.common_settings) {
            Setting(BehaviorSettingsStore.keepScreenOn)
        }

        DragonSettingsGroup(R.string.security) {
            SettingsItem(
                title = stringResource(R.string.lock_method),
                description = when (lockMethod) {
                    None -> stringResource(R.string.lock_none)
                    Pin -> stringResource(R.string.lock_pin)
                    Device -> stringResource(R.string.lock_device_unlock)
                    Pattern -> stringResource(R.string.pattern)
                },
                icon = R.drawable.lock
            ) { showLockMethodPicker = true }
        }
    }

    if (showLockMethodPicker) {
        LockMethodDialog { showLockMethodPicker = false }
    }
}
