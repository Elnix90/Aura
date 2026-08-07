package org.elnix.aura.ui.dragon.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.elnix.aura.theme.AppObjectsColors


@Composable
fun DragonIconButton(
    icon: Int,
    contentDescription: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = AppObjectsColors.iconButtonColors(),
    onClick: () -> Unit
) {

    DragonTooltip(contentDescription) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = colors,
            shapes = IconButtonDefaults.shapes()
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(contentDescription)
            )
        }
    }
}

