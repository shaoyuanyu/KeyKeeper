package com.yusy.keykeeper.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

// TODO:如何优雅地存储和读取bitmap尚需查找更多资料

fun getIconImageBitmap(path: String): ImageBitmap? {
    return if (path.isNotEmpty()) {
        if (File(path).exists()) {
            BitmapFactory.decodeFile(path).asImageBitmap()
        } else {
            null
        }
    } else {
        null
    }
}


fun storeIcon(context: Context, iconName: String, iconImageBitmap: ImageBitmap): String {
    val iconNameHash = iconName.hashCode().toString()

    context.openFileOutput(
        iconNameHash,
        Context.MODE_PRIVATE
    ).use {
        iconImageBitmap.asAndroidBitmap().compress(
            Bitmap.CompressFormat.PNG,
            100,
            it
        )
    }

    return (context.filesDir.toString() + "/" + iconNameHash)
}

fun deleteIcon(context: Context, iconName: String): Boolean =
    context.deleteFile(iconName.hashCode().toString())
