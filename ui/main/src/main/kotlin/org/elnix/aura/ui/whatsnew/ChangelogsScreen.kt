package org.elnix.aura.ui.whatsnew

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import org.elnix.aura.base.loader.loadChangelogs
import org.elnix.aura.base.utils.CopyPasteUtils.copyToClipboard
import org.elnix.aura.base.utils.VersionsUtils.getVersionCode
import org.elnix.aura.i18n.R
import org.elnix.aura.ui.helpers.settings.SettingsScaffold

@Composable
fun ChangelogsScreen() {
    val ctx = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val versionCode = ctx.getVersionCode()

    val updates by produceState(initialValue = emptyList()) {
        value = loadChangelogs(ctx, versionCode)
    }

    SettingsScaffold(
        title = stringResource(R.string.changelogs),
        helpText = stringResource(R.string.changelogs_help),
        resetText = null,
        onReset = null,
        lazyContent = {
            items(updates) { update ->

                val updateRegex: Regex = "[\\d-.]+".toRegex()
                val matchResult = updateRegex.find(update.versionName)

                val link = if (matchResult != null) {
                    "https://github.com/Elnix90/Dragon-Launcher/releases/tag/v${matchResult.value}"
                } else {
                    "https://github.com/Elnix90/Dragon-Launcher/releases/latest"
                }

                UpdateCard(
                    update,
                    onLongClick = {
                        ctx.copyToClipboard(link)
                    },
                    onClick = {
                        uriHandler.openUri(link)
                    }
                )
            }
        }
    )
}
