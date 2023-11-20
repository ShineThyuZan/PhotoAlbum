package com.po.photoalbum.ui.theme.resources

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BallGuruSizing(
    val tiny: Dp = 2.dp,
    val small: Dp = 4.dp,

    val base: Dp = 8.dp,
    val base_2x: Dp = 16.dp,
    val base_3x: Dp = 24.dp,
    val base_4x: Dp = 32.dp,
    val base_5x: Dp = 40.dp,
    val base_6x: Dp = 48.dp,
    val base_7x: Dp = 56.dp,
    val base_8x: Dp = 64.dp,
    val base_9x: Dp = 72.dp,
    val base_10x: Dp = 80.dp,
    val base_11x: Dp = 88.dp,
    val base_12x: Dp = 96.dp,

    val large: Dp = 120.dp,
    val extra_large: Dp = 128.dp,

    val small_icon: Dp = 20.dp,
)

val LocalSize = compositionLocalOf {
    BallGuruSizing()
}

val MaterialTheme.dimen: BallGuruSizing
    @Composable
    @ReadOnlyComposable
    get() = LocalSize.current
