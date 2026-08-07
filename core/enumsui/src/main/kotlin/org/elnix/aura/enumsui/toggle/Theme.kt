package org.elnix.aura.enumsui.toggle

import org.elnix.aura.enumsui.ToggleButtonOption
import org.elnix.aura.i18n.R


public enum class Theme(
    override val resId: Int,
    override val iconEnabled: Int? = null,
    override val iconDisabled: Int? = null
) : ToggleButtonOption {
    Light(R.string.flashbang_theme),
    Dark(R.string.dark_theme),
    Amoled(R.string.amoled_theme),
    System(R.string.system_theme)
}
