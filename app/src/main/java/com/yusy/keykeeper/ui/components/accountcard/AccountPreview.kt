package com.yusy.keykeeper.ui.components.accountcard

import androidx.compose.ui.graphics.ImageBitmap
import com.yusy.keykeeper.data.account.Account
import com.yusy.keykeeper.data.account.AppType
import com.yusy.keykeeper.utils.getIconImageBitmap

data class AccountPreview(
    val id: Int = 0,
    var uid: String = "",
    var appType: AppType = AppType.Unknown,
    var appName: String = "",
    var appUrl: String = "",
    var appIcon: ImageBitmap? = null,
    var createdAt: String = ""
)

fun Account.toAccountPreview(): AccountPreview = AccountPreview(
    id = id,
    uid = uid,
    appType = appType,
    appName = appName,
    appUrl = appUrl,
    appIcon = getIconImageBitmap(appIconPath),
    createdAt = createdAt
)