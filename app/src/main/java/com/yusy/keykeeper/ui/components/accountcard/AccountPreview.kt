package com.yusy.keykeeper.ui.components.accountcard

import androidx.compose.ui.graphics.ImageBitmap
import com.yusy.keykeeper.data.account.Account
import com.yusy.keykeeper.utils.getIconImageBitmap

data class AccountPreview(
    val id: Int = 0,
    var uid: String = "",
    var appName: String = "",
    var appUrl: String = "",
    var appIcon: ImageBitmap? = getIconImageBitmap(""),
    var createdAt: String = ""
)

fun Account.toAccountPreview(): AccountPreview = AccountPreview(
    id = id,
    uid = uid,
    appName = appName,
    appUrl = appUrl,
    appIcon = getIconImageBitmap(appIconPath),
    createdAt = createdAt
)