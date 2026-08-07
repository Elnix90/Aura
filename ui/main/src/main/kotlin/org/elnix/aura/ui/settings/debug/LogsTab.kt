@file:Suppress("AssignedValueIsNeverRead")

package org.elnix.aura.ui.settings.debug

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.elnix90.logging.LOGS_TAG
import io.github.elnix90.logging.logD
import io.github.elnix90.logging.logE
import io.github.elnix90.logging.logLevelName
import io.github.elnix90.runtime.asState
import kotlinx.coroutines.launch
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.base.utils.CopyPasteUtils.copyToClipboard
import org.elnix.aura.base.utils.CopyPasteUtils.createShareableFile
import org.elnix.aura.base.utils.CopyPasteUtils.shareContent
import org.elnix.aura.base.utils.DateUtils.formatDateTime
import org.elnix.aura.base.utils.VersionsUtils.getVersionCode
import org.elnix.aura.base.utils.VersionsUtils.getVersionName
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.showToast
import org.elnix.aura.models.DragonLogViewModel
import org.elnix.aura.settings.stores.map.DebugSettingsStore
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.base.animation.Icon
import org.elnix.aura.ui.base.animation.rememberAnimatedIcon
import org.elnix.aura.ui.base.components.Spacer
import org.elnix.aura.ui.base.compositionlocals.LocalNavigator
import org.elnix.aura.ui.components.burger.MoreOptions
import org.elnix.aura.ui.dragon.components.DragonButton
import org.elnix.aura.ui.dragon.components.DragonIconButton
import org.elnix.aura.ui.dragon.components.DragonSettingsGroup
import org.elnix.aura.ui.dragon.dialogs.UserValidation
import org.elnix.aura.ui.dragon.expandable.ExpandableSection
import org.elnix.aura.ui.dragon.expandable.rememberExpandableSection
import org.elnix.aura.ui.dragon.settings.Setting
import org.elnix.aura.ui.dragon.text.TextWithDescription
import org.elnix.aura.ui.helpers.settings.SettingsScaffold
import java.io.File

