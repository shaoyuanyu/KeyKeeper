package com.yusy.keykeeper.ui.pages.account

import androidx.annotation.DrawableRes
import com.yusy.keykeeper.R
import com.yusy.keykeeper.data.account.Account
import com.yusy.keykeeper.data.account.AppType
import com.yusy.keykeeper.data.account.EncryptFunc
import com.yusy.keykeeper.utils.decrypt
import com.yusy.keykeeper.utils.encrypt

data class AccountUiState(
    val accountDetails: AccountDetails = AccountDetails(),
    val isValid: Boolean = true
)

data class AccountDetails(
    val id: Int = 0,
    var uid: String = "",
    var plainPasswd: String = "",
    var encryptedPasswd: String = "",
    var encryptFunc: EncryptFunc = EncryptFunc.fun1,
    var appType: AppType = AppType.Unknown,
    var appName: String = "",
    var appUrl: String = "",
    @DrawableRes var appIcon: Int = R.drawable.ic_launcher_foreground,
    var createdAt: String = "",
)

fun AccountDetails.toAccount(): Account = Account(
    id = id,
    uid = uid,
    encryptedPasswd = encrypt(plainPasswd, encryptFunc),
    encryptFunc = encryptFunc,
    appType = appType,
    appName = appName,
    appUrl = appUrl,
    appIcon = appIcon,
    createdAt = createdAt
)

fun AccountDetails.toAccountUiState(isValid: Boolean): AccountUiState = AccountUiState(
    accountDetails = this,
    isValid = isValid
)

fun Account.toAccountDetails(): AccountDetails = AccountDetails(
    id = id,
    uid = uid,
    plainPasswd = decrypt(encryptedPasswd, encryptFunc),
    encryptFunc = encryptFunc,
    appType = appType,
    appName = appName,
    appUrl = appUrl,
    appIcon = appIcon,
    createdAt = createdAt
)
