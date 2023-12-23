package com.yusy.keykeeper.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.yusy.keykeeper.R

// TODO:如何优雅地存储和读取bitmap尚需查找更多资料

@Composable
fun getIconPainter(path: String): Painter {
    return if (path.isNotEmpty()) {
        BitmapPainter(BitmapFactory.decodeFile(path).asImageBitmap())
    } else {
        painterResource(id = R.drawable.ic_launcher_foreground)
    }
}

fun storeIcon(iconBitmap: Bitmap) {
}