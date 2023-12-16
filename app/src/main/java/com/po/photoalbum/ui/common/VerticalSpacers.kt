package com.po.photoalbum.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun CustomVerticalSpacer(
    size: Dp
) {
    Spacer(modifier = Modifier.height(size))
}
