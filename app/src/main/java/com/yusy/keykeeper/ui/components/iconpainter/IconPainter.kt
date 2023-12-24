package com.yusy.keykeeper.ui.components.iconpainter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.AppType

@Composable
fun iconPainter(appIcon: ImageBitmap?, appType: AppType): Painter {
    return if (appIcon != null) {
        BitmapPainter(appIcon)
    } else {
        when (appType) {
            AppType.Website -> {
                painterResource(id = R.drawable.ic_website)
            }
            AppType.AndroidAPP, AppType.HmAPP -> {
                painterResource(id = R.drawable.ic_launcher_foreground)
            }
            else -> {
                painterResource(id = R.drawable.ic_launcher_foreground)
            }
        }
    }
}