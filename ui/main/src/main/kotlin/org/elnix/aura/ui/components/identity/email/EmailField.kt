package org.elnix.aura.ui.components.identity.email

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import org.elnix.aura.i18n.R
import org.elnix.aura.ui.components.identity.common.IdentityTextField

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