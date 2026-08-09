package org.elnix.aura.ui.dragon.components

import androidx.compose.runtime.Composable
import org.elnix.aura.i18n.R

@Composable
fun ResetIcon(enabled: Boolean = true, onReset: () -> Unit) {
    DragonIconButton(
        icon = R.drawable.reset,
        contentDescription = R.string.reset,
        enabled = enabled,
        onClick = onReset
    )
}

@Composable
fun MoreIcon(enabled: Boolean = true, onReset: () -> Unit) {
    DragonIconButton(
        icon = R.drawable.more_vert,
        contentDescription = R.string.more,
        enabled = enabled,
        onClick = onReset
    )
}
@Composable
fun ShuffleIcon(onClick: () -> Unit) {
    DragonIconButton(
        icon = R.drawable.shuffle,
        contentDescription = R.string.shuffle_this_field,
        onClick = onClick
    )
}
