package org.elnix.aura

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.elnix90.logging.TAG
import io.github.elnix90.logging.logI
import io.github.elnix90.runtime.asState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.elnix.aura.models.DragonLogViewModel
import org.elnix.aura.settings.stores.map.BehaviorSettingsStore
import org.elnix.aura.settings.stores.map.PrivateSettingsStore
import org.elnix.aura.settings.stores.map.UiSettingsStore
import org.elnix.aura.theme.AuraTheme
import org.elnix.aura.ui.MainAppUi
import org.elnix.aura.ui.base.activityViewModel
import org.elnix.aura.ui.dialogs.CrashScreen

@AndroidEntryPoint
class MainActivity : FragmentActivity() {


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()
        // Use hardware acceleration ASAP
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

        super.onCreate(savedInstanceState)
        logI(TAG) { "MainActivity.onCreate started, hash=${System.identityHashCode(this)}" }

        var lastStackTrace by mutableStateOf(
            runBlocking {
                PrivateSettingsStore.lastCrashStackTrace.getOrNull(this@MainActivity)
            }
        )

        enableEdgeToEdge()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        }

        setContent {

            val ctx = LocalContext.current
            val scope = rememberCoroutineScope()

            if (lastStackTrace.isNullOrBlank()) {

                // Loads the logging system, do not remove or you won't have any logs!
                @Suppress("UnusedVariable", "unused")
                val dragonLogViewModel: DragonLogViewModel = activityViewModel()

                AuraTheme {
                    // Force launch of full viewmodel after first frame for performance
                    // This avoids layout & loading overlap
                    LaunchedEffect(Unit) {
                        lifecycleScope.launch(Dispatchers.Default) {
                            yield() // Wait for first frame
                            logI(TAG) { "First frame rendered in ${System.currentTimeMillis() - startTime}ms." }

//                            // All stores excepted the non-backupable ones, cause they trigger updates constantly (e.g., last backup time)
//                            AllStores.forEach { store ->
//                                store.onAnySettingChanged = {
//                                    backupViewModel.commandBackup()
//                                }
//                            }
                        }
                    }

                    val keepScreenOn by BehaviorSettingsStore.keepScreenOn.asState()
                    val fullscreen by UiSettingsStore.fullScreen.asState()

                    val window = this@MainActivity.window
                    val controller = WindowInsetsControllerCompat(window, window.decorView)

                    LaunchedEffect(keepScreenOn) {
                        if (keepScreenOn) {
                            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        } else {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        }
                    }

                    LaunchedEffect(Unit, fullscreen) {
                        if (fullscreen) {
                            controller.hide(WindowInsetsCompat.Type.systemBars())
                            controller.systemBarsBehavior =
                                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        } else {
                            controller.show(WindowInsetsCompat.Type.systemBars())
                        }
                    }

                    MainAppUi()
                }
            } else {
                MaterialTheme {
                    CrashScreen(
                        stackTrace = lastStackTrace ?: "Unable to recover last stackTrace",
                        onDismiss = {
                            scope.launch {
                                PrivateSettingsStore.lastCrashStackTrace.reset(ctx)
                            }
                            lastStackTrace = null
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}