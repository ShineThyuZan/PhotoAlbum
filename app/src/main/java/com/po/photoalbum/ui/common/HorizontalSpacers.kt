package com.po.photoalbum.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun CustomHorizontalSpacer(
    size: Dp
) {
    Spacer(modifier = Modifier.width(size))
}
