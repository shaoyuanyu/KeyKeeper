package com.yusy.keykeeper.ui.components.accountcard

import com.yusy.keykeeper.data.account.Account

data class AccountPreview(
    val id: Int = 0,
    var uid: String = "",
    var appName: String = "",
    var appUrl: String = "",
    var appIcon: String= "",
    var createdAt: String = ""
)

fun Account.toAccountPreview(): AccountPreview = AccountPreview(
    id = id,
    uid = uid,
    appName = appName,
    appUrl = appUrl,
    appIcon = appIcon,
    createdAt = createdAt
)