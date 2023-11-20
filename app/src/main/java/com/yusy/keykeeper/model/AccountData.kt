package com.yusy.keykeeper.model

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.yusy.keykeeper.R

enum class AppType {
    Unknown,
    Website,
    AndroidAPP,
    HmAPP
}

data class AccountData(
    val id: String, // 存储id
    var uid: String, // 用户名/账号/id
    var encryptedPasswd: String, // 已加密密码
    var encryptFunc: String, // 加密方式
    var appType: AppType, // 应用类型
    var appName: String, // 应用名
    var appUrl: String, // 应用统一资源定位
    @DrawableRes var appIcon: Int = R.drawable.ic_launcher_foreground, // 应用图标
    var createdAt: String, // 创建时间(最新)
) {
    ;
}
