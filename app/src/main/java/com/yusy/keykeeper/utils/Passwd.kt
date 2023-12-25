package com.yusy.keykeeper.utils

import android.annotation.SuppressLint
import com.yusy.keykeeper.data.account.EncryptFunc
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

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

fun getEncryptFunc(): EncryptFunc {
    return EncryptFunc.AES
}

fun getCryptKeys(encryptFunc: EncryptFunc): Pair<String, String> {
    val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return if (encryptFunc == EncryptFunc.AES) {
        val length = listOf(16, 24, 32).shuffled().first()
        val key = generateKey(charPool, length)
        Pair(key, key)
    } else {
        Pair("1234567887654321", "1234567887654321")
    }
}

fun generateKey(charPool: List<Char>, length: Int) = (1..length)
    .map { Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")

fun encryptPasswd(plainPasswd: String, encryptFunc: EncryptFunc, encryptKey: String): String {
    val encryptedPasswd = if (encryptFunc == EncryptFunc.AES) {
        Crypt.AESCrypt.encrypt(
            input = plainPasswd,
            key = encryptKey
        )
    } else {
        plainPasswd
    }

    return encryptedPasswd
}

fun decryptPasswd(encryptedPasswd: String, encryptFunc: EncryptFunc, decryptKey: String): String {
    val plainPasswd = if (encryptFunc == EncryptFunc.AES) {
        Crypt.AESCrypt.decrypt(
            input = encryptedPasswd,
            key = decryptKey
        )
    } else {
        encryptedPasswd
    }

    return plainPasswd
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

/**
 * 加解密算法
 */
object Crypt {
    object AESCrypt {
        // AES加密
        @SuppressLint("GetInstance")
        fun encrypt(input: String, key: String): String {
            // 初始化cipher对象
            val cipher = Cipher.getInstance("AES")
            // 生成密钥
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val encrypt = cipher.doFinal(input.toByteArray())
            val result = Base64.getEncoder().encode(encrypt)
            return String(result)
        }

        // AES解密
        @SuppressLint("GetInstance")
        fun decrypt(input: String, key: String): String {
            // 初始化cipher对象
            val cipher = Cipher.getInstance("AES")
            // 生成密钥
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val encrypt = cipher.doFinal(Base64.getDecoder().decode(input.toByteArray()))

            return String(encrypt)
        }
    }
}
