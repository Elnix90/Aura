package org.elnix.aura.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.elnix.aura.i18n.R
import org.elnix.aura.settings.stores.map.UiSettingsStore
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.base.animation.Icon
import org.elnix.aura.ui.base.animation.rememberAnimatedIcon
import org.elnix.aura.ui.dragon.components.DragonButton
import org.elnix.aura.ui.dragon.settings.Setting


@Composable
internal fun IdentityTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onShuffle: (() -> Unit)?,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    readOnly: Boolean = false,
    onFocusChanged: ((Boolean) -> Unit)? = null,
) {
    val focusModifier = if (onFocusChanged != null) {
        Modifier.onFocusChanged { onFocusChanged(it.isFocused) }
    } else {
        Modifier
    }

    val animatedIcon = rememberAnimatedIcon()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) {
            { Text(placeholder) }
        } else null,
        singleLine = singleLine,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = shape,
        trailingIcon = if (onShuffle != null) {
            {
                animatedIcon.Icon(R.drawable.shuffle) {
                    animatedIcon.setSuccess()
                    onShuffle()
                }
            }
        } else null,
        colors = AppObjectsColors.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            onBackgroundColor = MaterialTheme.colorScheme.onSurface,
            removeBorder = false,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .then(focusModifier),
    )
}

/**
 * Two part email field, split around the @ sign.
 */
@Composable
internal fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    onShuffle: () -> Unit,
) {
    IdentityTextField(
        value = value,
        onValueChange = onValueChange,
        onShuffle = onShuffle,
        label = stringResource(R.string.identity_email),
        placeholder = stringResource(R.string.email_placeholder),
        keyboardType = KeyboardType.Email,
        modifier = Modifier.fillMaxWidth(1f)
    )
}

/**
 * Multiline note field.
 */
@Composable
internal fun NotesField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    IdentityTextField(
        value = value,
        onValueChange = onValueChange,
        onShuffle = null,
        label = stringResource(R.string.identity_custom_note),
        singleLine = false,
        modifier = Modifier.fillMaxWidth(),
    )
}

/**
 * Text field with autocomplete suggestions filtered from [suggestions].
 */
@Composable
internal fun AutocompleteIdentityField(
    value: String,
    onValueChange: (String) -> Unit,
    onShuffle: (() -> Unit)?,
    label: String,
    suggestions: List<String>,
    modifier: Modifier = Modifier,
    placeholder: String = "",
) {
    var isFocused by remember { mutableStateOf(false) }
    val filtered = remember(value, suggestions) {
        if (value.isBlank()) {
            emptyList()
        } else {
            suggestions.filter { it.startsWith(value, ignoreCase = true) }.take(6)
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        IdentityTextField(
            value = value,
            onValueChange = onValueChange,
            onShuffle = onShuffle,
            label = label,
            placeholder = placeholder,
            onFocusChanged = { isFocused = it },
        )
        DropdownMenu(
            expanded = isFocused && filtered.isNotEmpty(),
            onDismissRequest = { isFocused = false },
        ) {
            filtered.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(suggestion) },
                    onClick = {
                        onValueChange(suggestion)
                        isFocused = false
                    },
                )
            }
        }
    }
}

/**
 * Radius slider and generate button for the OpenStreetMap random address.
 */
@Composable
internal fun RandomAddressSection(
    onGenerate: () -> Unit,
    isLoading: Boolean,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Setting(UiSettingsStore.radiusKm)

        DragonButton(
            onClick = onGenerate,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp),
        ) {
            Text(stringResource(R.string.random_address_nearby))
        }
        if (isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                )
                Text(
                    text = stringResource(R.string.generating_address),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}


