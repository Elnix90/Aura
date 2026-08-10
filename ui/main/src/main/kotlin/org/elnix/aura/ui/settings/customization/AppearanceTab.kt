package org.elnix.aura.ui.settings.customization

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.elnix90.runtime.asMutableStateNull
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.enumsui.toggle.Theme
import org.elnix.aura.i18n.R
import org.elnix.aura.models.IdentitiesViewModel
import org.elnix.aura.settings.stores.map.ColorModesSettingsStore
import org.elnix.aura.settings.stores.map.UiSettingsStore
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.base.animation.bouncySpec
import org.elnix.aura.ui.base.components.Spacer
import org.elnix.aura.ui.base.modifiers.conditional
import org.elnix.aura.ui.components.identity.card.IdentityCard
import org.elnix.aura.ui.dragon.components.DragonSettingsGroup
import org.elnix.aura.ui.dragon.settings.Setting
import org.elnix.aura.ui.helpers.settings.SettingsScaffold


@Composable
fun AppearanceTab(
    identitiesViewModel: IdentitiesViewModel = activityViewModel()
) {
    var defaultTheme by ColorModesSettingsStore.theme.asMutableStateNull()
    val identities by identitiesViewModel.identities.collectAsState()

    SettingsScaffold(
        title = stringResource(NavigationRoute.Appearance.resId),
        helpText = stringResource(R.string.appearance_tab_text),
        resetText = null,
        onReset = null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.large
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Theme.entries.forEach {
                val selected = it == defaultTheme

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.large)
                        .conditional(selected) {
                            background(MaterialTheme.colorScheme.surfaceDim)
                        }
                        .clickable { defaultTheme = it }
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val background = when (it) {
                        Theme.Dark -> Color.DarkGray
                        Theme.Light -> Color.White
                        Theme.System -> Brush.horizontalGradient(
                            colors = listOf(
                                Color.White,
                                Color.Black
                            )
                        )

                        Theme.Amoled -> Color.Black
                    }

                    // I like this simple animation I made, I think I've changed my mind about animations
                    val shapeCorners by animateIntAsState(
                        targetValue = if (selected) 12 else 50,
                        animationSpec = bouncySpec()
                    )

                    val scale by animateFloatAsState(
                        targetValue = if (selected) 1.2f else 1f,
                        animationSpec = bouncySpec()
                    )

                    val boxShape = RoundedCornerShape(shapeCorners)

                    Box((Modifier.scale(scale))) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(boxShape)
                                .then(
                                    when (background) {
                                        is Color -> Modifier.background(background)
                                        is Brush -> Modifier.background(background)
                                        else -> Modifier
                                    }
                                )
                                .border(
                                    1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), boxShape
                                )
                        )
                    }


                    Spacer(5.dp)

                    Text(
                        text = stringResource(it.resId),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        DragonSettingsGroup(R.string.special_options) {
            Setting(ColorModesSettingsStore.useCustomColorChannels)
            Setting(ColorModesSettingsStore.dynamicColors)
        }


        DragonSettingsGroup(R.string.main_screen) {
            Setting(UiSettingsStore.linearInterpolationForCardsColor)

            Spacer(20.dp)
            if (identities.isNotEmpty()) {
                IdentityCard(
                    identities.first(),
                    onDelete = { },
                    onClick = {},
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}
