@file:Suppress("AssignedValueIsNeverRead")

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.github.elnix90.logging.SECURITY_SERVICE
import io.github.elnix90.logging.logD
import io.github.elnix90.runtime.asState
import io.github.elnix90.runtime.asStateNull
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.enumsui.toggle.LockMethod.Device
import org.elnix.aura.enumsui.toggle.LockMethod.None
import org.elnix.aura.enumsui.toggle.LockMethod.Pattern
import org.elnix.aura.enumsui.toggle.LockMethod.Pin
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.findFragmentActivity
import org.elnix.aura.ktx.showToast
import org.elnix.aura.models.SecurityViewModel
import org.elnix.aura.settings.stores.map.PrivateSettingsStore
import org.elnix.aura.ui.base.Navigator
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.base.asState
import org.elnix.aura.ui.base.compositionlocals.LocalNavigator
import org.elnix.aura.ui.base.compositionlocals.ProvideGlobalCompositionLocals
import org.elnix.aura.ui.dialogs.GoogleLockingWarningDialog
import org.elnix.aura.ui.dialogs.security.PatternUnlock
import org.elnix.aura.ui.dialogs.security.PinUnlock
import org.elnix.aura.ui.helpers.FpsCounterGraph
import org.elnix.aura.ui.helpers.LauncherSnackbarHost
import org.elnix.aura.ui.navigation.horizontalMetadata
import org.elnix.aura.ui.navigation.verticalMetadata
import org.elnix.aura.ui.settings.customization.AppearanceTab
import org.elnix.aura.ui.settings.customization.BehaviorTab
import org.elnix.aura.ui.settings.debug.DebugTab
import org.elnix.aura.ui.settings.debug.LogsTab
import org.elnix.aura.ui.settings.debug.LogsViewerScreen
import org.elnix.aura.ui.warning.SignatureWarningDialog
import org.elnix.aura.ui.welcome.WelcomeScreen
import org.elnix.aura.ui.whatsnew.ChangelogsScreen
import org.elnix.aura.ui.whatsnew.WhatsNewBottomSheet


@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun MainAppUi(securityViewModel: SecurityViewModel = activityViewModel()) {
    val startScreen = NavigationRoute.Main
    val backStack = rememberNavBackStack(startScreen)
    val currentRoute by remember {
        derivedStateOf { backStack.lastOrNull() ?: NavigationRoute.Main }
    }

    val isLocked by securityViewModel.isLocked.asState()
    val screenToUnlock by securityViewModel.screenToUnlock.asState()
    val lockMethod by PrivateSettingsStore.lockMethod.asState()

    LaunchedEffect(currentRoute) {
        securityViewModel.onEnterNewRoute(currentRoute)
    }

    val navigator: Navigator = object : Navigator {
        override fun go(screen: NavigationRoute) {
            backStack.remove(screen)
            backStack.add(screen)
        }

        override fun navigate(screen: NavigationRoute) {
            if (!isLocked) {
                go(screen)
                return
            }

            if (NavigationRoute.LockScreen in backStack) return

            if (screen in NavigationRoute.settingsRoutes && lockMethod != None) {
                backStack.add(NavigationRoute.LockScreen)
                securityViewModel.requestUnlock(screen)
            } else {
                go(screen)
            }
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

    val hasSeenWelcome by PrivateSettingsStore.hasSeenWelcome.asStateNull()
    LaunchedEffect(hasSeenWelcome) {
        if (hasSeenWelcome == false) {
            navigator.navigate(NavigationRoute.Welcome)
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


                        entry<NavigationRoute.Welcome>(metadata = horizontalMetadata) { WelcomeScreen() }
                        entry<NavigationRoute.Settings>(metadata = horizontalMetadata) { SettingsScreen() }
                        entry<NavigationRoute.Appearance>(metadata = horizontalMetadata) { AppearanceTab() }
                        entry<NavigationRoute.Behavior>(metadata = horizontalMetadata) { BehaviorTab() }
                        entry<NavigationRoute.Changelogs>(metadata = horizontalMetadata) { ChangelogsScreen() }
                        entry<NavigationRoute.Debug>(metadata = horizontalMetadata) { DebugTab() }
                        entry<NavigationRoute.Logs>(metadata = horizontalMetadata) { LogsTab() }
                        entry<NavigationRoute.LogsViewer>(metadata = horizontalMetadata) { key -> LogsViewerScreen(key.filename) }
                        entry<NavigationRoute.LockScreen> {
                            when (lockMethod) {
                                None -> {
                                    // This block shouldn't be called
                                    backStack.remove(NavigationRoute.LockScreen)
                                    navigator.go(screenToUnlock!!)
                                    securityViewModel.unlock()
                                }

                                Pin -> {
                                    PinUnlock(
                                        onDismiss = {
                                            backStack.remove(NavigationRoute.LockScreen)
                                            securityViewModel.cancelUnlock()
                                        },
                                        onSuccess = {
                                            logD(SECURITY_SERVICE) { "onSuccess() called!" }
                                            backStack.remove(NavigationRoute.LockScreen)
                                            navigator.go(screenToUnlock!!)
                                            securityViewModel.unlock()
                                        }
                                    )
                                }

                                Pattern -> {
                                    PatternUnlock(
                                        onDismiss = {
                                            backStack.remove(NavigationRoute.LockScreen)
                                            securityViewModel.cancelUnlock()
                                        },
                                        onSuccess = {
                                            backStack.remove(NavigationRoute.LockScreen)
                                            navigator.go(screenToUnlock!!)
                                            securityViewModel.unlock()
                                        }
                                    )
                                }

                                Device -> {
                                    val ctx = LocalContext.current

                                    LaunchedEffect(screenToUnlock) {
                                        val activity = ctx.findFragmentActivity()
                                        if (activity != null && securityViewModel.isDeviceUnlockAvailable()) {
                                            securityViewModel.showDeviceUnlockPrompt(
                                                activity = activity,
                                                onSuccess = {
                                                    navigator.go(screenToUnlock!!)
                                                    securityViewModel.unlock()
                                                },
                                                onError = { msg ->
                                                    ctx.showToast(ctx.getString(R.string.authentication_error, msg))
                                                    backStack.remove(NavigationRoute.LockScreen)
                                                    securityViewModel.cancelUnlock()
                                                },
                                                onFailed = {
                                                    ctx.showToast(ctx.getString(R.string.authentication_failed))
                                                    backStack.remove(NavigationRoute.LockScreen)
                                                    securityViewModel.cancelUnlock()
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )

                if (screenToUnlock == null) {
                    WhatsNewBottomSheet()
                    GoogleLockingWarningDialog()
                    SignatureWarningDialog()
                }
            }
        }
    }
}