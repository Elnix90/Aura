package org.elnix.aura.ui.dialogs.security

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import io.github.elnix90.lock.PinLock
import io.github.elnix90.lock.pin.PinIndicator
import io.github.elnix90.runtime.asMutableState
import io.github.elnix90.runtime.asState
import org.elnix.aura.i18n.R
import org.elnix.aura.models.SecurityViewModel
import org.elnix.aura.settings.stores.map.PrivateSettingsStore
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.dragon.dialogs.UserValidation


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private val okMaterialShapes = listOf(
    MaterialShapes.Circle,
    MaterialShapes.Gem,
    MaterialShapes.Arrow,
    MaterialShapes.Arch,
    MaterialShapes.Cookie4Sided,
    MaterialShapes.Cookie7Sided,
    MaterialShapes.Flower,
    MaterialShapes.Pentagon,
    MaterialShapes.Pill,
    MaterialShapes.Diamond
)

/**
 * Dialog for entering a PIN to unlock settings.
 */
@Composable
fun PinUnlock(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    securityViewModel: SecurityViewModel = activityViewModel()
) {
    val haptic = LocalHapticFeedback.current
    val pinHash by PrivateSettingsStore.lockHash.asState()

    var pin by remember { mutableStateOf("") }
    val pinShapes = remember { mutableStateListOf<RoundedPolygon>() }
    var failedTries by remember { mutableIntStateOf(0) }
    var pinError by remember { mutableStateOf<String?>(null) }

    val wrongPinText = stringResource(R.string.wrong_pin)

    PinPrompt(
        title = stringResource(R.string.unlock_settings),
        subtitle = stringResource(R.string.enter_pin),
        pinValue = pin,
        pinShapes = pinShapes,
        errorMessage = pinError,
        failedTries = failedTries,
        onPinChanged = { newValue ->
            pinError = null
            pin = newValue
            if (pinShapes.size < newValue.length) {
                repeat(newValue.length - pinShapes.size) {
                    pinShapes.add(okMaterialShapes.random())
                }
            } else {
                repeat(pinShapes.size - newValue.length) {
                    pinShapes.removeAt(pinShapes.lastIndex)
                }
            }
        },
        onDismiss = {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            onDismiss()
        }
    ) {
        if (securityViewModel.verify(pin, pinHash)) {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            onSuccess()
            pinShapes.clear()
            pin = ""
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.Reject)
            pinError = wrongPinText
            failedTries++
            pinShapes.clear()
            pin = ""
        }
    }
}


/**
 * Dialog for setting up a new PIN (enter + confirm).
 */
@Composable
fun PinSetup(
    onDismiss: () -> Unit,
    onPinSet: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    var firstPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var isConfirmStep by remember { mutableStateOf(false) }

    var showWarningDialog by remember { mutableStateOf(false) }
    var doNotRemindMeWarningDialog by PrivateSettingsStore.doNotRemindMeAgainPinLockWarning.asMutableState()

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var failedTries by remember { mutableIntStateOf(0) }
    val pinMismatch = stringResource(R.string.pin_mismatch)

    val pinShapes = remember(isConfirmStep) { mutableStateListOf<RoundedPolygon>() }
    val currentPin = if (isConfirmStep) confirmPin else firstPin

    PinPrompt(
        title = stringResource(R.string.set_pin),
        subtitle = if (isConfirmStep) stringResource(R.string.confirm_pin) else stringResource(R.string.enter_pin),
        pinValue = currentPin,
        pinShapes = pinShapes,
        errorMessage = errorMessage,
        failedTries = failedTries,
        onPinChanged = { newValue ->
            errorMessage = null
            if (pinShapes.size < newValue.length) {
                repeat(newValue.length - pinShapes.size) {
                    pinShapes.add(okMaterialShapes.random())
                }
            } else {
                repeat(pinShapes.size - newValue.length) {
                    pinShapes.removeAt(pinShapes.lastIndex)
                }
            }
            if (isConfirmStep) {
                confirmPin = newValue
            } else {
                firstPin = newValue
            }
        },
        onDismiss = {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            if (isConfirmStep) {
                isConfirmStep = false
                confirmPin = ""
                errorMessage = null
            } else {
                onDismiss()
            }
        }
    ) {
        if (!isConfirmStep) {
            isConfirmStep = true
            confirmPin = ""
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
        } else {
            when {
                // Error
                firstPin != confirmPin -> {
                    errorMessage = pinMismatch
                    confirmPin = ""
                    pinShapes.clear()
                    failedTries++
                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                }

                else -> {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)

                    if (doNotRemindMeWarningDialog) {
                        onPinSet(firstPin)
                    } else {
                        showWarningDialog = true
                    }
                }
            }
        }
    }

    if (showWarningDialog) {
        UserValidation(
            title = stringResource(R.string.pin_code_warning_titls),
            message = stringResource(R.string.pin_code_warning_desc),
            doNotRemindMeAgain = { doNotRemindMeWarningDialog = true },
            onDismiss = onDismiss
        ) { onPinSet(firstPin) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun PinPrompt(
    title: String,
    subtitle: String,
    pinValue: String,
    pinShapes: List<RoundedPolygon>,
    errorMessage: String? = null,
    failedTries: Int,
    minDigits: Int = 1,
    maxDigits: Int = Int.MAX_VALUE,
    onPinChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onPrimaryAction: () -> Unit
) {
    val horizontalOffsetError = remember {
        Animatable(
            initialValue = 0f
        )
    }

    LaunchedEffect(failedTries) {
        if (failedTries > 0) {
            var left = true
            repeat(5) {
                horizontalOffsetError.animateTo(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = LinearEasing
                    ),
                    targetValue = if (left) -5f
                    else 5f
                )
                left = !left
            }
            horizontalOffsetError.animateTo(0f)
        }
    }



    // Lock color animation system
    val defaultLockColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    val lockColor = remember {
        Animatable(
            initialValue = defaultLockColor
        )
    }

    LaunchedEffect(failedTries) {
        if (failedTries > 0) {
            lockColor.animateTo(errorColor)
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage == null) {
            lockColor.animateTo(defaultLockColor)
        }
    }

    BackHandler(onBack = onDismiss)


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBarsIgnoringVisibility
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    painter = painterResource(R.drawable.lock),
                    contentDescription = null,
                    tint = lockColor.value,
                    modifier = Modifier
                        .offset(x = horizontalOffsetError.value.dp)
                        .size(34.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface

                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                PinIndicator(pinShapes)

                AnimatedVisibility(errorMessage != null) {
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            PinLock(
                modifier = Modifier.fillMaxWidth(),
                onDigit = { digit ->
                    if (pinValue.length < maxDigits) {
                        onPinChanged(pinValue + digit)
                    }
                },
                validateEnabled = pinValue.length >= minDigits,
                onValidate = onPrimaryAction,
                backSpaceOrClose = pinValue.isNotEmpty(),
                onClear = {
                    if (pinValue.isEmpty()) onDismiss()
                    else onPinChanged("")
                }
            )
        }
    }
}

