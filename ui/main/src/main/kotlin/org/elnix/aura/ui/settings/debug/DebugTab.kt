package org.elnix.aura.ui.settings.debug

import android.system.Os.kill
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.base.utils.LifecycleUtils
import org.elnix.aura.i18n.R
import org.elnix.aura.settings.AllStores
import org.elnix.aura.settings.stores.map.DebugSettingsStore
import org.elnix.aura.settings.stores.map.PrivateSettingsStore
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.dragon.components.DragonButton
import org.elnix.aura.ui.dragon.components.DragonSettingsGroup
import org.elnix.aura.ui.dragon.expandable.ExpandableSection
import org.elnix.aura.ui.dragon.expandable.rememberExpandableSection
import org.elnix.aura.ui.dragon.settings.Setting
import org.elnix.aura.ui.helpers.settings.RouteItem
import org.elnix.aura.ui.helpers.settings.SettingsScaffold

@Composable
fun DebugTab() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val storeResetSectionState = rememberExpandableSection(stringResource(R.string.store_reset))

    SettingsScaffold(
        title = stringResource(R.string.debug),
        helpText = "Advanced developer tools and system overrides.",
        onReset = null,
        resetText = null
    ) {
        DragonSettingsGroup { Setting(DebugSettingsStore.debugEnabled) }

        DragonSettingsGroup(R.string.logs) {
            RouteItem(NavigationRoute.Logs)
        }

        DragonSettingsGroup(R.string.ui_flow_and_debug) {
            DragonButton(
                onClick = { scope.launch { PrivateSettingsStore.lastSeenVersionCodeWhatsNew.reset(ctx) } },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Show What's New sheet")
            }

            DragonButton(
                onClick = { scope.launch { PrivateSettingsStore.lastSeenVersionCodeGoogleLockdownWarning.reset(ctx) } },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Show Google lockdown warning")
            }

            Setting(DebugSettingsStore.forceAppLanguageSelector)
            Setting(PrivateSettingsStore.hideBetaVersionWarning)
            Setting(DebugSettingsStore.showFps)
            Setting(DebugSettingsStore.showKillLauncherActionInActionPicker)
        }

        DragonSettingsGroup(R.string.risky) {
            DragonButton(
                onClick = {
                    @Suppress("DIVISION_BY_ZERO")
                    5 / 0
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) { Text(text = "What is 5 / 0? \uD83E\uDD2F") }

            DragonButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                onClick = { LifecycleUtils.closeApp(ctx as ComponentActivity) }
            ) { Text("Close app (gently)") }

            DragonButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = { kill(9, 9) }
            ) { Text("☠\uFE0F Kill Process") }
        }

        DragonSettingsGroup(R.string.dangerous_actions) {
            Setting(DebugSettingsStore.disableExtensionSignatureCheck)

            ExpandableSection(storeResetSectionState) {
                AllStores.forEach { store ->
                    DragonButton(
                        onClick = { scope.launch { store.resetAll(ctx) } },
                        colors = AppObjectsColors.cancelButtonColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text("Reset ${store.name}")
                    }
                }
            }
        }
    }
}