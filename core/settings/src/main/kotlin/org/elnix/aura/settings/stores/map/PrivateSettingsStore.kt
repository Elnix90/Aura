package org.elnix.aura.settings.stores.map

import io.github.elnix90.annotations.SettingKey
import io.github.elnix90.annotations.SettingsStore
import io.github.elnix90.core.objects.BooleanSettingObject
import io.github.elnix90.core.objects.EnumSettingObject
import io.github.elnix90.core.objects.IntSettingObject
import io.github.elnix90.core.objects.StringSettingObject
import io.github.elnix90.core.objects.boolean
import io.github.elnix90.core.objects.enum
import io.github.elnix90.core.objects.int
import io.github.elnix90.core.objects.string
import io.github.elnix90.core.stores.MapSettingsStore
import org.elnix.aura.enumsui.toggle.LockMethod
import org.elnix.aura.i18n.R

@SettingsStore
public object PrivateSettingsStore : MapSettingsStore(backupable = false) {
    @SettingKey
    public val hasInitialized: BooleanSettingObject = boolean(
        title = R.string.has_initialized,
        default = false
    )

    @SettingKey
    public val hideBetaVersionWarning: BooleanSettingObject = boolean(
        title = R.string.hide_beta_version_warning,
        description = R.string.hide_beta_version_warning_desc,
        default = false
    )

    @SettingKey
    public val lastSeenVersionCodeWhatsNew: IntSettingObject = int(
        default = 0,
        allowedRange = 0..Int.MAX_VALUE
    )

    @SettingKey
    public val lastSeenVersionCodeGoogleLockdownWarning: IntSettingObject = int(
        default = 0,
        allowedRange = 0..Int.MAX_VALUE
    )

    /**
     *  Hashed code for settings lock (SHA-256).
     *  This can contain either the Pattern hashed or the PIN hashed.
     *  They are both stored as string, containing the digits in the LtR direction.
     *
     * For example:
     * Pin can be: `"1234"`, and a patteran can also be the same (`"0123"`)
     */
    @SettingKey
    public val lockHash: StringSettingObject = string("")

    /**
     * Only used when a pattern is used. determined the size of the used pattern
     *
     * CRITICAL: when the pattern size changes, the hash must be also recomputed!!
     */
    @SettingKey
    public val patternSize: IntSettingObject = int(
        title = R.string.pattern_size,
        description = R.string.pattern_size_desc,
        default = 3,
        allowedRange = 2..10
    )

    @SettingKey
    public val lockMethod: EnumSettingObject<LockMethod> = enum(LockMethod.None)

    @SettingKey
    public val lastCrashStackTrace: StringSettingObject = string("")


    @SettingKey
    public val useAppEvenIfSignatureIsNotMatched: BooleanSettingObject = boolean(false)

    @SettingKey
    public val doNotRemindMeAgainPinLockWarning: BooleanSettingObject = boolean(
        title = R.string.do_not_remind_me_again_pin_lock,
        description = R.string.do_not_remind_me_again_pin_lock_desc,
        default = false
    )
}