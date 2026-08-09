package org.elnix.aura.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.database.models.Identity
import org.elnix.aura.i18n.R
import org.elnix.aura.models.IdentitiesViewModel
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.base.components.AnimatedFab
import org.elnix.aura.ui.base.components.Spacer
import org.elnix.aura.ui.base.compositionlocals.LocalNavigator
import org.elnix.aura.ui.dragon.dialogs.UserValidation
import org.elnix.aura.ui.helpers.settings.SettingsScaffold
import org.elnix.aura.ui.helpers.settings.SpecialSettingsTitle
import org.elnix.aura.ui.svg.undraw404Error

/**
 * Main screen listing every saved identity.
 *
 * The list is fed by [IdentitiesViewModel]. An empty state is shown when there
 * are no identities yet. The FAB opens the editor for a new identity, tapping
 * an existing card opens the editor prefilled, and long pressing a card asks
 * for deletion confirmation before removing it.
 */
@Composable
fun MainScreen(
    identitiesViewModel: IdentitiesViewModel = activityViewModel()
) {
    val navigator = LocalNavigator.current

    val identities by identitiesViewModel.identities.collectAsState()

    var identityToDelete by remember { mutableStateOf<Identity?>(null) }

    SettingsScaffold(
        title = stringResource(R.string.identities),
        helpText = stringResource(R.string.identities_help),
        resetText = null,
        onReset = null,
        onBack = null,
        specialSettingsTitle = {
            SpecialSettingsTitle { navigator.navigate(NavigationRoute.Settings) }
        },
        scrollableContent = false,
        fab = {
            AnimatedFab(
                icon = R.drawable.add,
                minSize = 70.dp,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                navigator.navigate(NavigationRoute.EditIdentity(null))
            }
        }
    ) {
        AnimatedContent(identities.isEmpty()) { isEmpty ->
            if (isEmpty) {
                NoIdentitiesPlaceholder()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(identities, key = { it.entity.id }) { identity ->
                        IdentityCard(
                            identity = identity,
                            onClick = {
                                navigator.navigate(NavigationRoute.EditIdentity(identity.entity.id))
                            },
                            onLongClick = { identityToDelete = identity },
                        )
                    }
                }
            }
        }
    }

    identityToDelete?.let { identity ->
        UserValidation(
            title = stringResource(R.string.identity_delete_title),
            message = stringResource(R.string.identity_delete_message),
            validateText = stringResource(R.string.identity_delete),
            cancelText = stringResource(R.string.cancel),
            onDismiss = { identityToDelete = null },
        ) {
            identityToDelete = null
            identitiesViewModel.deleteIdentity(identity.entity.id)
        }
    }
}


@Composable
private fun NoIdentitiesPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                imageVector = undraw404Error(),
                contentDescription = null
            )
            Spacer(24.dp)
            Text(
                text = stringResource(R.string.no_identities_title),
                style = MaterialTheme.typography.titleLargeEmphasized,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(12.dp)
            Text(
                text = stringResource(R.string.no_identities_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        // That's hilarious
        Image(
            painter = painterResource(R.drawable.pointing_arrows),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer {
                    translationX = 100.dp.toPx()
                    translationY = 100.dp.toPx()
                    scaleY = 0.5f
                    scaleX = 0.5f
                }
                .align(Alignment.BottomEnd)
        )
    }
}
