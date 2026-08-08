package org.elnix.aura.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.database.models.Identity
import org.elnix.aura.database.models.IdentityValues
import org.elnix.aura.i18n.R
import org.elnix.aura.models.IdentitiesViewModel
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.base.components.AnimatedFab
import org.elnix.aura.ui.base.compositionlocals.LocalNavigator
import org.elnix.aura.ui.dragon.components.DragonButton
import org.elnix.aura.ui.dragon.components.DragonModalBottomSheet
import org.elnix.aura.ui.helpers.settings.SettingsScaffold
import org.elnix.aura.ui.helpers.settings.SpecialSettingsTitle


@SuppressLint("LocalContextResourcesRead")
@Composable
fun MainScreen(
    identitiesViewModel: IdentitiesViewModel = activityViewModel()
) {
    val navigator = LocalNavigator.current

    val identities by identitiesViewModel.identities.collectAsState()

    var showAddNewSheet by rememberSaveable { mutableStateOf(false) }

    SettingsScaffold(
        title = stringResource(R.string.identities),
        helpText = "",
        resetText = null,
        onReset = null,
        onBack = null,
        specialSettingsTitle = {
            SpecialSettingsTitle { navigator.navigate(NavigationRoute.Settings) }
        },
        scrollableContent = true,
        lazyContent = {
            items(identities) { identity ->
                IdentityCard(identity)
            }
        },
        fab = {
//            var fabActivated by rememberSaveable { mutableStateOf(false) }

            AnimatedFab(
                icon = R.drawable.add,
                minSize = 70.dp,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) { showAddNewSheet = true }

//            ToggleAnimatedFab(
//                icon = { if (it) R.drawable.close else R.drawable.add },
//                minSize = 70.dp,
//                containerColor = MaterialTheme.colorScheme.primary,
//                checked = fabActivated,
//                onCheckedChange = {
//                    fabActivated = !fabActivated
//                }
//            )

//            FloatingActionButtonMenu(
//                expanded = fabActivated,
//                button = {
//
//                }
//            ) {
//
//            }
        }
    )

    if (showAddNewSheet) {
     AddNewIdentitySheet(
         onDismiss = {
             showAddNewSheet = false
         }
     ) {
         identitiesViewModel.createIdentity(it)
     }
    }
}


@Composable
fun IdentityCard(
    identity: Identity
) {
    val name = identity.name?.name ?: "<unknown>"

    Card {
        Text(name)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewIdentitySheet(
    onDismiss: () -> Unit,
    onAdd: (IdentityValues) -> Unit,
) {
    var values by remember { mutableStateOf(IdentityValues()) }

    DragonModalBottomSheet(onDismissRequest = onDismiss) {
        DragonButton(
            onClick = { onAdd(values) }
        ) {
            Text("Addddd")
        }
    }
}
