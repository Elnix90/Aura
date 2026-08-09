package org.elnix.aura.ui.components.identity.note

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.elnix.aura.i18n.R
import org.elnix.aura.theme.AppObjectsColors
import org.elnix.aura.ui.dragon.components.DragonSettingsGroup

@Composable
internal fun NotesField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    DragonSettingsGroup(R.string.notes) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(stringResource(R.string.note_placeholder)) },
            colors = AppObjectsColors.outlinedTextFieldColors(
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                onBackgroundColor = MaterialTheme.colorScheme.onSurface,
                removeBorder = true
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )
    }
}