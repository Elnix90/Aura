package org.elnix.aura.models

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.elnix90.logging.SECURITY_SERVICE
import io.github.elnix90.logging.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.elnix.aura.base.Constants.Signatures.AURA_SIGNATURE_HASH
import org.elnix.aura.base.SettingFlow
import org.elnix.aura.base.navigaton.NavigationRoute
import org.elnix.aura.enumsui.toggle.LockMethod
import org.elnix.aura.enumsui.toggle.LockMethod.Device
import org.elnix.aura.enumsui.toggle.LockMethod.None
import org.elnix.aura.enumsui.toggle.LockMethod.Pattern
import org.elnix.aura.enumsui.toggle.LockMethod.Pin
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.checkSignature
import org.elnix.aura.ktx.showToast
import org.elnix.aura.models.utils.viewModelInitialized
import org.elnix.aura.security.SecurityService
import org.elnix.aura.settings.stores.map.PrivateSettingsStore
import javax.inject.Inject

@HiltViewModel
public class SecurityViewModel @Inject constructor(
    application: Application,
    private val securityService: SecurityService,
) : AndroidViewModel(application) {

    public val isLocked: SettingFlow<Boolean> = SettingFlow(false)
    public val screenToUnlock: SettingFlow<NavigationRoute?> = SettingFlow(null)

    private val lockMethod: Flow<LockMethod> = PrivateSettingsStore.lockMethod.flow(application)

    public val signatureMatched: SettingFlow<Boolean> = SettingFlow(true)
    public val useAnyways: SettingFlow<Boolean> = SettingFlow(false)

    init {
        signatureMatched.value = application.checkSignature(AURA_SIGNATURE_HASH)
        viewModelInitialized()
    }


    public fun removeLock() {
        viewModelScope.launch {
            PrivateSettingsStore.lockHash.reset(application)
            PrivateSettingsStore.lockMethod.reset(application)
            unlock()
        }
    }

    public fun setPinLockMethod(pin: String) {
        viewModelScope.launch {
            val hash = securityService.hash(pin)
            PrivateSettingsStore.lockHash.set(application, hash)
            PrivateSettingsStore.lockMethod.set(application, Pin)
            application.showToast(application.getString(R.string.pin_set_success))
            unlock()
        }
    }

    public fun setPatternLockMethod(pattern: String) {
        viewModelScope.launch {
            val hash = securityService.hash(pattern)
            PrivateSettingsStore.lockHash.set(application, hash)
            PrivateSettingsStore.lockMethod.set(application, Pattern)
            application.showToast(application.getString(R.string.pattern_set_successfully))
            unlock()
        }
    }

    public fun setLockScreenMethod() {
        viewModelScope.launch {
            PrivateSettingsStore.lockHash.reset(application)
            PrivateSettingsStore.lockMethod.set(application, Device)
            unlock()
        }
    }


    public fun lock() {
        logD(SECURITY_SERVICE) { "User asked to lock!" }
        isLocked.value = true
    }

    public fun unlock() {
        logD(SECURITY_SERVICE) { "User asked to unlock!" }
        isLocked.value = false
        screenToUnlock.value = null
    }

    public fun verify(pin: String, storedHash: String): Boolean = securityService.verify(pin, storedHash)

    public fun cancelUnlock() {
        screenToUnlock.value = null
    }

    public fun onEnterNewRoute(route: NavKey) {
        if (route !in NavigationRoute.settingsRoutes) {
            lock()
        }
    }


    public fun requestUnlock(targetScreen: NavigationRoute) {
        viewModelScope.launch {
            when (lockMethod.first()) {
                None -> unlock()

                Pin, Pattern, Device -> {
                    screenToUnlock.value = targetScreen
                }
            }
        }
    }

    public fun isDeviceUnlockAvailable(): Boolean = securityService.isDeviceUnlockAvailable(application)

    public fun showDeviceUnlockPrompt(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ): Unit = securityService.showDeviceUnlockPrompt(
        activity = activity,
        onSuccess = onSuccess,
        onError = onError,
        onFailed = onFailed
    )
}
