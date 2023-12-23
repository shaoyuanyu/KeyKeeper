package com.yusy.keykeeper.ui.components.iconpainter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.yusy.keykeeper.R

@Composable
fun iconPainter(appIcon: ImageBitmap?): Painter {
    return if (appIcon != null) {
        BitmapPainter(appIcon)
    } else {
        painterResource(id = R.drawable.ic_launcher_foreground)
    }
}