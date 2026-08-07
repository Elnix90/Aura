package org.elnix.aura.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.elnix.aura.i18n.R
import org.elnix.aura.ui.base.components.AnimatedFab
import org.elnix.aura.ui.base.components.Spacer

@Composable
fun WelcomePageFinish(
    onEnterApp: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.celebration),
                contentDescription = stringResource(R.string.everything_ready),
                modifier = Modifier.size(80.dp)
            )

            Text(
                text = stringResource(R.string.everything_ready),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp
            )
        }

        Spacer(32.dp)

        AnimatedFab(
            icon = R.drawable.rocket_launch,
            minSize = 200.dp,
            containerColor = MaterialTheme.colorScheme.secondary,
            onClick = onEnterApp
        )
    }
}