@Composable
fun LogsTab(dragonLogViewModel: DragonLogViewModel = activityViewModel()) {
    val ctx = LocalContext.current
    val navigator = LocalNavigator.current
    val scope = rememberCoroutineScope()


    val enableLogging by DebugSettingsStore.enableLogging.asState()
    val filterTag by DebugSettingsStore.filterTag.asState()

    var tempFilterTag by remember(filterTag) { mutableStateOf(filterTag) }

    var refreshTrigger by remember { mutableIntStateOf(0) }
    val logFiles by produceState(initialValue = emptyList(), ctx, refreshTrigger) {
        value = dragonLogViewModel.getAllLogFiles()
    }

    var showDeleteDialog by remember { mutableStateOf<File?>(null) }

    val windowInfo = LocalWindowInfo.current
    val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memInfo = ActivityManager.MemoryInfo()
    am.getMemoryInfo(memInfo)
    val versionName = ctx.getVersionName()
    val versionCode = ctx.getVersionCode()



    val deviceDetails = remember {
        buildString {
            appendLine(" DEVICE DETAILS ")
            appendLine("System: ${Build.MANUFACTURER} ${Build.MODEL} (${Build.PRODUCT})")
            appendLine("OS: Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})")
            if (Build.VERSION.SECURITY_PATCH.isNotEmpty()) {
                appendLine("Security Patch: ${Build.VERSION.SECURITY_PATCH}")
            }
            appendLine("Arch: ${Build.SUPPORTED_ABIS.firstOrNull() ?: "unknown"}")
            appendLine("Display: ${windowInfo.containerSize.width}x${windowInfo.containerSize.height}px")
            appendLine(
                "RAM: %.1fGB used / %.1fGB total (%d%% available)".format(
                    (memInfo.totalMem - memInfo.availMem) / 1024.0 / 1024 / 1024,
                    memInfo.totalMem / 1024.0 / 1024 / 1024,
                    memInfo.availMem * 100 / memInfo.totalMem
                )
            )
            appendLine("App version: $versionName ($versionCode)")
        }
    }

    SettingsScaffold(
        title = "Logs",
        helpText = "Logs, need more info?",
        onReset = null,
        resetText = null,
        moreOptions = { dismiss ->
            listOf(
                MoreOptions(
                    text = { stringResource(R.string.refresh) },
                    onClick = {
                        refreshTrigger++; ctx.showToast("Refreshing...")
                        dismiss()
                    },
                    icon = R.drawable.refresh,
                )
            )
        }
    ) {
        ExpandableSection(rememberExpandableSection("Device info")) {
            Card(Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Device Information",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        DragonIconButton(
                            onClick = {
                                ctx.copyToClipboard(deviceDetails)
                                ctx.showToast("Device info copied")
                            },
                            icon = R.drawable.copy,
                            contentDescription = R.string.copy
                        )
                    }
                    Spacer(8.dp)
                    SelectionContainer {
                        Text(
                            text = deviceDetails,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        DragonSettingsGroup { Setting(DebugSettingsStore.enableLogging) }

        AnimatedVisibility(enableLogging) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                DragonSettingsGroup(R.string.log_level) {
                    Setting(
                        setting = DebugSettingsStore.snackBarLogLevel,
                        customDesc = { it.logLevelName }
                    )

                    Setting(
                        setting = DebugSettingsStore.filesLogLevel,
                        customDesc = { it.logLevelName }
                    )

                    val animatedIcon = rememberAnimatedIcon()
                    TextField(
                        value = tempFilterTag,
                        onValueChange = { tempFilterTag = it },
                        placeholder = { Text(stringResource(R.string.filter_tag)) },
                        colors = AppObjectsColors.outlinedTextFieldColors(
                            removeBorder = true
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(1f),
                        trailingIcon = {
                            animatedIcon.Icon(
                                defaultIcon = R.drawable.save,
                                enabled = tempFilterTag != filterTag
                            ) {
                                scope.launch {
                                    DebugSettingsStore.filterTag.set(ctx, tempFilterTag)
                                    animatedIcon.setSuccess()
                                }
                            }
                        }
                    )
                }

                DragonButton(
                    onClick = {
                        dragonLogViewModel.clearLogs()
                        refreshTrigger++
                    },
                    modifier = Modifier.padding(16.dp),
                    needConfirm = true,
                    confirmText = "Are you sure you want to delete all logs files?"
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete_forever),
                        contentDescription = "Delete"
                    )
                    Spacer(8.dp)
                    Text("Clear All Logs")
                }

                HorizontalDivider()

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 600.dp)
                ) {
                    items(logFiles) { file ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navigator.navigate(NavigationRoute.LogsViewer(file.name))
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(modifier = Modifier.weight(1f)) {
                                    TextWithDescription(
                                        text = file.name,
                                        description = "${(file.length() / 1024).toInt()}KB • ${
                                            file.lastModified().formatDateTime()
                                        }"
                                    )
                                }

                                Row(
                                    modifier = Modifier.padding(5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    DragonIconButton(
                                        icon = R.drawable.delete_forever,
                                        contentDescription = R.string.remove
                                    ) { showDeleteDialog = file }

                                    DragonIconButton(
                                        onClick = {
                                            ctx.copyToClipboard(dragonLogViewModel.readLogFile(file))
                                        },
                                        icon = R.drawable.copy,
                                        contentDescription = R.string.copy
                                    )

                                    DragonIconButton(
                                        icon = R.drawable.share,
                                        contentDescription = R.string.ok // I don't want to add a new string just for that
                                    ) { exportLogFile(dragonLogViewModel, ctx, file) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog != null) {
        val fileToDelete = showDeleteDialog!!

        UserValidation(
            title = "Delete file ${fileToDelete.name}",
            message = "THis can't be undone",
            onDismiss = { showDeleteDialog = null }
        ) {
            dragonLogViewModel.deleteLogFile(fileToDelete)
            refreshTrigger++
            showDeleteDialog = null
        }
    }
}

private fun exportLogFile(
    dragonLogViewModel: DragonLogViewModel,
    ctx: Context,
    file: File
) {
    try {
        val (shareFile, uri) = ctx.createShareableFile(file) ?: return

        ctx.shareContent(
            uri = uri,
            text = "Dragon Launcher logs",
            subject = "Dragon Logs - ${shareFile.name}",
            chooserTitle = "Share ${shareFile.name}"
        )

        logD(LOGS_TAG) { "Share opened: ${shareFile.name}" }

    } catch (e: SecurityException) {
        logE(LOGS_TAG, e) { "FileProvider not configured, falling back to text share" }

        // Fallback to text sharing
        val content = dragonLogViewModel.readLogFile(file)

        ctx.shareContent(
            text = content,
            subject = "Dragon Logs - ${file.name}",
            chooserTitle = "Share logs (text)"
        )
    } catch (e: Exception) {
        logE(LOGS_TAG, e) { "Failed to share log file" }
    }
}
