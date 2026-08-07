package org.elnix.aura.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import io.github.elnix90.runtime.asStateNull
import kotlinx.coroutines.launch
import org.elnix.aura.base.utils.VersionsUtils.getVersionCode
import org.elnix.aura.settings.stores.map.PrivateSettingsStore
import org.elnix.aura.ui.base.asState
import org.elnix.aura.ui.warning.GoogleWarningDialog
import org.elnix.aura.ui.warning.GoogleWarningManager

@Composable
fun GoogleLockingWarningDialog() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentVersionCode = ctx.getVersionCode()

    val lastSeenVersionCodeGoogleLockdownWarning by PrivateSettingsStore.lastSeenVersionCodeGoogleLockdownWarning.asStateNull()
    val showWarning by GoogleWarningManager.showWarningDialog.asState()

    if (lastSeenVersionCodeGoogleLockdownWarning != null && (lastSeenVersionCodeGoogleLockdownWarning!! < currentVersionCode) && showWarning) {
        GoogleWarningDialog(
            onDismissRequest = {
                scope.launch {
                    PrivateSettingsStore.lastSeenVersionCodeGoogleLockdownWarning.set(ctx, currentVersionCode)
                }
                GoogleWarningManager.updateWarningDialog(false)
            }
        )
    }
}