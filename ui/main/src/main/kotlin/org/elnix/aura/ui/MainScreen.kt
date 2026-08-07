package org.elnix.aura.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import org.elnix.aura.ui.base.compositionlocals.LocalNavigator


@SuppressLint("LocalContextResourcesRead")
@Composable
fun MainScreen() {
    val navigator = LocalNavigator.current
}
