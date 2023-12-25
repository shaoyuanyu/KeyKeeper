package com.yusy.keykeeper.data.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // 主键id

    var uid: String, // 用户名/账号/邮箱/id

    var encryptedPasswd: String, // 已加密密码
    var encryptFunc: EncryptFunc, // 加密方式
    var encryptKey: String, // 加密密钥
    var decryptKey: String, // 解密密钥

    var appType: AppType, // 应用类型
    var appName: String, // 应用名
    var appUrl: String, // 应用统一资源定位
    var appIconPath: String = "", // 应用图标
    var createdAt: String, // 创建时间(最新)
)

enum class AppType {
    Unknown,
    Website,
    AndroidAPP,
    HmAPP
}

enum class EncryptFunc {
    AES,
}

val accountExampleAndroid = Account(
    id = 0,
    uid = "",
    encryptedPasswd = "123456",
    encryptFunc = EncryptFunc.AES,
    encryptKey = "1234567887654321",
    decryptKey = "1234567887654321",
    appType = AppType.AndroidAPP,
    appName = "test app",
    appUrl = "com.yusy.test",
    appIconPath = "",
    createdAt = "2023/12/23 12:00:00"
)

val accountExampleWebsite = Account(
    id = 0,
    uid = "",
    encryptedPasswd = "123456",
    encryptFunc = EncryptFunc.AES,
    encryptKey = "1234567887654321",
    decryptKey = "1234567887654321",
    appType = AppType.Website,
    appName = "test website",
    appUrl = "www.yusy.xyz",
    appIconPath = "",
    createdAt = "2023/12/23 12:00:00"
)
