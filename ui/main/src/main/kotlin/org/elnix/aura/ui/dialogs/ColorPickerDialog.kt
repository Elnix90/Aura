package org.elnix.aura.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.launch
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.randomColor
import org.elnix.aura.ui.base.animation.Icon
import org.elnix.aura.ui.base.animation.rememberAnimatedIcon


@Composable
fun ColorPickerDialog(
    initialColor: Color? = null,
    onDismissRequest: () -> Unit,
    onAddNewColor: (Int) -> Unit
) {
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()

    val controller = rememberColorPickerController()
    var hexCode by remember { mutableStateOf("") }

    val pastingIconStatus = rememberAnimatedIcon()
    val copyIconStatus = rememberAnimatedIcon()
    val randomColorStatus = rememberAnimatedIcon()


    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                painter = painterResource(R.drawable.colorize_filled),
                contentDescription = null
            )
        },
        title = { Text(stringResource(R.string.color_picker)) },
        confirmButton = {
            TextButton(
                onClick = {
                    onAddNewColor(controller.selectedColor.value.toArgb())
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        text = {
            val context = LocalContext.current

            fun copyHexTextToClipboard() {

            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                HsvColorPicker(
                    modifier = Modifier.height(250.dp),
                    controller = controller,
                    onColorChanged = {
                        hexCode = it.hexCode
                    },
                    initialColor = initialColor
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = ::copyHexTextToClipboard
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(MaterialTheme.shapes.large)
                            .background(controller.selectedColor.value)
                    )
                    Text("#$hexCode")
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    copyIconStatus.Icon(R.drawable.copy, onClick = ::copyHexTextToClipboard)

                    pastingIconStatus.Icon(R.drawable.paste) {
                        scope.launch {
                            val clip = clipboardManager.getClipEntry()?.clipData ?: return@launch
                            if (clip.itemCount == 0) return@launch
                            clip.getItemAt(0).coerceToText(context)?.toString()?.let { pasted ->
                                try {
                                    if (pasted.startsWith("#") && pasted.length == 9) {
                                        controller.selectByColor(Color(pasted.toColorInt()), true)
                                        pastingIconStatus.setSuccess()
                                    } else {
                                        pastingIconStatus.setError()
                                    }
                                } catch (_: Exception) {
                                    // Decrypt failed from clipboard, no need to log that
                                    pastingIconStatus.setError()
                                }
                            }
                        }
                    }

                    randomColorStatus.Icon(R.drawable.shuffle) {
                        val randomColor = randomColor(minLuminance = 1f)
                        controller.selectByColor(randomColor, true)
                        randomColorStatus.setSuccess()
                    }
                }
            }
        }
    )
}
