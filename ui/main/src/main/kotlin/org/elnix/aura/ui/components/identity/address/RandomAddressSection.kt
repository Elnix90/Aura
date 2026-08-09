package org.elnix.aura.ui.components.identity.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.elnix.aura.i18n.R
import org.elnix.aura.settings.stores.map.UiSettingsStore
import org.elnix.aura.ui.dragon.components.DragonButton
import org.elnix.aura.ui.dragon.settings.Setting

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
                LoadingIndicator(modifier = Modifier.size(20.dp))
                Text(
                    text = stringResource(R.string.generating_address),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

