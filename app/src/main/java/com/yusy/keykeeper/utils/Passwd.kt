package com.yusy.keykeeper.utils

import com.yusy.keykeeper.data.account.EncryptFunc

fun encrypt(plainPasswd: String, encryptFunc: EncryptFunc): String {
    return plainPasswd
}

fun decrypt(encryptedPasswd: String, encryptFunc: EncryptFunc): String {
    return encryptedPasswd
}
