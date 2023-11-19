package com.yusy.keykeeper.model

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

enum class AppType {
    Website,
    AndroidAPP,
    HmAPP
}

data class AccountData(
    val id: String, // 存储id
    val uid: String, // 用户名/账号/id
    val encryptedPasswd: String, // 已加密密码
    val encryptFunc: String, // 加密方式
    val appType: AppType, // 应用类型
    val appName: String, // 应用名
    val appUrl: String, // 应用统一资源定位
    @DrawableRes val appIcon: Int, // 应用图标
    val createdAt: String, // 创建时间(最新)
) {
    ;
}
