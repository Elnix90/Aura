package org.elnix.aura.enumsui.toggle

import org.elnix.aura.i18n.R
import org.elnix.aura.enumsui.ToggleButtonOption

public enum class BackupSelectStoresButtons(
    override val resId: Int?,
    override val iconEnabled: Int,
    override val iconDisabled: Int? = null
) : ToggleButtonOption {
    DeselectAll(R.string.deselect_all, R.drawable.deselect),
    SelectAll(R.string.select_all, R.drawable.select_all),
    Invert(R.string.invert, R.drawable.swap_calls)
}
