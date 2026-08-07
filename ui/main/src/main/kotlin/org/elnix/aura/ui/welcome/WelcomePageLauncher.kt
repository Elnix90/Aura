package org.elnix.aura.ui.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.elnix.aura.common.utils.rememberIsDefaultLauncher
import org.elnix.aura.i18n.R
import org.elnix.aura.ktx.openDefaultLauncherSettings
import org.elnix.aura.ui.helpers.GradientBigButton

@Composable
fun WelcomePageLauncher() {
    val ctx = LocalContext.current
    val isDefaultLauncher by rememberIsDefaultLauncher()

    WelcomePagerHeader(
        title = stringResource(R.string.set_default_launcher),
        icon = R.drawable.rocket_launch
    ) {
        GradientBigButton(
            text = if (isDefaultLauncher)
                stringResource(R.string.already_default_launcher)
            else
                stringResource(R.string.open_default_launcher_settings),
            enabled = !isDefaultLauncher,
            onClick = {
                ctx.openDefaultLauncherSettings()
            }
        )
    }
}
