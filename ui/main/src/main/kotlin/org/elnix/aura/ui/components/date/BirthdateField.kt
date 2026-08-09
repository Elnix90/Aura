package org.elnix.aura.ui.components.date

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SplitButton
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.elnix.aura.i18n.R
import org.elnix.aura.ui.base.animation.barsContentTransform
import org.elnix.aura.ui.base.animation.bouncySpec
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.milliseconds

/**
 * Birthdate field that opens the Material date picker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BirthdateField(
    value: String,
    onValueChange: (String) -> Unit,
    onShuffle: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var showPicker by remember { mutableStateOf(false) }
    val description = stringResource(R.string.open)

    val rotation = remember { Animatable(0f) }

    SplitButton(
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                onClick = { showPicker = true }
            ) {
                AnimatedContent(
                    targetState = value,
                    transitionSpec = { barsContentTransform }
                ) { value ->
                    Text(value.ifEmpty { stringResource(R.string.pick_a_date) })
                }
            }
        },
        // Icon-only trailing button should have a tooltip for a11y.
        trailingButton = {
            TooltipBox(
                positionProvider =
                    TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = {
                    PlainTooltip(
                        modifier =
                            Modifier.semantics {
                                liveRegion = LiveRegionMode.Assertive
                                paneTitle = description
                            }
                    ) {
                        Text(description)
                    }
                },
                state = rememberTooltipState(),
            ) {
                SplitButtonDefaults.TrailingButton(
                    checked = rotation.value != 0f,
                    onCheckedChange = {
                        scope.launch {
                            rotation.animateTo(
                                targetValue = 360f,
                                animationSpec = bouncySpec()
                            )
                            rotation.snapTo(0f)
                        }
                        scope.launch {
                            delay(200.milliseconds)
                            onShuffle()
                        }
                    },
                    modifier = Modifier.semantics {
                        stateDescription = if (showPicker) "Expanded" else "Collapsed"
                        contentDescription = description
                    }
                ) {

                    Icon(
                        painter = painterResource(R.drawable.shuffle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(SplitButtonDefaults.TrailingIconSize)
                            .graphicsLayer {
                                this.rotationZ = rotation.value
                            }
                    )
                }
            }
        }
    )


    if (showPicker) {
        val initialMillis = runCatching {
            LocalDate.parse(value).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        }.getOrNull()
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        onValueChange(date.toString())
                    }
                    showPicker = false
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        ) {
            DatePicker(state = pickerState, showModeToggle = false)
        }
    }
}
