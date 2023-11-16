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
    val test_account_data = AccountData(
        uid = "account",
        encryptedPasswd = "",
        encryptFunc = "",
        appType = AppType.AndroidAPP,
        appName = "test",
        appUrl = "com.yusy.test",
        appIcon = R.drawable.ic_launcher_foreground,
        createdAt = "2023/11/16"
    )
    val test_account_data_list = List<AccountData>(20){test_account_data}

    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            items(test_account_data_list) { item ->
                AccountCard(accountData = item)
            }
        }
    }
}