package org.elnix.aura.ui.components.identity.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.elnix.aura.i18n.R
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.base.animation.Icon
import org.elnix.aura.ui.base.animation.rememberAnimatedIcon


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
        shape = MaterialTheme.shapes.extraLarge,
        trailingIcon = if (onShuffle != null) {
            {
                animatedIcon.Icon(R.drawable.shuffle) {
                    animatedIcon.setSuccess()
                    onShuffle()
                }
            }
        } else null,
        colors = AppObjectsColors.outlinedTextFieldColors(),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .then(focusModifier),
    )
}

