package com.yusy.keykeeper.data.account

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yusy.keykeeper.R

@Entity(tableName = "Account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // 主键id

    var uid: String, // 用户名/账号/邮箱/id
    var encryptedPasswd: String, // 已加密密码
    var encryptFunc: EncryptFunc, // 加密方式
    var appType: AppType, // 应用类型
    var appName: String, // 应用名
    var appUrl: String, // 应用统一资源定位
    @DrawableRes var appIcon: Int = R.drawable.ic_launcher_foreground, // 应用图标
    var createdAt: String, // 创建时间(最新)
) {
    ;
}

enum class AppType {
    Unknown,
    Website,
    AndroidAPP,
    HmAPP
}

enum class EncryptFunc {
    fun1,
}
