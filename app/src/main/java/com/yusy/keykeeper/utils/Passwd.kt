package com.yusy.keykeeper.utils

import com.yusy.keykeeper.data.account.EncryptFunc

fun generatePasswd(): String {
    val validPasswdChar = generateValidPasswdChar()
    val passwdLen = (12..15).random()
    val passwdCharList = arrayListOf<Char>()
    var passwd = ""

    passwdCharList.add(validPasswdChar[(0..9).random()]) // 数字
    passwdCharList.add(validPasswdChar[(10..35).random()]) // 小写字母
    passwdCharList.add(validPasswdChar[(36..61).random()]) // 大写字母
    passwdCharList.add(validPasswdChar[(62..65).random()]) // 符号
    for (i in 5..passwdLen) {
        passwdCharList.add(validPasswdChar[(0..65).random()])
    }

    for (i in 1..passwdLen) {
        val locate = (0 until passwdCharList.size).random()
        passwd += passwdCharList[locate]
        passwdCharList.removeAt(locate)
    }

    return passwd
}

/**
 * 生成密码合法字符
 */
fun generateValidPasswdChar() : ArrayList<Char> {
    val validPasswdChar = arrayListOf<Char>()

    for (i in '0'..'9') {
        validPasswdChar.add(i)
    }
    for (i in 'a'..'z') {
        validPasswdChar.add(i)
    }
    for (i in 'A'..'Z') {
        validPasswdChar.add(i)
    }
    validPasswdChar.add('#')
    validPasswdChar.add('+')
    validPasswdChar.add('-')
    validPasswdChar.add('=')

    return validPasswdChar
}

fun encryptPasswd(plainPasswd: String, encryptFunc: EncryptFunc): String {
    return plainPasswd
}

fun decryptPasswd(encryptedPasswd: String, encryptFunc: EncryptFunc): String {
    return encryptedPasswd
}
