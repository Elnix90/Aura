package org.elnix.aura.ui.helpers.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.ktx.semiTransparentIfDisabled
import org.elnix.aura.ui.base.compositionlocals.LocalNavigator
import org.elnix.aura.ui.base.modifiers.conditional
import org.elnix.aura.ui.dragon.text.TextWithDescription

@Composable
fun SettingsItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true,
    icon: Int,
    trailingIcon: Int? = null,
    onLongClick: (() -> Unit)? = null,
    onExternalClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .combinedClickable(
                enabled = enabled,
                onLongClick = onLongClick,
                onClick = onClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.semiTransparentIfDisabled(enabled)
        )


        TextWithDescription(
            text = title,
            description = description,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )

        if (trailingIcon != null) {
            Icon(
                painter = painterResource(trailingIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.semiTransparentIfDisabled(enabled),
                modifier = Modifier
                    .sizeIn(maxHeight = 25.dp)
                    .conditional(onExternalClick != null) {
                        clickable(onClick = onExternalClick!!)
                    }
            )
        }
    }
}

@Composable
fun RouteItem(
    route: NavigationRoute,
    enabled: Boolean = true,
    onExternalClick: (() -> Unit)? = null
) {
    val navigator = LocalNavigator.current
    SettingsItem(
        title = stringResource(route.resId),
        enabled = enabled,
        onExternalClick = onExternalClick,
        icon = route.icon
    ) {
        navigator.navigate(route)
    }
}