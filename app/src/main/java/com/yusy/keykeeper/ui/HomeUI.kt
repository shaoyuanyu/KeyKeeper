package com.yusy.keykeeper.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yusy.keykeeper.R
import com.yusy.keykeeper.model.AccountData
import com.yusy.keykeeper.model.AppType
import com.yusy.keykeeper.ui.components.AccountCard

@Composable
fun HomeUI(innerPadding: PaddingValues) {
    val testAccountData = AccountData(
        uid = "account",
        encryptedPasswd = "",
        encryptFunc = "",
        appType = AppType.AndroidAPP,
        appName = "test",
        appUrl = "com.yusy.test",
        appIcon = R.drawable.ic_launcher_foreground,
        createdAt = "2023/11/16"
    )
    val testAccountDataList = List(20){testAccountData}

    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            items(testAccountDataList) { item ->
                AccountCard(accountData = item)
            }
        }
    }
}