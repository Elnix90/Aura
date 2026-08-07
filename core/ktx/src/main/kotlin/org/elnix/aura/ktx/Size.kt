package org.elnix.aura.ktx

import androidx.compose.ui.geometry.Size

@Suppress("NOTHING_TO_INLINE")
public inline fun Size.Companion.rect(sidePx: Float): Size =
    Size(sidePx, sidePx)