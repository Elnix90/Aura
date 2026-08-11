package org.elnix.aura.ui

import android.annotation.SuppressLint
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.database.models.Identity
import org.elnix.aura.database.models.IdentityValues
import org.elnix.aura.models.IdentitiesViewModel
import org.elnix.aura.ui.base.Navigator
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.base.compositionlocals.LocalNavigator
import org.elnix.aura.ui.base.compositionlocals.ProvideGlobalCompositionLocals
import org.elnix.aura.ui.dialogs.GoogleLockingWarningDialog
import org.elnix.aura.ui.helpers.FpsCounterGraph
import org.elnix.aura.ui.helpers.LauncherSnackbarHost
import org.elnix.aura.ui.base.animation.horizontalMetadata
import org.elnix.aura.ui.base.animation.verticalMetadata
import org.elnix.aura.ui.settings.customization.AppearanceTab
import org.elnix.aura.ui.settings.customization.BehaviorTab
import org.elnix.aura.ui.settings.debug.DebugTab
import org.elnix.aura.ui.settings.debug.LogsTab
import org.elnix.aura.ui.settings.debug.LogsViewerScreen
import org.elnix.aura.ui.warning.SignatureWarningDialog
import org.elnix.aura.ui.whatsnew.ChangelogsScreen
import org.elnix.aura.ui.whatsnew.WhatsNewBottomSheet


@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun MainAppUi(
    identitiesViewModel: IdentitiesViewModel = activityViewModel()
) {
    val startScreen = NavigationRoute.Main
    val backStack = rememberNavBackStack(startScreen)

    val navigator: Navigator = object : Navigator {
        override fun navigate(screen: NavigationRoute) {
            backStack.remove(screen)
            backStack.add(screen)
        }

        override fun onBack() {
            // Popping the only screen will crash so this avoids it
            if (backStack.size == 1) return
            backStack.removeLastOrNull()
        }

        override fun popBackMainScreen() {
            backStack.clear()
            backStack.add(NavigationRoute.Main)
        }
    }

    ProvideGlobalCompositionLocals {
        CompositionLocalProvider(
            LocalNavigator provides navigator
        ) {
            Scaffold(
                topBar = { FpsCounterGraph() },
                snackbarHost = { LauncherSnackbarHost() },
                contentWindowInsets = WindowInsets(),
                containerColor = MaterialTheme.colorScheme.background
            ) { paddingValues ->

                NavDisplay(
                    backStack = backStack,
                    modifier = Modifier.padding(paddingValues),
                    onBack = { navigator.onBack() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    predictivePopTransitionSpec = {
                        ContentTransform(
                            fadeIn(),
                            slideOutHorizontally { it },
                        )
                    },
                    popTransitionSpec = {
                        ContentTransform(
                            fadeIn(),
                            slideOutHorizontally { it },
                        )
                    },
                    entryProvider = entryProvider {
                        entry<NavigationRoute.Main>(metadata = verticalMetadata) { MainScreen() }
                        entry<NavigationRoute.Settings>(metadata = horizontalMetadata) { SettingsScreen() }
                        entry<NavigationRoute.Appearance>(metadata = horizontalMetadata) { AppearanceTab() }
                        entry<NavigationRoute.Behavior>(metadata = horizontalMetadata) { BehaviorTab() }
                        entry<NavigationRoute.Changelogs>(metadata = horizontalMetadata) { ChangelogsScreen() }
                        entry<NavigationRoute.Debug>(metadata = horizontalMetadata) { DebugTab() }
                        entry<NavigationRoute.Logs>(metadata = horizontalMetadata) { LogsTab() }
                        entry<NavigationRoute.LogsViewer>(metadata = horizontalMetadata) { key -> LogsViewerScreen(key.filename) }
                        entry<NavigationRoute.EditIdentity>(metadata = horizontalMetadata) { key ->
                            val isCreatingNew = key.id == null

                            val identity: State<Identity?>? = if (isCreatingNew) {
                                null
                            } else {
                                identitiesViewModel.observeIdentity(key.id!!).collectAsState(null)
                            }

                            IdentityEditorScreen(
                                initialValues = identity?.value?.toValues() ?: IdentityValues(),
                                isCreatingNew = isCreatingNew,
                                onSave = { values ->
                                    if (isCreatingNew) {
                                        identitiesViewModel.createIdentity(values) { navigator.onBack() }
                                    } else {
                                        identitiesViewModel.updateIdentity(key.id!!, values) { navigator.onBack() }
                                    }
                                },
                            )
                        }
                    }
                )

                WhatsNewBottomSheet()
                GoogleLockingWarningDialog()
                SignatureWarningDialog()
            }
        }
    }
}