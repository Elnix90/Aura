package org.elnix.aura.ui.settings.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.elnix.aura.base.utils.CopyPasteUtils.copyToClipboard
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.showToast
import org.elnix.aura.models.DragonLogViewModel
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.components.burger.MoreOptions
import org.elnix.aura.ui.helpers.MonospaceScrollableText
import org.elnix.aura.ui.helpers.settings.SettingsScaffold
import java.io.File

@Composable
fun LogsViewerScreen(
    filename: String,
    dragonLogViewModel: DragonLogViewModel = activityViewModel()
) {
    val ctx = LocalContext.current

    val file = File(ctx.filesDir, "logs/$filename")

    var logs: String by remember(filename) { mutableStateOf("") }
    LaunchedEffect(Unit) {
        logs = dragonLogViewModel.readLogFile(file)
    }
    val lines by remember(logs) { derivedStateOf { logs.lines() } }

    val helpText = "Viewing logs from the log file: $filename\n - ${lines.size} total lines\n - ${logs.length} total chars"

    SettingsScaffold(
        title = filename,
        helpText = helpText,
        onReset = null,
        resetText = null,
        scrollableContent = false,
        moreOptions = { dismiss ->
            listOf(
                MoreOptions(
                    text = { stringResource(R.string.copy) },
                    onClick = {
                        ctx.copyToClipboard(logs)
                        ctx.showToast("Copied to clipboard")
                        dismiss()
                    },
                    icon = R.drawable.copy,
                )
            )
        }
    ) {
        MonospaceScrollableText(lines, useDragonLogsColoration = true)
    }
}