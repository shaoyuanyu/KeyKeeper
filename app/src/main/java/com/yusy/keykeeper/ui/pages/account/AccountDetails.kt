package com.yusy.keykeeper.ui.pages.account

import androidx.compose.ui.graphics.ImageBitmap
import com.yusy.keykeeper.data.account.Account
import com.yusy.keykeeper.data.account.AppType
import com.yusy.keykeeper.data.account.EncryptFunc
import com.yusy.keykeeper.utils.decryptPasswd
import com.yusy.keykeeper.utils.encryptPasswd
import com.yusy.keykeeper.utils.getIconImageBitmap

data class AccountDetails(
    val id: Int = 0,
    var uid: String = "",
    var plainPasswd: String = "",
    var encryptedPasswd: String = "",
    var encryptFunc: EncryptFunc = EncryptFunc.Fun1,
    var appType: AppType = AppType.Unknown,
    var appName: String = "",
    var appUrl: String = "",
    var appIconPath: String = "",
    var appIcon: ImageBitmap? = null,
    var createdAt: String = "",
)

fun AccountDetails.toAccount(): Account = Account(
    id = id,
    uid = uid,
    encryptedPasswd = encryptPasswd(plainPasswd, encryptFunc),
    encryptFunc = encryptFunc,
    appType = appType,
    appName = appName,
    appUrl = appUrl,
    appIconPath = appIconPath,
    createdAt = createdAt
)

fun Account.toAccountDetails(): AccountDetails = AccountDetails(
    id = id,
    uid = uid,
    plainPasswd = decryptPasswd(encryptedPasswd, encryptFunc),
    encryptFunc = encryptFunc,
    appType = appType,
    appName = appName,
    appUrl = appUrl,
    appIcon = getIconImageBitmap(appIconPath),
    createdAt = createdAt
)
