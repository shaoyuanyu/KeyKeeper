package com.yusy.keykeeper.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

// TODO:如何优雅地存储和读取bitmap尚需查找更多资料

fun getIconImageBitmap(path: String): ImageBitmap? {
    return if (path.isNotEmpty()) {
        BitmapFactory.decodeFile(path).asImageBitmap()
    } else {
        null
    }
}


fun storeIcon(iconImageBitmap: ImageBitmap) {
}